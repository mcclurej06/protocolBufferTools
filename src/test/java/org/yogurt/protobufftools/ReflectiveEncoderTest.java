package org.yogurt.protobufftools;

import org.junit.Test;
import org.yogurt.testClasses.Hair;
import org.yogurt.testClasses.Person;

import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ReflectiveEncoderTest {

    @Test
    public void testEncodeAndDecode() throws Exception {
        Person person = new Person();

        person.setId(new Random().nextInt());
        person.setTheEmail("fake@theEmail.com");
        person.setName("James");
        person.setDontSendMe("dont send me!");

        Hair hair = new Hair();
        hair.setColor("blonde");
        hair.setLength(5);
        person.setHair(hair);

        ReflectiveEncoder encoder = new ReflectiveEncoder();

        byte[] bytes = encoder.encode(person);
        Object actual = encoder.decode(bytes);

        assertEquals(person, actual);

        assertNull(((Person) actual).getDontSendMe());
    }

}
