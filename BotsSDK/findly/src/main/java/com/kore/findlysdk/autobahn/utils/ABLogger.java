package com.kore.findlysdk.autobahn.utils;

public class ABLogger {

    private static boolean isAndroid() {
        return System.getProperty("java.vendor").equals("The Android Project");
    }

    public static IABLogger getLogger(String tag) {
        Class<?> loggerClass;

        try {
            if (isAndroid()) {
                loggerClass = Class.forName("com.kore.findlysdk.autobahn.utils.ABALogger");
            } else {
                loggerClass = Class.forName("com.kore.findlysdk.autobahn.utils.ABJLogger");
            }

            return (IABLogger) loggerClass.getConstructor(String.class).newInstance(tag);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
