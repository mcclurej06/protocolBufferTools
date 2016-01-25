package org.yogurt.protobufftools.codec;

import com.google.protobuf.ByteString;
import org.apache.commons.lang3.StringUtils;
import org.yogurt.protobufftools.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ReflevtiveDecoder {
    public Object decode(byte[] bytes) throws Exception {
        Message message = new MessageWrapper().unwrap(bytes);
        ReflectiveObject o = new ReflectiveObject(Class.forName(message.getMessageType()).newInstance());

        ReflectiveObject buffer = ReflectiveCodecHelper.createBuffer(o, "parseFrom", message.getPayload());
        return extractFromBuffer(o, buffer);
    }

    private Object extractFromBuffer(ReflectiveObject o, ReflectiveObject buffer) throws Exception {
        for (Field field : o.getFieldsAnnotatedWith(ProtoBufferField.class)) {
            handleField(o, buffer, field);
        }

        for (Field field : o.getFieldsAnnotatedWith(ProtoBufferList.class)) {
            handleList(o, buffer, field);
        }

        return o.getObject();
    }

    private void handleList(ReflectiveObject o, ReflectiveObject buffer, Field field) throws Exception {
        if (ReflectiveCodecHelper.classShouldBeRecursed((Class<?>) ReflectiveCodecHelper.getListType(field))) {
            List objects = (List) buffer.smartGet(StringUtils.capitalize(field.getAnnotation(ProtoBufferList.class).fieldName()) + "List").getObject();
            List<Object> newObjects = new ArrayList<>();
            for (Object o1 : objects) {
                newObjects.add(extractFromBuffer(new ReflectiveObject(Class.forName(((Class<?>) ReflectiveCodecHelper.getListType(field)).getCanonicalName()).newInstance()), new ReflectiveObject(o1)));
            }
            o.smartSet(field.getName(), newObjects);
        } else {
            o.smartSet(field.getName(), buffer.smartGet(StringUtils.capitalize(field.getAnnotation(ProtoBufferList.class).fieldName()) + "List"));
        }
    }

    private void handleField(ReflectiveObject o, ReflectiveObject buffer, Field field) throws Exception {
        ReflectiveObject fieldValue = buffer.smartGet(field.getAnnotation(ProtoBufferField.class).fieldName());
        if (ReflectiveCodecHelper.fieldShouldBeRecursed(field)) {
            o.smartSet(field.getName(), extractFromBuffer(new ReflectiveObject(field.getType()), fieldValue));
        } else if (ReflectiveCodecHelper.isByteArrayField(field)) {
            o.smartSet(field.getName(), ((ByteString) fieldValue.getObject()).toByteArray());
        } else {
            o.smartSet(field.getName(), fieldValue);
        }
    }

}
