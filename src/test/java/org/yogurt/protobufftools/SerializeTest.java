package org.yogurt.protobufftools;

import org.junit.Test;
import org.yogurt.testClasses.Person;
import org.yogurt.testClasses.PersonHelper;

import java.io.*;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

public class SerializeTest {
    @Test
    public void testEncodeAndDecode() throws Exception {
        Person person = new PersonHelper().createPerson();

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out;
        out = new ObjectOutputStream(bos);
        long encodeStart = System.nanoTime();
        out.writeObject(person);
        byte[] bytes = bos.toByteArray();
        long encodeEnd = System.nanoTime();
        System.out.println("serialized encoded byte[] size: "+bytes.length);

        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        ObjectInput in;
        long decodeStart = System.nanoTime();
        in = new ObjectInputStream(bis);
        Object actual = in.readObject();
        long decodeEnd= System.nanoTime();

        long elapsedEncode = encodeEnd - encodeStart;
        long elapsedDecode = decodeEnd - decodeStart;
        System.out.println("serialized encode time (ns): "+ elapsedEncode);
        System.out.println("serialized encode time (ms): "+ TimeUnit.NANOSECONDS.toMillis(elapsedEncode));
        System.out.println("serialized decode time (ns): "+ elapsedDecode);
        System.out.println("serialized decode time (ms): "+ TimeUnit.NANOSECONDS.toMillis(elapsedDecode));

        assertEquals(person, actual);
    }
}
