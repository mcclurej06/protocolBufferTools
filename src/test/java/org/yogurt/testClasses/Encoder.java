package org.yogurt.testClasses;

import com.google.protobuf.InvalidProtocolBufferException;
import org.yogurt.protobufftools.IMessageEncoder;
import org.yogurt.protobufftools.MessageWrapper;

public class Encoder implements IMessageEncoder {

    @Override
    public byte[] encode(Object o) {
        PersonProtos.Person.Builder personBuilder = PersonProtos.Person.newBuilder();

        Person person = (Person) o;

        personBuilder.setId(person.getId());
        personBuilder.setName(person.getName());
        personBuilder.setEmail(person.getTheEmail());

        PersonProtos.Hair.Builder hairBuilder = PersonProtos.Hair.newBuilder();
        hairBuilder.setColor(person.getHair().getColor());
        hairBuilder.setLength(person.getHair().getLength());

        personBuilder.setHair(hairBuilder);

        return new MessageWrapper().wrap(o.getClass().getCanonicalName(), personBuilder.build().toByteArray());
    }

    @Override
    public Object decode(byte[] bytes) throws InvalidProtocolBufferException {
        PersonProtos.Person parsedPerson = PersonProtos.Person.parseFrom(new MessageWrapper().unwrap(bytes).getPayload());

        Person person = new Person();
        person.setId(parsedPerson.getId());
        person.setName(parsedPerson.getName());
        person.setTheEmail(parsedPerson.getEmail());

        Hair hair = new Hair();
        hair.setLength(parsedPerson.getHair().getLength());
        hair.setColor(parsedPerson.getHair().getColor());

        person.setHair(hair);

        return person;
    }

}
