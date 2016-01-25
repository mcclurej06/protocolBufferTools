package org.yogurt.protobufftools.codec;

import com.google.protobuf.ByteString;
import org.apache.commons.lang3.StringUtils;
import org.yogurt.protobufftools.MessageWrapper;
import org.yogurt.protobufftools.ProtoBufferField;
import org.yogurt.protobufftools.ProtoBufferList;
import org.yogurt.reflection.IReflectiveObject;
import org.yogurt.reflection.IReflectiveObjectFactory;
import org.yogurt.reflection.ReflectiveObject;
import org.yogurt.reflection.ReflectiveObjectFactoryProvider;

import java.lang.reflect.Field;
import java.util.List;

public class ReflectiveEncoder {
    IReflectiveObjectFactory factory = ReflectiveObjectFactoryProvider.provide();

    public byte[] encode(Object o) throws Exception {
        Object builder = populateBuilder(factory.create(o));
        Object built = builder.getClass().getDeclaredMethod("build").invoke(builder);
        byte[] payload = (byte[]) built.getClass().getMethod("toByteArray").invoke(built);
        return new MessageWrapper().wrap(o.getClass().getCanonicalName(), payload);
    }

    private Object populateBuilder(IReflectiveObject o) throws Exception {
        IReflectiveObject builder = ReflectiveCodecHelper.createBuffer(o, "newBuilder");

        for (Field field : o.getFieldsAnnotatedWith(ProtoBufferField.class)) {
            String fieldName = field.getAnnotation(ProtoBufferField.class).fieldName();
            IReflectiveObject invoked = o.smartGet(field.getName());
            if (ReflectiveCodecHelper.fieldShouldBeRecursed(field)) {
                builder.smartSet(fieldName, populateBuilder(invoked));
            } else if (ReflectiveCodecHelper.isByteArrayField(field)) {
                builder.smartSet(fieldName, ByteString.copyFrom((byte[]) invoked.getObject()));
            } else {
                builder.smartSet(fieldName, invoked);
            }
        }

        for (Field field : o.getFieldsAnnotatedWith(ProtoBufferList.class)) {
            String fieldName = field.getAnnotation(ProtoBufferList.class).fieldName();
            IReflectiveObject invoked = o.smartGet(field.getName());
            if (ReflectiveCodecHelper.classShouldBeRecursed((Class<?>) ReflectiveCodecHelper.getListType(field))) {
                for (Object o2 : (List) invoked.getObject()) {
                    builder.call("add" + StringUtils.capitalize(fieldName), populateBuilder(factory.create(o2)));
                }
            } else {
                builder.call("addAll" + StringUtils.capitalize(fieldName), invoked.getObject());
            }
        }

        return builder.getObject();
    }
}
