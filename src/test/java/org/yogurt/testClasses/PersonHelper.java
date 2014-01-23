package org.yogurt.testClasses;

import java.util.Arrays;
import java.util.Random;

/**
 * Created by james on 1/23/14.
 */
public class PersonHelper {

    public Person createPerson(){
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

        person.setLanguages(Arrays.asList("english", "Spanish"));
        person.setSomeNumbers(Arrays.asList(1, 2));


        return person;
    }
}
