package org.yogurt.reflection;

public class ReflectiveObjectFactory implements IReflectiveObjectFactory{

    @Override
    public IReflectiveObject create(Class c) throws Exception {
        return new ReflectiveObject(c);
    }

    @Override
    public IReflectiveObject create(Object o) throws Exception {
        return new ReflectiveObject(o);
    }
}

