package org.yogurt.testClasses;

import org.yogurt.protobufftools.ProtoBufferData;
import org.yogurt.protobufftools.ProtoBufferField;

@ProtoBufferData(protoBuffer = PersonProtos.Person.class)
public class Person {
    @ProtoBufferField(fieldName = "id")
    private int id;
    @ProtoBufferField(fieldName = "name")
    private String name;
    @ProtoBufferField(fieldName = "email")
    private String theEmail;
    @ProtoBufferField(fieldName = "hair")
    private Hair hair;

    String dontSendMe;

    public Hair getHair() {
        return hair;
    }

    public void setHair(Hair hair) {
        this.hair = hair;
    }

    public String getDontSendMe() {
        return dontSendMe;
    }

    public void setDontSendMe(String dontSendMe) {
        this.dontSendMe = dontSendMe;
    }

    public String getTheEmail() {
        return theEmail;
    }

    public void setTheEmail(String theEmail) {
        this.theEmail = theEmail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", theEmail='" + theEmail + '\'' +
                ", hair=" + hair +
                ", dontSendMe='" + dontSendMe + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Person person = (Person) o;

        if (id != person.id) return false;
        if (hair != null ? !hair.equals(person.hair) : person.hair != null) return false;
        if (name != null ? !name.equals(person.name) : person.name != null) return false;
        if (theEmail != null ? !theEmail.equals(person.theEmail) : person.theEmail != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (theEmail != null ? theEmail.hashCode() : 0);
        result = 31 * result + (hair != null ? hair.hashCode() : 0);
        return result;
    }
}
