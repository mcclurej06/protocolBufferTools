package org.yogurt.protobufftools;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created with IntelliJ IDEA.
 * User: james
 * Date: 11/8/13
 * Time: 11:25 PM
 * To change this template use File | Settings | File Templates.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ProtoBufferField {
    String fieldName();
}
