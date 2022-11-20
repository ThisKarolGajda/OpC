package me.opkarol.opc.api.tools;

import me.opkarol.opc.api.map.OpMap;
import org.jetbrains.annotations.NotNull;

public class SingletonHolder {

    public record ClassHolder<K>(K object) {
    }

    public SingletonHolder() {
        addInstance(this);
    }

    private static final OpMap<Class<?>, ClassHolder<?>> map = new OpMap<>();

    public static <K> void addInstance(@NotNull K k) {
        map.set(k.getClass(), new ClassHolder<>(k));
    }

    public static <K> K getInstance(Class<K> kClass) {
        return (K) (map.getMap().get(kClass)).object();
    }
}