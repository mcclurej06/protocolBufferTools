package org.yogurt.protobufftools;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashSet;
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
        Object o = Class.forName(message.getMessageType()).newInstance();

        Class<?> bufferClass = getProtoBufferClass(o);

        Object buffer = bufferClass.getMethod("parseFrom", byte[].class).invoke(null, message.getPayload());
        return extractFromBuffer(new ReflectiveObject(o), new ReflectiveObject(buffer));
    }

    private Object populateBuilder(ReflectiveObject o) throws Exception {
        ReflectiveObject builder = new ReflectiveObject(getProtoBufferClass(o.getObject()).getMethod("newBuilder").invoke(null));

        for (Field field : o.getFieldsAnnotatedWith(ProtoBufferField.class)) {
            String fieldName = field.getAnnotation(ProtoBufferField.class).fieldName();
            Object invoked = o.smartGet(field.getName());
            if (fieldShouldBeRecursed(field)) {
                builder.smartSet(fieldName, populateBuilder(new ReflectiveObject(invoked)));
            } else {
                builder.smartSet(fieldName, invoked);
            }
        }

        return builder.getObject();
    }

    private Object extractFromBuffer(ReflectiveObject o, ReflectiveObject buffer) throws Exception {

        Set<Field> bufferFields = o.getFieldsAnnotatedWith(ProtoBufferField.class);
        for (Field field : bufferFields) {
            Object fieldValue = buffer.smartGet(field.getAnnotation(ProtoBufferField.class).fieldName());
			if (fieldShouldBeRecursed(field)) {
                o.smartSet(field.getName(), extractFromBuffer(new ReflectiveObject(field.getType().newInstance()), new ReflectiveObject(fieldValue)));
            } else {
                o.smartSet(field.getName(), fieldValue);
            }

        }
        return o.getObject();
    }

    private boolean fieldShouldBeRecursed(Field field) {
        return field.getType().getAnnotation(ProtoBufferData.class) != null;
    }

    private Class<?> getProtoBufferClass(Object o) {
        return o.getClass().getAnnotation(ProtoBufferData.class).protoBuffer();
    }

    private String capitalize(String line) {
        return Character.toUpperCase(line.charAt(0)) + line.substring(1);
    }

}
