package org.yogurt.protobufftools.codec;

import com.google.protobuf.ByteString;
import org.apache.commons.lang3.StringUtils;
import org.yogurt.protobufftools.*;
import org.yogurt.reflection.IReflectiveObject;
import org.yogurt.reflection.IReflectiveObjectFactory;
import org.yogurt.reflection.ReflectiveObject;
import org.yogurt.reflection.ReflectiveObjectFactoryProvider;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ReflevtiveDecoder {
    IReflectiveObjectFactory factory = ReflectiveObjectFactoryProvider.provide();

    public Object decode(byte[] bytes) throws Exception {
        Message message = new MessageWrapper().unwrap(bytes);
        IReflectiveObject o = factory.create(Class.forName(message.getMessageType()).newInstance());

        IReflectiveObject buffer = ReflectiveCodecHelper.createBuffer(o, "parseFrom", message.getPayload());
        return extractFromBuffer(o, buffer);
    }

    private Object extractFromBuffer(IReflectiveObject o, IReflectiveObject buffer) throws Exception {
        for (Field field : o.getFieldsAnnotatedWith(ProtoBufferField.class)) {
            handleField(o, buffer, field);
        }

        for (Field field : o.getFieldsAnnotatedWith(ProtoBufferList.class)) {
            handleList(o, buffer, field);
        }

        return o.getObject();
    }

    private void handleList(IReflectiveObject o, IReflectiveObject buffer, Field field) throws Exception {
        if (ReflectiveCodecHelper.classShouldBeRecursed((Class<?>) ReflectiveCodecHelper.getListType(field))) {
            List objects = (List) buffer.smartGet(field.getAnnotation(ProtoBufferList.class).fieldName() + "List").getObject();
            List<Object> newObjects = new ArrayList<>();
            for (Object o1 : objects) {
                newObjects.add(extractFromBuffer(factory.create(Class.forName(((Class<?>) ReflectiveCodecHelper.getListType(field)).getCanonicalName()).newInstance()), factory.create(o1)));
            }
            o.smartSet(field.getName(), newObjects);
        } else {
            o.smartSet(field.getName(), buffer.smartGet(field.getAnnotation(ProtoBufferList.class).fieldName() + "List"));
        }
    }

    private void handleField(IReflectiveObject o, IReflectiveObject buffer, Field field) throws Exception {
        IReflectiveObject fieldValue = buffer.smartGet(field.getAnnotation(ProtoBufferField.class).fieldName());
        if (ReflectiveCodecHelper.fieldShouldBeRecursed(field)) {
            o.smartSet(field.getName(), extractFromBuffer(factory.create(field.getType()), fieldValue));
        } else if (ReflectiveCodecHelper.isByteArrayField(field)) {
            o.smartSet(field.getName(), ((ByteString) fieldValue.getObject()).toByteArray());
        } else {
            o.smartSet(field.getName(), fieldValue);
        }
    }

}
