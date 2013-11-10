package my.pack;

import com.google.protobuf.*;
import jdk.nashorn.internal.objects.NativeArray;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

import static com.google.protobuf.Descriptors.FieldDescriptor;

public class Encoder {

    public byte[] encode(Object o) throws Exception {

        return reflectionEncodeMethod(o);
    }

    private byte[] normalEncodeMethod(Object o) throws Descriptors.DescriptorValidationException {
        PersonProtos.Person.Builder builder = PersonProtos.Person.newBuilder();

        builder.setName(((Person)o).getName());
        builder.setEmail(((Person) o).getTheEmail());



        UnknownFieldSet.Builder unknownFieldSetBuilder = UnknownFieldSet.newBuilder();
        UnknownFieldSet.Field.Builder fieldBuilder = UnknownFieldSet.Field.newBuilder();
        fieldBuilder.addLengthDelimited(ByteString.copyFromUtf8("something"));
        Map<FieldDescriptor, Object> fields = builder.getAllFields();
        int newIndex = 1;
        for (FieldDescriptor f : fields.keySet()){
            if (f.getIndex() >= newIndex){
                newIndex = f.getIndex() +2;
            }
        }

        unknownFieldSetBuilder.addField(newIndex, fieldBuilder.build());
        builder.setUnknownFields(unknownFieldSetBuilder.build());

        return wrapMessage(o.getClass().getCanonicalName(), builder.build().toByteArray());
    }

    private byte[] wrapMessage(String clazz, byte[] payload){
        MessageWrapperProtos.MessageWrapper.Builder builder = MessageWrapperProtos.MessageWrapper.newBuilder();

        ByteString bytes = ByteString.copyFrom(payload);

        builder.setPayload(bytes);
        builder.setPackagedClass(clazz);

        return builder.build().toByteArray();
    }

    private Message unwrapMessage(byte[] message) throws InvalidProtocolBufferException {
        MessageWrapperProtos.MessageWrapper messageWrapper = MessageWrapperProtos.MessageWrapper.parseFrom(message);
        return new Message(messageWrapper.getPackagedClass(), messageWrapper.getPayload().toByteArray());
    }

    private class Message{
        Message(String clazz, byte[] payload){
            this.clazz = clazz;
            this.payload = payload;
        }

        public String clazz;
        public byte[] payload;
    }

    private byte[] reflectionEncodeMethod(Object o) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, ClassNotFoundException {
        String buffer = ((ProtoBufferData) o.getClass().getAnnotation(ProtoBufferData.class)).protoBufferName();
        String message = ((ProtoBufferData) o.getClass().getAnnotation(ProtoBufferData.class)).protoBufferMessage();
        String pack = o.getClass().getPackage().getName();

        Object builder = Class.forName(pack + "." + buffer + "$" + message).getMethod("newBuilder").invoke(null);

        for (Field f : o.getClass().getDeclaredFields()) {
            ProtoBufferField annotation = (ProtoBufferField) f.getAnnotation(ProtoBufferField.class);
            if (annotation == null) {
                continue;
            }

            Method setter = builder.getClass().getDeclaredMethod("set" + capitalize(annotation.fieldName()), f.getType());
            Method getter = o.getClass().getMethod("get" + capitalize(f.getName()));
            setter.invoke(builder, getter.invoke(o));
        }

        Object built = builder.getClass().getDeclaredMethod("build").invoke(builder);
        byte[] payload = (byte[]) built.getClass().getMethod("toByteArray").invoke(built);

        return wrapMessage(o.getClass().getCanonicalName(), payload);
    }

    private String capitalize(String line) {
        return Character.toUpperCase(line.charAt(0)) + line.substring(1);
    }

    public Object decode(byte[] bytes) throws Exception {
        return reflectionDecodeMethod(bytes);
    }

    private Object normalDecodeMethod(byte[] bytes) throws InvalidProtocolBufferException {
        PersonProtos.Person parsedPerson = PersonProtos.Person.parseFrom(unwrapMessage(bytes).payload);

        Person person = new Person();
        person.setName(parsedPerson.getName());
        person.setTheEmail(parsedPerson.getEmail());

        return person;
    }
    private Object reflectionDecodeMethod(byte[] bytes) throws InvalidProtocolBufferException, ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        Message message = unwrapMessage(bytes);

        Object o = Class.forName(message.clazz).newInstance();

        String buffer = ((ProtoBufferData) o.getClass().getAnnotation(ProtoBufferData.class)).protoBufferName();
        String messageType = ((ProtoBufferData) o.getClass().getAnnotation(ProtoBufferData.class)).protoBufferMessage();
        String pack = o.getClass().getPackage().getName();

        Object personProto = Class.forName(pack + "." + buffer + "$" + messageType).getMethod("parseFrom", byte[].class).invoke(null, message.payload);


        Field[] declaredFields = o.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            ProtoBufferField annotation = (ProtoBufferField) field.getAnnotation(ProtoBufferField.class);
            if (annotation == null) {
                continue;
            }

            Method getter = personProto.getClass().getDeclaredMethod("get" + capitalize(annotation.fieldName()));
            Object data = getter.invoke(personProto);
            Method setter = o.getClass().getMethod("set" + capitalize(field.getName()), field.getType());
            setter.invoke(o, data);
        }


        return o;
    }
}
