package org.yogurt.protobufftools;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.reflect.MethodUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;

public class ReflectiveObject {
    Object o;

    public ReflectiveObject(Class<?> clazz) throws Exception {
        this.o = clazz.newInstance();
    }

    public ReflectiveObject(Object o) {
        this.o = o;
    }

    public Object getObject() {
        return o;
    }

    public ReflectiveObject smartGet(String fieldName) throws Exception {
        try {
            return new ReflectiveObject(MethodUtils.invokeMethod(o, "get" + StringUtils.capitalize(fieldName)));
        } catch (NoSuchMethodException e) {
            return new ReflectiveObject(FieldUtils.readDeclaredField(o, fieldName, true));
        }
    }

    public void smartSet(String fieldName, ReflectiveObject value) throws Exception {
        smartSet(fieldName, value.getObject());
    }

    public void smartSet(String fieldName, Object value) throws Exception {
        System.err.println("trying to set " + value.getClass() + " type to field " + fieldName);
        try {
            MethodUtils.invokeMethod(o, "set" + StringUtils.capitalize(fieldName), value);
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

    public void call(String methodName, Object... objects) throws Exception {
        MethodUtils.invokeMethod(o, methodName, objects);
    }
}
