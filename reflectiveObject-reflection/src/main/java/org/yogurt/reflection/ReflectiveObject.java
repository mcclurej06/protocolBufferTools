package org.yogurt.reflection;

import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import sun.reflect.ReflectionFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.*;

public class ReflectiveObject implements IReflectiveObject {
    Object o;

    ReflectiveObject(Class<?> clazz) throws Exception {
        final ReflectionFactory reflection = ReflectionFactory.getReflectionFactory();
        final Constructor<?> constructor =
                reflection.newConstructorForSerialization(
                        clazz, Object.class.getDeclaredConstructor());
        this.o = constructor.newInstance();
    }

    ReflectiveObject(Object o) {
        this.o = o;
    }

    @Override
    public Object getObject() {
        return o;
    }

    @Override
    public IReflectiveObject smartGet(String fieldName) throws Exception {
        try {
            return new ReflectiveObject(MethodUtils.invokeMethod(o, "get" + StringUtils.capitalize(fieldName)));
        } catch (NoSuchMethodException e) {
            return new ReflectiveObject(FieldUtils.readDeclaredField(o, fieldName, true));
        }

    }

    @Override
    public void smartSet(String fieldName, IReflectiveObject value) throws Exception {
        smartSet(fieldName, value.getObject());
    }

    @Override
    public void smartSet(String fieldName, Object value) throws Exception {
        try {
            call("set" + StringUtils.capitalize(fieldName), value);
        } catch (NoSuchMethodException e) {
            FieldUtils.writeDeclaredField(o, fieldName, value, true);
        }
    }

    @Override
    public Set<Field> getFieldsAnnotatedWith(Type annotationType) {
        Set<Field> fields = new HashSet<>();
        for (Field field : o.getClass().getDeclaredFields()) {
            for (Annotation declaredAnnotation : field.getDeclaredAnnotations()) {
                if (declaredAnnotation.annotationType().equals(annotationType)) {
                    fields.add(field);
                }
            }
        }
        return fields;
    }

    @Override
    public void call(String methodName, Object... objects) throws Exception {
        MethodUtils.invokeMethod(o, methodName, objects);
    }
}
