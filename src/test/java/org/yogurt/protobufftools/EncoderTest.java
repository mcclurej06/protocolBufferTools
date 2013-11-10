package org.yogurt.protobufftools;

import org.junit.Test;
import org.yogurt.testClasses.Encoder;
import org.yogurt.testClasses.Person;

import static org.junit.Assert.assertEquals;

public class EncoderTest {

    @Test
    public void testEncodeAndDecode() throws Exception {
        Person person = new Person();

        person.setTheEmail("fake@theEmail.com");
        person.setName("James");

        Encoder encoder = new Encoder();

        byte[] bytes = encoder.encode(person);
        Object actual = encoder.decode(bytes);

        assertEquals(person, actual);
    }

}
