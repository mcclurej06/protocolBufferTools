package org.yogurt.protobufftools;

import org.junit.Test;
import org.yogurt.testClasses.Hair;
import org.yogurt.testClasses.Person;
import org.yogurt.testClasses.PersonHelper;

import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ReflectiveEncoderTest {

    @Test
    public void testEncodeAndDecode() throws Exception {
        Person person = new PersonHelper().createPerson();

        ReflectiveEncoder encoder = new ReflectiveEncoder();

        byte[] bytes = encoder.encode(person);
        System.out.println("reflective encoded byte[] size: "+bytes.length);
        Object actual = encoder.decode(bytes);

        assertEquals(person, actual);

        assertNull(((Person) actual).getDontSendMe());
    }

}
