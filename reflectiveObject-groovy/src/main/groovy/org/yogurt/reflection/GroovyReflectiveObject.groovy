package org.yogurt.reflection

import com.sun.xml.internal.ws.util.StringUtils

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Type

public class GroovyReflectiveObject implements IReflectiveObject {
    Object object;

    GroovyReflectiveObject(Class<?> clazz) throws Exception {
        this.object = clazz.newInstance();
    }

    GroovyReflectiveObject(Object object) {
        this.object = object;
    }

    @Override
    public IReflectiveObject smartGet(String fieldName) throws Exception {
        return new GroovyReflectiveObject(object."$fieldName");
    }

    @Override
    public void smartSet(String fieldName, IReflectiveObject value) throws Exception {
        smartSet(fieldName, value.object)
    }

    @Override
    public void smartSet(String fieldName, Object value) throws Exception {
        object."$fieldName" = value
    }

    @Override
    public Set<Field> getFieldsAnnotatedWith(Type annotationType) {
        //TODO: this isnt groovy
        Set<Field> fields = new HashSet<>();
        for (Field field : object.getClass().getDeclaredFields()) {
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
        object."$methodName"(*objects)
    }
}
