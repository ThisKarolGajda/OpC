package me.opkarol.opc.api.utils;

public class MathUtils {

    public static int getRandomInt(int min, int max) {
        return (int) Math.floor(Math.random() * (max - min + 1) + min);
    }
}
