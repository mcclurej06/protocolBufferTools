package org.yogurt.testClasses;

import org.yogurt.protobufftools.ProtoBufferData;
import org.yogurt.protobufftools.ProtoBufferField;

@ProtoBufferData(protoBuffer = PersonProtos.Hair.class)
public class Hair {
    @ProtoBufferField(fieldName = "length")
    int length;
    @ProtoBufferField(fieldName = "color")
    String color;

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Hair hair = (Hair) o;

        if (length != hair.length) return false;
        if (color != null ? !color.equals(hair.color) : hair.color != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = length;
        result = 31 * result + (color != null ? color.hashCode() : 0);
        return result;
    }
}
