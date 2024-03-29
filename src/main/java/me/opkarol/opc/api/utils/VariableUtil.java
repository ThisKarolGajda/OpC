package me.opkarol.opc.api.utils;

import me.opkarol.opc.api.map.OpMap;
import me.opkarol.opc.api.misc.Tuple;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class VariableUtil {

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

    @SafeVarargs
    public static <A, B> @NotNull OpMap<A, B> getMapFromTuples(Tuple<A, B>... tuples) {
        OpMap<A, B> map = new OpMap<>();
        if (tuples == null) {
            return map;
        }

        for (Tuple<A, B> tuple : tuples) {
            map.set(tuple.first(), tuple.second());
        }
        return map;
    }

    public static <K> @NotNull List<K> getWith(@Nullable List<K> list, K k) {
        if (list == null) {
            list = new ArrayList<>();
        }

        list.add(k);
        return list;
    }

    @Contract(pure = true)
    public static int @NotNull [] range(int from, int to) {
        int size = to - from + 1; // Additional 1 so it includes last number
        int[] range = new int[size];
        for (int i = 0; i < size; i++) {
            range[i] = from + i;
        }
        return range;
    }

    public static <K, V> @NotNull String stringValueOfMap(@NotNull OpMap<K, V> map) {
        StringBuilder builder = new StringBuilder();

        for (K key : map.keySet()) {
            V value = map.unsafeGet(key);

            builder.append(key).append("-").append(value).append(", ");
        }
        return builder.toString();
    }

    public static <K, V> @NotNull String stringValueOfMap(@NotNull Map<K, V> map) {
        StringBuilder builder = new StringBuilder();

        for (K key : map.keySet()) {
            V value = map.get(key);

            builder.append(key).append("-").append(value).append(", ");
        }
        return builder.toString();
    }

    public static Material getRandomSolidMaterial(String input) {
        if (input == null) {
            return Material.STONE;
        }

        long seed = input.hashCode();
        Random random = new Random(seed);

        Material[] materials = Material.values();

        Material[] selectiveMaterials = Arrays.stream(materials)
                .filter(Material::isSolid)
                .filter(Material::isItem)
                // Remove "Disabled Items" that are pre-1.20
                .filter(material -> material.isEnabledByFeature(Bukkit.getWorlds().get(0)))
                .toArray(Material[]::new);

        int randomIndex = random.nextInt(selectiveMaterials.length);
        return selectiveMaterials[randomIndex];
    }
}
