package my.pack;

import org.yogurt.protobufftools.IMessageEncoder;
import org.yogurt.protobufftools.Message;
import org.yogurt.protobufftools.MessageWrapper;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ReflectiveEncoder implements IMessageEncoder{
    public byte[] encode(Object o) throws Exception {
        String buffer = o.getClass().getAnnotation(ProtoBufferData.class).protoBufferName();
        String message = o.getClass().getAnnotation(ProtoBufferData.class).protoBufferMessage();
        String pack = o.getClass().getPackage().getName();

        Object builder = Class.forName(pack + "." + buffer + "$" + message).getMethod("newBuilder").invoke(null);

        for (Field f : o.getClass().getDeclaredFields()) {
            ProtoBufferField annotation = f.getAnnotation(ProtoBufferField.class);
            if (annotation == null) {
                continue;
            }

            Method setter = builder.getClass().getDeclaredMethod("set" + capitalize(annotation.fieldName()), f.getType());
            Method getter = o.getClass().getMethod("get" + capitalize(f.getName()));
            setter.invoke(builder, getter.invoke(o));
        }

        Object built = builder.getClass().getDeclaredMethod("build").invoke(builder);
        byte[] payload = (byte[]) built.getClass().getMethod("toByteArray").invoke(built);

        return new MessageWrapper().wrap(o.getClass().getCanonicalName(), payload);
    }

    public Object decode(byte[] bytes) throws Exception {
        Message message = new MessageWrapper().unwrap(bytes);

        Object o = Class.forName(message.getMessageType()).newInstance();

        String buffer = o.getClass().getAnnotation(ProtoBufferData.class).protoBufferName();
        String messageType = o.getClass().getAnnotation(ProtoBufferData.class).protoBufferMessage();
        String pack = o.getClass().getPackage().getName();

        Object personProto = Class.forName(pack + "." + buffer + "$" + messageType).getMethod("parseFrom", byte[].class).invoke(null, message.getPayload());


        for (Field field : o.getClass().getDeclaredFields()) {
            ProtoBufferField annotation = field.getAnnotation(ProtoBufferField.class);
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

    private String capitalize(String line) {
        return Character.toUpperCase(line.charAt(0)) + line.substring(1);
    }

}
