package org.yogurt.protobufftools;

import org.junit.Test;
import org.yogurt.testClasses.Encoder;
import org.yogurt.testClasses.Hair;
import org.yogurt.testClasses.Person;
import org.yogurt.testClasses.PersonHelper;

import java.util.Random;

import static org.junit.Assert.assertEquals;

public class EncoderTest {

    @Test
    public void testEncodeAndDecode() throws Exception {
        Person person = new PersonHelper().createPerson();

        Encoder encoder = new Encoder();

        byte[] bytes = encoder.encode(person);
        System.out.println("standard encoded byte[] size: "+bytes.length);
        Object actual = encoder.decode(bytes);

        assertEquals(person, actual);
    }

}
