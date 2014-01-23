package org.yogurt.testClasses;

import org.yogurt.protobufftools.ProtoBufferData;
import org.yogurt.protobufftools.ProtoBufferField;

import java.io.Serializable;

/**
 * Created by james on 1/23/14.
 */
@ProtoBufferData(protoBuffer = PersonProtos.Car.class)
public class Car implements Serializable{
    @ProtoBufferField(fieldName = "color")
    private String color;
    @ProtoBufferField(fieldName = "manufacturer")
    private String manufacturer;

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    @Override
    public String toString() {
        return "Car{" +
                "color='" + color + '\'' +
                ", manufacturer='" + manufacturer + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Car car = (Car) o;

        if (color != null ? !color.equals(car.color) : car.color != null) return false;
        if (manufacturer != null ? !manufacturer.equals(car.manufacturer) : car.manufacturer != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = color != null ? color.hashCode() : 0;
        result = 31 * result + (manufacturer != null ? manufacturer.hashCode() : 0);
        return result;
    }
}
