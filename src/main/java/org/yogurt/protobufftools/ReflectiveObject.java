package org.yogurt.protobufftools;

import org.apache.commons.lang3.reflect.MethodUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

class ReflectiveObject {
    Object o;
    ReflectiveObject(Object o){
        this.o = o;
    }

    public Object getObject(){
        return o;
    }

    public Object invoke(String methodName, Object... params) throws Exception{

        if(params != null){
            Class<?>[] types = getTypes(params);
            return MethodUtils.getMatchingAccessibleMethod(o.getClass(), methodName, types).invoke(o, params);
        }
        return MethodUtils.getMatchingAccessibleMethod(o.getClass(), methodName, null).invoke(o, params);
    }


    private Class<?>[] getTypes(Object[] params) {
        Class<?>[] types = new Class[0];
        for (Object object : params) {
            types = Arrays.copyOf(types, types.length + 1);
            types[types.length - 1] = object.getClass();
        }
        return types;
    }

    public Object invoke(String methodName) throws Exception{
        return invoke(methodName, (Object[])null);
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
}
