package com.kore.ai.widgetsdk.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Shiva Krishna on 11/15/2017.
 */
public final class StringUtils {

    public static final String kora_thread = "Kore_Chat";
    public static final String kora_team = "Kore_Team";

    public static final String ELLIPSES_CHAR = "\u2026";
    public static final String EMPTY = "";
    /**
     * Valid characters in a team name as well as topic name
     */
    public static final String TEAM_NAME_REGEX = "[A-Za-z0-9][A-Za-z0-9\\-\\_\\s\\+]*";
    public static final long TEAM_NAME_AUTO_VALIDATION_DELAY = 2000L; // 2 seconds after user stops typing

    public static boolean isNullOrEmpty(CharSequence string) {
        return string == null || string.length() == 0;
    }

    public static boolean isNullOrEmpty(String string) {
        return string == null || string.isEmpty();
    }

    public static boolean isNullOrEmptyWithTrim(String string) {
        return string == null || string.trim().isEmpty();
    }

    public static boolean isNullOrEmptyWithTrim(CharSequence charSequence) {
        return charSequence == null || charSequence.toString().trim().isEmpty();
    }
    public static boolean containsAny(String haystack, String[] needles) {
        if (haystack == null) {
            return false;
        }

        for (String needle : needles) {
            if (haystack.contains(needle)) {
                return true;
            }
        }

        return false;
    }

    public static String capitalizeFirstLetter(String original) {
        if (isNullOrEmpty(original)) {
            return original;
        }
        return original.substring(0, 1).toUpperCase() + original.substring(1);
    }

    public static String replaceLast(String text, String regex, String replacement) {
        return text.replaceFirst("(?s)(.*)" + regex, "$1" + replacement);
    }

    /**
     * Add each character ascii value with
     * additional modulo on it to avoid "Java" with "Jvaa""
     */
    public static int stringToInt(String str) {
        int num = 0;
        int size = str.length();

        for (int i = 0; i < size; i++) {
            int _num = (int) str.charAt(i);
            num += _num + _num % (i + 1);
        }

        return num;
    }

    /**
     * contains with regex support
     */
    public static boolean contains(String source, String search) {
        String pattern = "\\b" + search + "\\b";
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(source);
        return m.find();
    }

    /**
     * contains ignoring case with regex support
     */
    public static boolean containsIgnoreCase(String source, String search) {
        source = source.toLowerCase();
        search = search.toLowerCase();

        String pattern = "\\b" + search + "\\b";
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(source);
        return m.find();
    }

    public static boolean isAlphanumericSpace_(CharSequence cs) {
        if (cs == null) {
            return false;
        } else {
            int sz = cs.length();

            for(int i = 0; i < sz; ++i) {
                if (!Character.isLetterOrDigit(cs.charAt(i)) && cs.charAt(i) != ' ' && cs.charAt(i) != '_') {
                    return false;
                }
            }

            return true;
        }
    }

    public static String getInitials(String name) {
        if (!StringUtils.isNullOrEmptyWithTrim(name)) {
            String[] strings = name.trim().split("\\s+");
            String str = (strings.length == 1) ? "" + strings[0].charAt(0) : ("" + strings[0].charAt(0) + strings[1].charAt(0));
            return str.toUpperCase();
        }
        return "";
    }

    public static String getInitials(String fn, String ln) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(!StringUtils.isNullOrEmptyWithTrim(fn) ? fn.charAt(0) : "");
        stringBuilder.append(!StringUtils.isNullOrEmptyWithTrim(ln) ? ln.charAt(0) : "");
        return stringBuilder.toString().trim();
    }
}
