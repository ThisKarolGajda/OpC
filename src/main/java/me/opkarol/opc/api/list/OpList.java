package me.opkarol.opc.api.list;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class OpList<K> implements IList<K>, Serializable {
    private List<K> list;

    public OpList(List<K> list) {
        this.list = list;
    }

    public OpList() {
        this.list = new ArrayList<>();
    }

    @SafeVarargs
    public OpList(K... ks) {
        this.list = new ArrayList<>();
        addAll(Arrays.asList(ks));
    }

    public static <K> @NotNull Collector<K, ?, OpList<K>> getCollector() {
        return Collectors.toCollection(OpList::new);
    }

    @SafeVarargs
    public static <K> @NotNull OpList<K> asList(K @Nullable ... a) {
        if (a == null || a.length == 0) {
            return new OpList<>();
        }
        return new OpList<>(Arrays.asList(a));
    }

    @Override
    public int size() {
        createIfNotExists();
        return list.size();
    }

    @Override
    public boolean isEmpty() {
        createIfNotExists();
        return list.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        createIfNotExists();
        return list.contains(o);
    }

    @NotNull
    @Override
    public Iterator<K> iterator() {
        return list.iterator();
    }

    @NotNull
    @Override
    public Object @NotNull [] toArray() {
        return list.toArray();
    }

    @NotNull
    @Override
    public <T> T @NotNull [] toArray(@NotNull T @NotNull [] a) {
        return list.toArray(a);
    }

    @Override
    public boolean add(K k) {
        createIfNotExists();
        return list.add(k);
    }

    @Override
    public boolean remove(Object o) {
        createIfNotExists();
        return list.remove(o);
    }

    @Override
    public boolean containsAll(@NotNull Collection<?> c) {
        return new HashSet<>(list).containsAll(c);
    }

    @Override
    public boolean addAll(@NotNull Collection<? extends K> c) {
        return list.addAll(c);
    }

    @Override
    public boolean removeAll(@NotNull Collection<?> c) {
        return list.removeAll(c);
    }

    @Override
    public void clear() {
        list.clear();
    }

    @Override
    public @NotNull Optional<K> get(int index) {
        if (!isPresent(index)) {
            return Optional.empty();
        }
        return Optional.of(list.get(index));
    }

    public K unsafeGet(int index) {
        return list.get(index);
    }

    @Override
    public K set(int index, K element) {
        createIfNotExists();
        return list.set(index, element);
    }

    @Override
    public void add(int index, K element) {
        createIfNotExists();
        list.add(index, element);
    }

    @Override
    public K remove(int index) {
        return list.remove(index);
    }

    @Override
    public K getOrDefault(int index, K def) {
        if (isPresent(index)) {
            return unsafeGet(index);
        }
        return def;
    }

    @Override
    public boolean removeIf(Predicate<? super K> predicate) {
        return list.removeIf(predicate);
    }

    @Override
    @Deprecated
    public boolean retainAll(@NotNull Collection<?> c) {
        return false;
    }

    @Override
    public boolean removeAndThen(@NotNull Predicate<K> predicate, @NotNull Consumer<K> consumer) {
        AtomicBoolean removed = new AtomicBoolean(false);
        list.forEach(obj -> {
            if (predicate.test(obj) && remove(obj)) {
                consumer.accept(obj);
                removed.set(true);
            }
        });
        return removed.get();
    }

    @Override
    public void forEach(Consumer<? super K> consumer) {
        createIfNotExists();
        list.forEach(consumer);
    }

    public Spliterator<K> spliterator() {
        return list.spliterator();
    }

    public Stream<K> stream() {
        return list.stream();
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OpList<?> opList = (OpList<?>) o;

        return list.equals(opList.list);
    }

    @Override
    public int hashCode() {
        return list.hashCode();
    }

    public void createIfNotExists() {
        if (list == null) {
            list = new ArrayList<>();
        }
    }

    public boolean isPresent(int index) {
        return list.size() > index;
    }

    public boolean ifPresent(int index, Consumer<K> consumer) {
        get(index).ifPresent(consumer);
        return isPresent(index);
    }

    public boolean ifPresentSpecified(int index, K key, Consumer<K> consumer) {
        if (isPresent(index)) {
            Optional<K> key2 = get(index);
            if (key2.isPresent() && key.equals(key2)) {
                key2.ifPresent(consumer);
            }
            return true;
        }
        return false;
    }

    public @NotNull String toArrayString() {
        return Arrays.toString(list.toArray());
    }
}
