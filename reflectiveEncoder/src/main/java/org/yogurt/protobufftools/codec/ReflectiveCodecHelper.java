package org.yogurt.protobufftools.codec;

import org.apache.commons.lang3.reflect.MethodUtils;
import org.yogurt.protobufftools.ProtoBufferData;
import org.yogurt.reflection.IReflectiveObject;
import org.yogurt.reflection.ReflectiveObjectFactoryProvider;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class ReflectiveCodecHelper {
    public static Type getListType(Field field) {
        return ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
    }

    public static IReflectiveObject createBuffer(IReflectiveObject o, String methodName, Object... params) throws Exception {
        return ReflectiveObjectFactoryProvider.provide().create(MethodUtils.invokeStaticMethod(getProtoBufferClass(o), methodName, params));
    }

    public static boolean isByteArrayField(Field field) {
        return field.getType().isAssignableFrom(byte[].class);
    }

    public static boolean fieldShouldBeRecursed(Field field) {
        return classShouldBeRecursed(field.getType());
    }

    public static boolean classShouldBeRecursed(Class<?> type) {
        return type.getAnnotation(ProtoBufferData.class) != null;
    }

    private static Class<?> getProtoBufferClass(IReflectiveObject o) {
        return o.getObject().getClass().getAnnotation(ProtoBufferData.class).protoBuffer();
    }
}
