package org.yogurt.protobufftools;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by james on 1/23/14.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ProtoBufferList {
    String fieldName();
}
