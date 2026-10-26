/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  androidx.annotation.Nullable
 */
package org.webrtc;

import androidx.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import org.webrtc.CalledByNative;

public class MediaConstraints {
    public final List<KeyValuePair> mandatory = new ArrayList<KeyValuePair>();
    public final List<KeyValuePair> optional = new ArrayList<KeyValuePair>();

    private static String stringifyKeyValuePairList(List<KeyValuePair> list) {
        StringBuilder builder = new StringBuilder("[");
        for (KeyValuePair pair : list) {
            if (builder.length() > 1) {
                builder.append(", ");
            }
            builder.append(pair.toString());
        }
        return builder.append("]").toString();
    }

    public String toString() {
        return "mandatory: " + MediaConstraints.stringifyKeyValuePairList(this.mandatory) + ", optional: " + MediaConstraints.stringifyKeyValuePairList(this.optional);
    }

    @CalledByNative
    List<KeyValuePair> getMandatory() {
        return this.mandatory;
    }

    @CalledByNative
    List<KeyValuePair> getOptional() {
        return this.optional;
    }

    public static class KeyValuePair {
        private final String key;
        private final String value;

        public KeyValuePair(String key, String value) {
            this.key = key;
            this.value = value;
        }

        @CalledByNative(value="KeyValuePair")
        public String getKey() {
            return this.key;
        }

        @CalledByNative(value="KeyValuePair")
        public String getValue() {
            return this.value;
        }

        public String toString() {
            return this.key + ": " + this.value;
        }

        public boolean equals(@Nullable Object other) {
            if (this == other) {
                return true;
            }
            if (other == null || this.getClass() != other.getClass()) {
                return false;
            }
            KeyValuePair that = (KeyValuePair)other;
            return this.key.equals(that.key) && this.value.equals(that.value);
        }

        public int hashCode() {
            return this.key.hashCode() + this.value.hashCode();
        }
    }
}

