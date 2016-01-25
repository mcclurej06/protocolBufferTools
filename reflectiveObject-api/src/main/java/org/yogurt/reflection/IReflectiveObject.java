package org.yogurt.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Set;

public interface IReflectiveObject {
    Object getObject();

    IReflectiveObject smartGet(String fieldName) throws Exception;

    void smartSet(String fieldName, IReflectiveObject value) throws Exception;

    void smartSet(String fieldName, Object value) throws Exception;

    Set<Field> getFieldsAnnotatedWith(Type protoBufferFieldClass);

    void call(String methodName, Object... objects) throws Exception;
}
