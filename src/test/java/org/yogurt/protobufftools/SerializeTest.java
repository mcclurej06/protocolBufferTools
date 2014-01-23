package org.yogurt.protobufftools;

import org.junit.Test;
import org.yogurt.testClasses.Encoder;
import org.yogurt.testClasses.Person;
import org.yogurt.testClasses.PersonHelper;

import java.io.*;

import static org.junit.Assert.assertEquals;

/**
 * Created by james on 1/23/14.
 */
public class SerializeTest {
    @Test
    public void testEncodeAndDecode() throws Exception {
        Person person = new PersonHelper().createPerson();

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out;
        out = new ObjectOutputStream(bos);
        out.writeObject(person);
        byte[] bytes = bos.toByteArray();

        System.out.println("serialized encoded byte[] size: "+bytes.length);

        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        ObjectInput in;
        in = new ObjectInputStream(bis);
        Object actual = in.readObject();


        assertEquals(person, actual);
    }
}
