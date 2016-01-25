package org.yogurt.protobufftools.codec;

import org.yogurt.protobufftools.IMessageEncoder;
import org.yogurt.reflection.GroovyReflectiveObjectFactory;
import org.yogurt.reflection.ReflectiveObjectFactoryProvider;

public class ReflectiveCodec implements IMessageEncoder {
    static {
        //TODO: find a better way to do this...
//        ReflectiveObjectFactoryProvider.register(new ReflectiveObjectFactory());
        ReflectiveObjectFactoryProvider.register(new GroovyReflectiveObjectFactory());
    }

    public byte[] encode(Object o) throws Exception {
        return new ReflectiveEncoder().encode(o);
    }

    public Object decode(byte[] bytes) throws Exception {
        return new ReflevtiveDecoder().decode(bytes);
    }
}
