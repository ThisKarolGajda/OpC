package me.opkarol.opc.api.tools;

import me.opkarol.opc.api.map.OpMap;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

public class SingletonHolder {

    private static final OpMap<Class<?>, ClassHolder<?>> map = new OpMap<>();

    public SingletonHolder() {
        addInstance(this);
    }

    public static <K> void addInstance(@NotNull K k) {
        map.set(k.getClass(), new ClassHolder<>(k));
    }

    public static <K> K getInstance(Class<K> kClass) {
        if (map.getMap().containsKey(kClass)) {
            if (map.getMap().get(kClass).object != null) {
                return (K) map.getMap().get(kClass).object();
            }
        }
        try {
            K object = (K) Class.forName(kClass.getName()).getConstructor().newInstance();
            addInstance(object);
            return object;
        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | InstantiationException |
                 IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <K> Optional<K> getOptionalInstance(Class<K> kClass) {
        return Optional.ofNullable(getInstance(kClass));
    }

    public record ClassHolder<K>(K object) {
    }
}