package org.yogurt.reflection

import java.lang.reflect.Field
import java.lang.reflect.Type
import java.util.concurrent.ConcurrentHashMap

public class GroovyReflectiveObject implements IReflectiveObject {
    private static Map<Pair<Class, Type>, Set<Field>> FIELDS = new ConcurrentHashMap()
    Object object

    GroovyReflectiveObject(Class<?> clazz) throws Exception {
        this.object = clazz.newInstance()
    }

    GroovyReflectiveObject(Object object) {
        this.object = object
    }

    @Override
    public IReflectiveObject smartGet(String fieldName) throws Exception {
        new GroovyReflectiveObject(object."$fieldName")
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
        Pair key = new Pair(object.getClass(), annotationType)

        if (!FIELDS.containsKey(key)) {
            FIELDS.put(key, object.getClass().declaredFields.findAll { field ->
                annotationType in field.declaredAnnotations*.annotationType()
            })
        }

        FIELDS.get(key)
    }

    @Override
    public void call(String methodName, Object... objects) throws Exception {
        object."$methodName"(*objects)
    }
}
