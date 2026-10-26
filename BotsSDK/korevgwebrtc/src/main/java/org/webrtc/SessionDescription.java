/*
 * Decompiled with CFR 0.152.
 */
package org.webrtc;

import java.util.Locale;
import org.webrtc.CalledByNative;

public class SessionDescription {
    public final Type type;
    public final String description;

    @CalledByNative
    public SessionDescription(Type type, String description) {
        this.type = type;
        this.description = description;
    }

    @CalledByNative
    String getDescription() {
        return this.description;
    }

    @CalledByNative
    String getTypeInCanonicalForm() {
        return this.type.canonicalForm();
    }

    public static enum Type {
        OFFER,
        PRANSWER,
        ANSWER,
        ROLLBACK;


        public String canonicalForm() {
            return this.name().toLowerCase(Locale.US);
        }

        @CalledByNative(value="Type")
        public static Type fromCanonicalForm(String canonical) {
            return Type.valueOf(Type.class, canonical.toUpperCase(Locale.US));
        }
    }
}

