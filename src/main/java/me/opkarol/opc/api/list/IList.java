package me.opkarol.opc.api.list;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

public interface IList<K> extends Collection<K> {

    int size();

    boolean isEmpty();

    boolean contains(Object o);

    boolean add(K k);

    boolean remove(Object o);

    boolean addAll(@NotNull Collection<? extends K> c);

    boolean removeAll(Collection<?> c);

    void clear();

    Optional<K> get(int index);

    K set(int index, K element);

    void add(int index, K element);

    K remove(int index);

    K getOrDefault(int index, K def);

    boolean removeIf(Predicate<? super K> predicate);

    /**
     * If the provided predicate is not null, and its valid then it executes the consumer.
     *
     * @param predicate given predicate
     * @param consumer consumer that run on valid predicate
     */
    boolean removeAndThen(Predicate<K> predicate, Consumer<K> consumer);

    void forEach(Consumer<? super K> consumer);

    Stream<K> stream();

    String toArrayString();
}
