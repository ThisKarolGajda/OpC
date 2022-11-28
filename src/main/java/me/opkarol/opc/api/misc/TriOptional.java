package me.opkarol.opc.api.misc;

import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

@SuppressWarnings("all")
public class TriOptional<A, B, C> {
    private final Optional<A> first;
    private final Optional<B> second;
    private final Optional<C> third;

    public TriOptional(Optional<A> first, Optional<B> second, Optional<C> third){
        this.first = first;
        this.second = second;
        this.third = third;
    }

    public TriOptional(A first, B second, C third) {
        this.first = Optional.of(first);
        this.second = Optional.of(second);
        this.third = Optional.of(third);
    }

    public Optional<A> getFirst() {
        return first;
    }

    public Optional<B> getSecond() {
        return second;
    }

    public Optional<C> getThird() {
        return third;
    }

    public A getFirstObject() {
        return first.get();
    }

    public B getSecondObject() {
        return second.get();
    }

    public C getThirdObject() {
        return third.get();
    }

    public boolean firstPresent() {
        return first.isPresent();
    }

    public boolean secondPresent() {
        return second.isPresent();
    }

    public boolean thirdPresent() {
        return third.isPresent();
    }

    public boolean allPresent() {
        return first.isPresent() && second.isPresent() && third.isPresent();
    }

    public boolean atLeastOnePresent() {
        return first.isPresent() || second.isPresent() || third.isPresent();
    }

    public TriOptional<A, B, C> ifFirstPresent(Consumer<? super A> ifPresent) {
        if (second.isEmpty()) {
            first.ifPresent(ifPresent);
        }
        return this;
    }

    public TriOptional<A, B, C> ifSecondPresent(Consumer<? super B> ifPresent) {
        if (first.isEmpty()) {
            second.ifPresent(ifPresent);
        }
        return this;
    }

    public TriOptional<A, B, C> ifAllPresent(TriConsumer<? super A, ? super B, ? super C> ifPresent) {
        if(firstPresent() && secondPresent() && thirdPresent()){
            ifPresent.accept(getFirstObject(), getSecondObject(), getThirdObject());
        }
        return this;
    }

    public <T extends Throwable> void orElseThrow(Supplier<? extends T> exProvider) throws T {
        if(first.isEmpty() && second.isEmpty() && third.isEmpty()){
            throw exProvider.get();
        }
    }

    public static <A, B, C> @NotNull TriOptional<A, B, C> empty() {
        return of(null, null, null);
    }

    public static <A, B, C> @NotNull TriOptional<A, B, C> of(A a, B b, C c) {
        return new TriOptional<>(Optional.ofNullable(a), Optional.ofNullable(b), Optional.ofNullable(c));
    }
}
