package org.yogurt.protobufftools;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ReflectiveEncoder implements IMessageEncoder{
    public byte[] encode(Object o) throws Exception {
        Class<?> buffer = o.getClass().getAnnotation(ProtoBufferData.class).protoBuffer();
        Object builder = buffer.getMethod("newBuilder").invoke(null);

        for (Field f : o.getClass().getDeclaredFields()) {
            ProtoBufferField annotation = f.getAnnotation(ProtoBufferField.class);
            if (annotation == null) {
                continue;
            }

            Method setter = builder.getClass().getDeclaredMethod("set" + capitalize(annotation.fieldName()), f.getType());
            Method getter = o.getClass().getMethod("get" + capitalize(f.getName()));
            setter.invoke(builder, getter.invoke(o));
        }

        Object built = builder.getClass().getDeclaredMethod("build").invoke(builder);
        byte[] payload = (byte[]) built.getClass().getMethod("toByteArray").invoke(built);

        return new MessageWrapper().wrap(o.getClass().getCanonicalName(), payload);
    }

    public Object decode(byte[] bytes) throws Exception {
        Message message = new MessageWrapper().unwrap(bytes);

        Object o = Class.forName(message.getMessageType()).newInstance();

        Class<?> buffer = o.getClass().getAnnotation(ProtoBufferData.class).protoBuffer();
        Object personProto = buffer.getMethod("parseFrom", byte[].class).invoke(null, message.getPayload());


        for (Field field : o.getClass().getDeclaredFields()) {
            ProtoBufferField annotation = field.getAnnotation(ProtoBufferField.class);
            if (annotation == null) {
                continue;
            }

            Method getter = personProto.getClass().getDeclaredMethod("get" + capitalize(annotation.fieldName()));
            Object data = getter.invoke(personProto);
            Method setter = o.getClass().getMethod("set" + capitalize(field.getName()), field.getType());
            setter.invoke(o, data);
        }


        return o;
    }

    private String capitalize(String line) {
        return Character.toUpperCase(line.charAt(0)) + line.substring(1);
    }

}
