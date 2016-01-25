package org.yogurt.reflection;

public class GroovyReflectiveObjectFactory implements IReflectiveObjectFactory{

    @Override
    public IReflectiveObject create(Class c) throws Exception {
        return new GroovyReflectiveObject(c);
    }

    @Override
    public IReflectiveObject create(Object o) throws Exception {
        return new GroovyReflectiveObject(o);
    }
}

