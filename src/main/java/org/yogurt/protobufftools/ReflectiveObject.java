package org.yogurt.protobufftools;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.reflect.MethodUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;

class ReflectiveObject {
    Object o;

    ReflectiveObject(Class<?> clazz) throws Exception {
        this.o = clazz.newInstance();
    }

    ReflectiveObject(Object o) {
        this.o = o;
    }

    public Object getObject() {
        return o;
    }

    public ReflectiveObject smartGet(String fieldName) throws Exception {
        try {
            return new ReflectiveObject(MethodUtils.invokeMethod(o, "get" + capitalize(fieldName)));
        } catch (NoSuchMethodException e) {
            return new ReflectiveObject(FieldUtils.readDeclaredField(o, fieldName, true));
        }
    }

    public void smartSet(String fieldName, ReflectiveObject value) throws Exception {
        smartSet(fieldName, value.getObject());
    }

    public void smartSet(String fieldName, Object value) throws Exception {
        try {
            MethodUtils.invokeMethod(o, "set" + capitalize(fieldName), value);
        } catch (NoSuchMethodException e) {
            FieldUtils.writeDeclaredField(o, fieldName, value, true);
        }
    }

    public Set<Field> getFieldsAnnotatedWith(Type protoBufferFieldClass) {
        Set<Field> fields = new HashSet<>();
        for (Field field : o.getClass().getDeclaredFields()) {
            for (Annotation declaredAnnotation : field.getDeclaredAnnotations()) {
                if (declaredAnnotation.annotationType().equals(protoBufferFieldClass)) {
                    fields.add(field);
                }
            }
        }
        return fields;
    }

    private String capitalize(String line) {
        return Character.toUpperCase(line.charAt(0)) + line.substring(1);
    }

    public void call(String methodName, Object... objects) throws Exception {
        MethodUtils.invokeMethod(o, methodName, objects);
    }
}
