package org.yogurt.testClasses;

import java.util.Arrays;
import java.util.Random;

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

        Car car1 = new Car();
        car1.setColor("red");
        car1.setManufacturer("ford");
        Car car2 = new Car();
        car2.setColor("black");
        car2.setManufacturer("bmw");
        person.setCars(Arrays.asList(car1, car2));

        return person;
    }
}
