package org.yogurt.protobufftools.codec;

import org.apache.commons.lang3.reflect.MethodUtils;
import org.yogurt.protobufftools.ProtoBufferData;
import org.yogurt.protobufftools.ReflectiveObject;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class ReflectiveCodecHelper {
    public static Type getListType(Field field) {
        return ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
    }

    public static ReflectiveObject createBuffer(ReflectiveObject o, String methodName, Object... params) throws Exception {
        return new ReflectiveObject(MethodUtils.invokeStaticMethod(getProtoBufferClass(o), methodName, params));
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

    public static Class<?> getProtoBufferClass(ReflectiveObject o) {
        return o.getObject().getClass().getAnnotation(ProtoBufferData.class).protoBuffer();
    }
}
