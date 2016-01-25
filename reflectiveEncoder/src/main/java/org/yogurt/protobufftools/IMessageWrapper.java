package org.yogurt.protobufftools;

import com.google.protobuf.InvalidProtocolBufferException;

public interface IMessageWrapper {

    byte[] wrap(String messageType, byte[] payload);

    Message unwrap(byte[] message) throws InvalidProtocolBufferException;

}
