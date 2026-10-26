/*
 * Decompiled with CFR 0.152.
 */
package com.korevg.mv.webrtcsdk.session;

public enum DTMF {
    ZERO,
    ONE,
    TWO,
    THREE,
    FOUR,
    FIVE,
    SIX,
    SEVEN,
    EIGHT,
    NINE,
    A,
    B,
    C,
    D,
    POUND,
    STAR;


    public String getDTMFString() {
        String dtmf = null;
        switch (this) {
            case ZERO: {
                dtmf = "0";
                break;
            }
            case ONE: {
                dtmf = "1";
                break;
            }
            case TWO: {
                dtmf = "2";
                break;
            }
            case THREE: {
                dtmf = "3";
                break;
            }
            case FOUR: {
                dtmf = "4";
                break;
            }
            case FIVE: {
                dtmf = "5";
                break;
            }
            case SIX: {
                dtmf = "6";
                break;
            }
            case SEVEN: {
                dtmf = "7";
                break;
            }
            case EIGHT: {
                dtmf = "8";
                break;
            }
            case NINE: {
                dtmf = "9";
                break;
            }
            case A: {
                dtmf = "A";
                break;
            }
            case B: {
                dtmf = "B";
                break;
            }
            case C: {
                dtmf = "C";
                break;
            }
            case D: {
                dtmf = "D";
                break;
            }
            case POUND: {
                dtmf = "#";
                break;
            }
            case STAR: {
                dtmf = "*";
            }
        }
        return dtmf;
    }
}

