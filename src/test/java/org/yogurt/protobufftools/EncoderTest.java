package org.yogurt.protobufftools;

import org.junit.Test;
import org.yogurt.testClasses.Encoder;
import org.yogurt.testClasses.Person;
import org.yogurt.testClasses.PersonHelper;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

public class EncoderTest {

    @Test
    public void testEncodeAndDecode() throws Exception {
        Person person = new PersonHelper().createPerson();

        Encoder encoder = new Encoder();

        long encodeStart = System.nanoTime();
        byte[] bytes = encoder.encode(person);
        long encodeEnd = System.nanoTime();
        System.out.println("standard encoded byte[] size: "+bytes.length);
        long decodeStart = System.nanoTime();
        Object actual = encoder.decode(bytes);
        long decodeEnd = System.nanoTime();
        long elapsedEncode = encodeEnd - encodeStart;
        long elapsedDecode = decodeEnd - decodeStart;
        System.out.println("standard encode time (ns): "+ elapsedEncode);
        System.out.println("standard encode time (ms): " + TimeUnit.NANOSECONDS.toMillis(elapsedEncode));
        System.out.println("standard decode time (ns): "+ elapsedDecode);
        System.out.println("standard decode time (ms): "+TimeUnit.NANOSECONDS.toMillis(elapsedDecode));

        assertEquals(person, actual);
    }

}
