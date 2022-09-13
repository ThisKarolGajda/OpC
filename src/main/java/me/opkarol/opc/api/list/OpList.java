package me.opkarol.opc.api.list;

import me.opkarol.opc.api.utils.VariableUtil;
import org.jetbrains.annotations.NotNull;

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
        return list.containsAll(c);
    }

    @Override
    public boolean addAll(@NotNull Collection<? extends K> c) {
        return list.addAll(c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return list.removeAll(c);
    }

    @Override
    public void clear() {
        list.clear();
    }

    @Override
    public K get(int index) {
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
        return VariableUtil.getOrDefault(get(index), def);
    }

    @Override
    public boolean removeIf(Predicate<? super K> predicate) {
        return list.removeIf(predicate);
    }

    @Override
    public boolean retainAll(@NotNull Collection<?> c) {
        return false;
    }

    @Override
    public boolean removeAndThen(Predicate<K> predicate, Consumer<K> consumer) {
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

    public static <K> @NotNull Collector<K, ?, OpList<K>> getCollector() {
        return Collectors.toCollection(OpList::new);
    }

    @SafeVarargs
    public static <K> @NotNull OpList<K> asList(K... a) {
        if (a == null || a.length == 0) {
            return new OpList<>();
        }
        return new OpList<>(Arrays.asList(a));
    }

    @Override
    public boolean equals(Object o) {
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
}
