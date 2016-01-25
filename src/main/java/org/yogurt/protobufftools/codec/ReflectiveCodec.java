package org.yogurt.protobufftools.codec;

import org.yogurt.protobufftools.IMessageEncoder;

public class ReflectiveCodec implements IMessageEncoder {
    public byte[] encode(Object o) throws Exception {
        return new ReflectiveEncoder().encode(o);
    }

    public Object decode(byte[] bytes) throws Exception {
        return new ReflevtiveDecoder().decode(bytes);
    }
}
