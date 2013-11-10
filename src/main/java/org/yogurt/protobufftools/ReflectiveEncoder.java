package org.yogurt.protobufftools;

import org.reflections.Reflections;
import org.reflections.scanners.FieldAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Set;

public class ReflectiveEncoder implements IMessageEncoder {
    public byte[] encode(Object o) throws Exception {
        Object builder = getProtoBufferClass(o).getMethod("newBuilder").invoke(null);

        for (Field f : getBufferFields(o)) {
            ProtoBufferField annotation = f.getAnnotation(ProtoBufferField.class);

            Method setter = getSetter(builder, annotation.fieldName(), f.getType());
            Method getter = getGetter(o, f.getName());
            setter.invoke(builder, getter.invoke(o));
        }

        Object built = builder.getClass().getDeclaredMethod("build").invoke(builder);
        byte[] payload = (byte[]) built.getClass().getMethod("toByteArray").invoke(built);

        return new MessageWrapper().wrap(o.getClass().getCanonicalName(), payload);
    }

    public Object decode(byte[] bytes) throws Exception {
        Message message = new MessageWrapper().unwrap(bytes);

        Object o = Class.forName(message.getMessageType()).newInstance();

        Class<?> bufferClass = getProtoBufferClass(o);
        Object personProto = bufferClass.getMethod("parseFrom", byte[].class).invoke(null, message.getPayload());

        for (Field field : getBufferFields(o)) {
            ProtoBufferField annotation = field.getAnnotation(ProtoBufferField.class);

            Method getter = getGetter(personProto, annotation.fieldName());
            Method setter = getSetter(o, field.getName(), field.getType());

            Object data = getter.invoke(personProto);
            setter.invoke(o, data);
        }

        return o;
    }

    private Class<?> getProtoBufferClass(Object o) {
        return o.getClass().getAnnotation(ProtoBufferData.class).protoBuffer();
    }

    private Set<Field> getBufferFields(Object o) {
        return new Reflections(new ConfigurationBuilder().addUrls(ClasspathHelper.forClass(o.getClass())).setScanners(new FieldAnnotationsScanner())).getFieldsAnnotatedWith(ProtoBufferField.class);
    }

    private Method getSetter(Object o, String field, Class<?> type) throws NoSuchMethodException {
        return o.getClass().getMethod("set" + capitalize(field), type);
    }
    private Method getGetter(Object o, String field) throws NoSuchMethodException {
        return o.getClass().getMethod("get" + capitalize(field), (Class<?>[]) null);
    }

    private String capitalize(String line) {
        return Character.toUpperCase(line.charAt(0)) + line.substring(1);
    }

}
