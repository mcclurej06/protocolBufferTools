package my.pack;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ReflectiveEncoderTest {

    @Test
    public void testEncodeAndDecode() throws Exception {
        Person person = new Person();

        person.setTheEmail("fake@theEmail.com");
        person.setName("James");

        ReflectiveEncoder encoder = new ReflectiveEncoder();

        byte[] bytes = encoder.encode(person);
        Object actual = encoder.decode(bytes);

        assertEquals(person, actual);
    }

}
