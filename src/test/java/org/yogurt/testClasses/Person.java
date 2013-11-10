package org.yogurt.testClasses;

import org.yogurt.protobufftools.ProtoBufferData;
import org.yogurt.protobufftools.ProtoBufferField;

/**
 * Created with IntelliJ IDEA.
 * User: james
 * Date: 11/8/13
 * Time: 11:14 PM
 * To change this template use File | Settings | File Templates.
 */
@ProtoBufferData(protoBufferName = "PersonProtos", protoBufferMessage = "Person")
public class Person {
    @ProtoBufferField(fieldName = "name")
    private String name;
    @ProtoBufferField(fieldName = "email")
    private String theEmail;

    String dontSentMe;

    public String getDontSentMe() {
        return dontSentMe;
    }

    public void setDontSentMe(String dontSentMe) {
        this.dontSentMe = dontSentMe;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Person person = (Person) o;

        if (name != null ? !name.equals(person.name) : person.name != null) return false;
        if (theEmail != null ? !theEmail.equals(person.theEmail) : person.theEmail != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (theEmail != null ? theEmail.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", theEmail='" + theEmail + '\'' +
                ", dontSentMe='" + dontSentMe + '\'' +
                '}';
    }
}
