package org.yogurt.protobufftools;

public interface IMessageEncoder {
    byte[] encode(Object o) throws Exception;

    Object decode(byte[] bytes) throws Exception;
}
