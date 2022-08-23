package me.opkarol.opc.api.utils;

public class Util {

    public static <K> K getOrDefault(K object, K defaultObject) {
        return object == null ? defaultObject : object;
    }
}
