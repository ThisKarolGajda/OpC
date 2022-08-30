package me.opkarol.opc.api.utils;

import org.jetbrains.annotations.NotNull;

public class Util {

    public static <K> K getOrDefault(K object, K defaultObject) {
        return object == null ? defaultObject : object;
    }

    public static @NotNull String ifEndsWithRemove(String text, String endsWith) {
        if (text == null) {
            return "";
        }
        return text.endsWith(endsWith) ? text.substring(0, text.length() - endsWith.length()) : text;
    }

    public static @NotNull String ifNotEndsWithAdd(String text, String endsWith) {
        if (text == null) {
            return "";
        }
        return text.endsWith(endsWith) ? text : text.concat(endsWith);
    }
}
