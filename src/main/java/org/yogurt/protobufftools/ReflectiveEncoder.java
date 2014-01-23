package org.yogurt.protobufftools;

import com.google.protobuf.ByteString;
import org.apache.commons.lang3.reflect.MethodUtils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Set;

public class ReflectiveEncoder implements IMessageEncoder {
    public byte[] encode(Object o) throws Exception {
        Object builder = populateBuilder(new ReflectiveObject(o));
        Object built = builder.getClass().getDeclaredMethod("build").invoke(builder);
        byte[] payload = (byte[]) built.getClass().getMethod("toByteArray").invoke(built);
        return new MessageWrapper().wrap(o.getClass().getCanonicalName(), payload);
    }

    public Object decode(byte[] bytes) throws Exception {
        Message message = new MessageWrapper().unwrap(bytes);
        ReflectiveObject o = new ReflectiveObject(Class.forName(message.getMessageType()).newInstance());

        ReflectiveObject buffer = createBuffer(o, "parseFrom", message.getPayload());
        return extractFromBuffer(o, buffer).getObject();
    }

    private Object populateBuilder(ReflectiveObject o) throws Exception {
        ReflectiveObject builder = createBuffer(o, "newBuilder");

        for (Field field : o.getFieldsAnnotatedWith(ProtoBufferField.class)) {
            String fieldName = field.getAnnotation(ProtoBufferField.class).fieldName();
            ReflectiveObject invoked = o.smartGet(field.getName());
            if (fieldShouldBeRecursed(field)) {
                builder.smartSet(fieldName, populateBuilder(invoked));
            } else if (isByteArrayField(field)) {
                builder.smartSet(fieldName, ByteString.copyFrom((byte[]) invoked.getObject()));
            } else {
                builder.smartSet(fieldName, invoked);
            }
        }

        for (Field field : o.getFieldsAnnotatedWith(ProtoBufferList.class)) {
            String fieldName = field.getAnnotation(ProtoBufferList.class).fieldName();
            ReflectiveObject invoked = o.smartGet(field.getName());
            builder.call("addAll"+capitalize(fieldName), invoked.getObject());
        }

        return builder.getObject();
    }
    private String capitalize(String line) {
        return Character.toUpperCase(line.charAt(0)) + line.substring(1);
    }
    private boolean isStringList(Field field) {
        Type genericType = field.getGenericType();
        if(ParameterizedType.class.isAssignableFrom(genericType.getClass())){
            Type[] actualTypeArguments = ((ParameterizedType) genericType).getActualTypeArguments();
            for (Type type : actualTypeArguments) {
                if (!String.class.equals(type)){
                    return false;
                }
            }
        } else {
            return false;
        }
        return true;
    }

    private ReflectiveObject extractFromBuffer(ReflectiveObject o, ReflectiveObject buffer) throws Exception {
        Set<Field> bufferFields = o.getFieldsAnnotatedWith(ProtoBufferField.class);
        for (Field field : bufferFields) {
            ReflectiveObject fieldValue = buffer.smartGet(field.getAnnotation(ProtoBufferField.class).fieldName());
            if (fieldShouldBeRecursed(field)) {
                o.smartSet(field.getName(), extractFromBuffer(new ReflectiveObject(field.getType()), fieldValue));
            } else if (isByteArrayField(field)) {
                o.smartSet(field.getName(), ((ByteString) fieldValue.getObject()).toByteArray());
            } else {
                o.smartSet(field.getName(), fieldValue);
            }

        }

        for (Field field : o.getFieldsAnnotatedWith(ProtoBufferList.class)) {
            o.smartSet(field.getName(), buffer.smartGet(capitalize(field.getAnnotation(ProtoBufferList.class).fieldName())+"List"));
        }
        return o;
    }

    private ReflectiveObject createBuffer(ReflectiveObject o, String methodName, Object... params) throws Exception {
        return new ReflectiveObject(MethodUtils.invokeStaticMethod(getProtoBufferClass(o), methodName, params));
    }

    private boolean isByteArrayField(Field field) {
        return field.getType().isAssignableFrom(byte[].class);
    }

    private boolean fieldShouldBeRecursed(Field field) {
        return field.getType().getAnnotation(ProtoBufferData.class) != null;
    }

    private Class<?> getProtoBufferClass(ReflectiveObject o) {
        return o.getObject().getClass().getAnnotation(ProtoBufferData.class).protoBuffer();
    }
}
