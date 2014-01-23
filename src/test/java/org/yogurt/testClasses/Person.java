package org.yogurt.testClasses;

import org.yogurt.protobufftools.ProtoBufferData;
import org.yogurt.protobufftools.ProtoBufferField;
import org.yogurt.protobufftools.ProtoBufferList;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

@ProtoBufferData(protoBuffer = PersonProtos.Person.class)
public class Person implements Serializable{
    @ProtoBufferField(fieldName = "id")
    private int id;
    @ProtoBufferField(fieldName = "name")
    private String name;
    @ProtoBufferField(fieldName = "email")
    private String theEmail;
    @ProtoBufferField(fieldName = "money")
    private int money;
    @ProtoBufferField(fieldName = "hair")
    private Hair hair;
    @ProtoBufferField(fieldName = "someBytes")
    private byte[] someByteArray;
    @ProtoBufferList(fieldName = "language")
    private List<String> languages;
    @ProtoBufferList(fieldName = "someNumber")
    private List<Integer> someNumbers;

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

    public int wealth() {
        return money;
    }

    public void addMoney(int money) {
        this.money += money;
    }

    public byte[] getSomeByteArray() {
        return someByteArray;
    }

    public void setSomeByteArray(byte[] someByteArray) {
        this.someByteArray = someByteArray;
    }

    public List<String> getLanguages() {
        return languages;
    }

    public void setLanguages(List<String> languages) {
        this.languages = languages;
    }

    public List<Integer> getSomeNumbers() {
        return someNumbers;
    }

    public void setSomeNumbers(List<Integer> someNumbers) {
        this.someNumbers = someNumbers;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", theEmail='" + theEmail + '\'' +
                ", money=" + money +
                ", hair=" + hair +
                ", someByteArray=" + Arrays.toString(someByteArray) +
                ", languages=" + languages +
                ", someNumbers=" + someNumbers +
                ", dontSendMe='" + dontSendMe + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Person person = (Person) o;

        if (id != person.id) return false;
        if (money != person.money) return false;
        if (hair != null ? !hair.equals(person.hair) : person.hair != null) return false;
        if (languages != null ? !languages.equals(person.languages) : person.languages != null) return false;
        if (name != null ? !name.equals(person.name) : person.name != null) return false;
        if (!Arrays.equals(someByteArray, person.someByteArray)) return false;
        if (someNumbers != null ? !someNumbers.equals(person.someNumbers) : person.someNumbers != null) return false;
        if (theEmail != null ? !theEmail.equals(person.theEmail) : person.theEmail != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (theEmail != null ? theEmail.hashCode() : 0);
        result = 31 * result + money;
        result = 31 * result + (hair != null ? hair.hashCode() : 0);
        result = 31 * result + (someByteArray != null ? Arrays.hashCode(someByteArray) : 0);
        result = 31 * result + (languages != null ? languages.hashCode() : 0);
        result = 31 * result + (someNumbers != null ? someNumbers.hashCode() : 0);
        return result;
    }
}
