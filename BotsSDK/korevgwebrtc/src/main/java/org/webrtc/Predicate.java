/*
 * Decompiled with CFR 0.152.
 */
package org.webrtc;

public interface Predicate<T> {
    public boolean test(T var1);

    default public Predicate<T> or(final Predicate<? super T> other) {
        return new Predicate<T>(){

            @Override
            public boolean test(T arg) {
                return Predicate.this.test(arg) || other.test(arg);
            }
        };
    }

    default public Predicate<T> and(final Predicate<? super T> other) {
        return new Predicate<T>(){

            @Override
            public boolean test(T arg) {
                return Predicate.this.test(arg) && other.test(arg);
            }
        };
    }

    default public Predicate<T> negate() {
        return new Predicate<T>(){

            @Override
            public boolean test(T arg) {
                return !Predicate.this.test(arg);
            }
        };
    }
}

