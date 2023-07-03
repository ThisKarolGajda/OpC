package me.opkarol.opc.api.utils;

import java.util.Optional;
import java.util.Random;

public class EnumUtils {
    public static <K extends Enum<?>> Optional<K> getRandomEnum(Class<K> clazz) {
        if (clazz == null || !clazz.isEnum()) {
            return Optional.empty();
        }

        K[] enumConstants = clazz.getEnumConstants();
        Random random = new Random();
        int randomIndex = random.nextInt(enumConstants.length);

        return getEnumByOrdinal(clazz, randomIndex);
    }

    public static <K extends Enum<?>> Optional<K> getEnumByOrdinal(Class<K> clazz, int ordinal) {
        if (clazz == null || !clazz.isEnum()) {
            return Optional.empty();
        }

        K[] enumConstants = clazz.getEnumConstants();
        return Optional.ofNullable(enumConstants[ordinal]);
    }

    public static <K extends Enum<?>> Optional<K> getRandomEnumFromString(Class<K> clazz, String input) {
        if (clazz == null || !clazz.isEnum()) {
            return Optional.empty();
        }

        K[] enumConstants = clazz.getEnumConstants();
        int randomIndex = Math.abs(input.hashCode()) % enumConstants.length;

        return Optional.ofNullable(enumConstants[randomIndex]);
    }
}
