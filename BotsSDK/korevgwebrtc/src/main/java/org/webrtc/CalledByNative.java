/*
 * Decompiled with CFR 0.152.
 */
package org.webrtc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value={ElementType.CONSTRUCTOR, ElementType.METHOD})
@Retention(value=RetentionPolicy.CLASS)
public @interface CalledByNative {
    public String value() default "";
}

