package org.yogurt.reflection;

public class ReflectiveObjectFactoryProvider {
    private static IReflectiveObjectFactory reflectiveObjectFactory;

    public static IReflectiveObjectFactory provide(){
        if(reflectiveObjectFactory == null){
            throw new IllegalStateException("no factory implementation was registered");
        }
        return reflectiveObjectFactory;
    }

    public static synchronized void register(IReflectiveObjectFactory factory){
        if(reflectiveObjectFactory != null){
            throw new IllegalArgumentException("factory implementation is already set");
        }
        reflectiveObjectFactory = factory;
    }
}
