package org.yogurt.testClasses.codec;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import org.yogurt.protobufftools.IMessageEncoder;
import org.yogurt.protobufftools.MessageWrapper;
import org.yogurt.testClasses.Car;
import org.yogurt.testClasses.Hair;
import org.yogurt.testClasses.Person;
import org.yogurt.testClasses.PersonProtos;

import java.util.ArrayList;
import java.util.List;

public class Encoder implements IMessageEncoder {

    @Override
    public byte[] encode(Object o) {
        PersonProtos.Person.Builder personBuilder = PersonProtos.Person.newBuilder();

        Person person = (Person) o;

        personBuilder.setId(person.getId());
        personBuilder.setName(person.getName());
        personBuilder.setEmail(person.getTheEmail());
        personBuilder.setSomeBytes(ByteString.copyFrom(person.getSomeByteArray()));

        PersonProtos.Hair.Builder hairBuilder = PersonProtos.Hair.newBuilder();
        hairBuilder.setColor(person.getHair().getColor());
        hairBuilder.setLength(person.getHair().getLength());

        personBuilder.setHair(hairBuilder);

        personBuilder.addAllLanguage(person.getLanguages());
        personBuilder.addAllSomeNumber(person.getSomeNumbers());


        for(Car car : person.getCars()){
            PersonProtos.Car.Builder carBuilder = PersonProtos.Car.newBuilder();
            carBuilder.setColor(car.getColor());
            carBuilder.setManufacturer(car.getManufacturer());
            personBuilder.addCar(carBuilder);
        }

        return new MessageWrapper().wrap(o.getClass().getCanonicalName(), personBuilder.build().toByteArray());
    }

    @Override
    public Object decode(byte[] bytes) throws InvalidProtocolBufferException {
        PersonProtos.Person parsedPerson = PersonProtos.Person.parseFrom(new MessageWrapper().unwrap(bytes).getPayload());

        Person person = new Person();
        person.setId(parsedPerson.getId());
        person.setName(parsedPerson.getName());
        person.setTheEmail(parsedPerson.getEmail());
        person.setSomeByteArray(parsedPerson.getSomeBytes().toByteArray());

        Hair hair = new Hair();
        hair.setLength(parsedPerson.getHair().getLength());
        hair.setColor(parsedPerson.getHair().getColor());

        person.setHair(hair);

        person.setLanguages(parsedPerson.getLanguageList());
        person.setSomeNumbers(parsedPerson.getSomeNumberList());

        List<PersonProtos.Car> carList = parsedPerson.getCarList();
        ArrayList<Car> cars = new ArrayList<>();
        for (PersonProtos.Car car : carList){
            Car newCar = new Car();

            newCar.setColor(car.getColor());
            newCar.setManufacturer(car.getManufacturer());

            cars.add(newCar);
        }
        person.setCars(cars);

        return person;
    }

}
