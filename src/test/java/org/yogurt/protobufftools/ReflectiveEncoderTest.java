package org.yogurt.protobufftools;

import org.junit.Test;
import org.yogurt.testClasses.Person;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

public class ReflectiveEncoderTest {

    @Test
    public void testEncodeAndDecode() throws Exception {
        Person person = new Person();

        person.setTheEmail("fake@theEmail.com");
        person.setName("James");
        person.setDontSentMe("dont send me!");

        ReflectiveEncoder encoder = new ReflectiveEncoder();

        byte[] bytes = encoder.encode(person);
        Object actual = encoder.decode(bytes);

        assertEquals(person, actual);

        assertNull(((Person) actual).getDontSentMe());
    }

}
