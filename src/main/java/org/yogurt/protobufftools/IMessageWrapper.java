package org.yogurt.protobufftools;

import com.google.protobuf.InvalidProtocolBufferException;

public interface IMessageWrapper {

    public byte[] wrap(String messageType, byte[] payload);
    public Message unwrap(byte [] message) throws InvalidProtocolBufferException;

}
