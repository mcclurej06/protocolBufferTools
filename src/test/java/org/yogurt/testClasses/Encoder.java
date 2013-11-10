package org.yogurt.testClasses;

import com.google.protobuf.InvalidProtocolBufferException;
import org.yogurt.protobufftools.IMessageEncoder;
import org.yogurt.protobufftools.MessageWrapper;

public class Encoder implements IMessageEncoder {

    @Override
    public byte[] encode(Object o)  {
        PersonProtos.Person.Builder builder = PersonProtos.Person.newBuilder();

        builder.setName(((Person) o).getName());
        builder.setEmail(((Person) o).getTheEmail());

        return new MessageWrapper().wrap(o.getClass().getCanonicalName(), builder.build().toByteArray());
    }

    @Override
    public Object decode(byte[] bytes) throws InvalidProtocolBufferException {
        PersonProtos.Person parsedPerson = PersonProtos.Person.parseFrom(new MessageWrapper().unwrap(bytes).getPayload());

        Person person = new Person();
        person.setName(parsedPerson.getName());
        person.setTheEmail(parsedPerson.getEmail());

        return person;
    }

}
