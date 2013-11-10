package org.yogurt.protobufftools;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ProtoBufferField {
    String fieldName();
}
