package org.yogurt.protobufftools;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import sun.security.util.Length;


public class ReflectiveEncoder implements IMessageEncoder {
    public byte[] encode(Object o) throws Exception {
        Object builder = populateBuilder(o);
        Object built = builder.getClass().getDeclaredMethod("build").invoke(builder);
        byte[] payload = (byte[]) built.getClass().getMethod("toByteArray").invoke(built);
        return new MessageWrapper().wrap(o.getClass().getCanonicalName(), payload);
    }

    public Object decode(byte[] bytes) throws Exception {
        Message message = new MessageWrapper().unwrap(bytes);
        Object o = Class.forName(message.getMessageType()).newInstance();

        Class<?> bufferClass = getProtoBufferClass(o);
        Object buffer = bufferClass.getMethod("parseFrom", byte[].class).invoke(null, message.getPayload());
        return extractFromBuffer(o, buffer);
    }

    private Object populateBuilder(Object o) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Object builder = getProtoBufferClass(o).getMethod("newBuilder").invoke(null);

        for (Field field : getBufferFields(o)) {
            ProtoBufferField annotation = field.getAnnotation(ProtoBufferField.class);
            Method getter = getGetter(o, field.getName());

            if (fieldShouldBeRecursed(field)) {
                Class<?> type = field.getType().getAnnotation(ProtoBufferData.class).protoBuffer();
                Object newBuilder = type.getMethod("newBuilder").invoke(null);
                Method setter = getSetter(builder, annotation.fieldName(), newBuilder.getClass());

                setter.invoke(builder, populateBuilder(getter.invoke(o)));
            } else {
                Method setter = getSetter(builder, annotation.fieldName(), field.getType());
                setter.invoke(builder, getter.invoke(o));
            }
        }

        return builder;
    }

    private Object extractFromBuffer(Object o, Object buffer) throws Exception {
        for (Field field : getBufferFields(o)) {
            Method setter = getSetter(o, field.getName(), field.getType());
            
            

            
            Object fieldValue = new ReflectiveObject(buffer).invoke("get" + capitalize(field.getAnnotation(ProtoBufferField.class).fieldName()));
            
            
			if (fieldShouldBeRecursed(field)) {
                setter.invoke(o, extractFromBuffer(field.getType().newInstance(), fieldValue));
            } else {
            	new ReflectiveObject(o).invoke("set" + capitalize(field.getName()), fieldValue);
                setter.invoke(o, fieldValue);
            }

        }
        return o;
    }

    private Set<Field> getBufferFields(Object o) {
        Set<Field> fields = new HashSet<>();
        for (Field field : o.getClass().getDeclaredFields()) {
            for (Annotation declaredAnnotation : field.getDeclaredAnnotations()) {
                if (declaredAnnotation.annotationType().equals(ProtoBufferField.class)) {
                    fields.add(field);
                }
            }
        }
        return fields;
    }

    private boolean fieldShouldBeRecursed(Field field) {
        return field.getType().getAnnotation(ProtoBufferData.class) != null;
    }

    private Class<?> getProtoBufferClass(Object o) {
        return o.getClass().getAnnotation(ProtoBufferData.class).protoBuffer();
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
    
    private class ReflectiveObject{
    	Object o;
    	ReflectiveObject(Object o){
			this.o = o;
    	}
    	
    	public Object invoke(String methodName, Object... params) throws Exception{
    		
    		Class<?>[] types = new Class[0];
    		if(params != null){
    			for (Object object : params) {
    				types = Arrays.copyOf(types, types.length + 1);
    				types[types.length - 1] = object.getClass();
    			}
    			
				return  o.getClass().getMethod(methodName, types).invoke(o, params);
    		}
    		return  o.getClass().getMethod(methodName, (Class[]) null).invoke(o, params);
    	}
    	
    	
    	public Object invoke(String methodName) throws Exception{
    		return invoke(methodName, (Object[])null);
    	}
    }

}
