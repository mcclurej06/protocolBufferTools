package org.yogurt.protobufftools;

import org.junit.Test;
import org.yogurt.testClasses.Encoder;
import org.yogurt.testClasses.Hair;
import org.yogurt.testClasses.Person;

import java.util.Random;

import static org.junit.Assert.assertEquals;

public class EncoderTest {

    @Test
    public void testEncodeAndDecode() throws Exception {
        Person person = new Person();

        person.setId(new Random().nextInt());
        person.setTheEmail("fake@theEmail.com");
        person.setName("James");

        byte[] someBytes = "someBytes".getBytes();
        person.setSomeByteArray(someBytes);

        Hair hair = new Hair();
        hair.setColor("blonde");
        hair.setLength(5);
        person.setHair(hair);

        Encoder encoder = new Encoder();

        byte[] bytes = encoder.encode(person);
        Object actual = encoder.decode(bytes);

        assertEquals(person, actual);
    }

}
