package org.yogurt.reflection;

public interface IReflectiveObjectFactory {
    IReflectiveObject create(Class c) throws Exception;

    IReflectiveObject create(Object o) throws Exception;
}
