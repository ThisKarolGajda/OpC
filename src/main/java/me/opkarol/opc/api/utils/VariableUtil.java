package me.opkarol.opc.api.utils;

import me.opkarol.opc.api.map.OpMap;
import me.opkarol.opc.api.misc.Tuple;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

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
}
