package org.yogurt.protobufftools;

import org.apache.commons.lang3.reflect.MethodUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
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
        return extractFromBuffer(o, buffer);
    }

    private Object populateBuilder(ReflectiveObject o) throws Exception {
        ReflectiveObject builder = createBuffer(o,"newBuilder");

        for (Field field : o.getFieldsAnnotatedWith(ProtoBufferField.class)) {
            String fieldName = field.getAnnotation(ProtoBufferField.class).fieldName();
            ReflectiveObject invoked = o.smartGet(field.getName());
            if (fieldShouldBeRecursed(field)) {
                builder.smartSet(fieldName, populateBuilder(invoked));
            } else {
                builder.smartSet(fieldName, invoked);
            }
        }

        return builder.getObject();
    }

    private Object extractFromBuffer(ReflectiveObject o, ReflectiveObject buffer) throws Exception {

        Set<Field> bufferFields = o.getFieldsAnnotatedWith(ProtoBufferField.class);
        for (Field field : bufferFields) {
            ReflectiveObject fieldValue = buffer.smartGet(field.getAnnotation(ProtoBufferField.class).fieldName());
            if (fieldShouldBeRecursed(field)) {
                o.smartSet(field.getName(), extractFromBuffer(new ReflectiveObject(field.getType()), fieldValue));
            } else {
                o.smartSet(field.getName(), fieldValue);
            }

        }
        return o.getObject();
    }

    private ReflectiveObject createBuffer(ReflectiveObject o, String methodName, Object... params) throws Exception{
        return new ReflectiveObject(MethodUtils.invokeStaticMethod(getProtoBufferClass(o), methodName, params));
    }

    private boolean fieldShouldBeRecursed(Field field) {
        return field.getType().getAnnotation(ProtoBufferData.class) != null;
    }

    private Class<?> getProtoBufferClass(ReflectiveObject o) {
        return o.getObject().getClass().getAnnotation(ProtoBufferData.class).protoBuffer();
    }
}
