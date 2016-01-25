package org.yogurt.protobufftools;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;

public class MessageWrapper implements IMessageWrapper {

    @Override
    public byte[] wrap(String messageType, byte[] payload) {

        MessageWrapperProtos.MessageWrapper.Builder builder = MessageWrapperProtos.MessageWrapper.newBuilder();
        builder.setMessageType(messageType).setPayload(ByteString.copyFrom(payload));

        return builder.build().toByteArray();
    }

    @Override
    public Message unwrap(byte[] message) throws InvalidProtocolBufferException {
        MessageWrapperProtos.MessageWrapper messageWrapper = MessageWrapperProtos.MessageWrapper.parseFrom(message);
        return new Message(messageWrapper.getMessageType(), messageWrapper.getPayload().toByteArray());
    }
}
