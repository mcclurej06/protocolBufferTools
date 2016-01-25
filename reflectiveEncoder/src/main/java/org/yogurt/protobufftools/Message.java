package org.yogurt.protobufftools;

import java.util.Arrays;

public class Message {
    private String messageType;
    private byte[] payload;

    public Message(String messageType, byte[] payload) {
        this.messageType = messageType;
        this.payload = payload;
    }

    public String getMessageType() {
        return messageType;
    }

    public byte[] getPayload() {
        return payload;
    }

    @Override
    public String toString() {
        return "Message{" +
                "messageType='" + messageType + '\'' +
                ", payload=" + Arrays.toString(payload) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Message message = (Message) o;

        if (messageType != null ? !messageType.equals(message.messageType) : message.messageType != null) return false;
        if (!Arrays.equals(payload, message.payload)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = messageType != null ? messageType.hashCode() : 0;
        result = 31 * result + (payload != null ? Arrays.hashCode(payload) : 0);
        return result;
    }
}
