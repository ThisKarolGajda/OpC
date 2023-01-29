package me.opkarol.opc.api.map;

import me.opkarol.opc.api.wrappers.OpObjectSerialized;
import me.opkarol.opc.api.utils.StringUtil;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public class OpMapBuilder<K, V> extends OpMap<K, V> {
    public OpMapBuilder<K, V> setValue(K k, V v) {
        set(k, v);
        return this;
    }

    public OpMapBuilder<K, V> removeValue(K k, V v) {
        remove(k, v);
        return this;
    }

    @SuppressWarnings("unchecked")
    public <S> S getIfCorrect(K k, Class<? extends S> clazz) {
        Optional<V> optional = getByKey(k);
        if (optional.isPresent()) {
            V value = optional.get();
            if (value.getClass().equals(clazz)) {
                return (S) value;
            }
        }

        return (S) DefaultSwitchValues.getDefaultValue(clazz).orElse(null);
    }

    public void ifPresentThen(K k, Consumer<V> action) {
        getByKey(k).ifPresent(action);
    }

    public <S extends V, H> void ifPresentThen(K k, Consumer<S> action, Consumer<H> error) {
        Optional<V> value = getByKey(k);
        try {
            value.ifPresent((Consumer<? super V>) action);
        } catch (ClassCastException ignore) {
            value.ifPresent(object -> error.accept((H) object));
        }
    }

    public <S extends V> void ifPresentThen(K k, Consumer<S> action, Function<V, S> error) {
        Optional<V> value = getByKey(k);
        try {
            value.ifPresent((Consumer<? super V>) action);
        } catch (ClassCastException ignore) {
            value.ifPresent(error::apply);
        }
    }

    public <S extends V> void ifPresentSpecificThen(K k, Consumer<S> action) {
        getByKey(k).ifPresent((Consumer<? super V>) action);
    }

    public <S extends Enum<S>> void ifPresentEnumThen(K k, Class<S> clazz, Consumer<? super S> action) {
        getByKey(k).flatMap(object -> StringUtil.getEnumValue((String) object, clazz)).ifPresent(action);
    }

    public <W> void ifPresentObjectSerialized(K k, Consumer<? super W> action) {
        Optional<V> optional = getByKey(k);
        if (optional.isEmpty()) {
            return;
        }

        V value = optional.get();
        if (value instanceof OpObjectSerialized serialized) {
            serialized.ifPresentThen(action);
        }
    }

    public <S> void ifPresentClassThen(K k, Class<S> clazz, Consumer<S> action) {
        Optional<V> optional = getByKey(k);
        if (optional.isEmpty()) {
            return;
        }
        V value = optional.get();
        if (!value.getClass().equals(clazz)) {
            return;
        }
        action.accept((S) value);
    }

    static final class DefaultSwitchValues {
        private static final OpMap<Class<?>, Object> map = new OpMap<>();

        static {
            map.set(int.class, -1);
            map.set(Integer.class, -1);
        }

        public static Optional<Object> getDefaultValue(Class<?> clazz) {
            return map.getByKey(clazz);
        }
    }
}
