package me.opkarol.opc.api.misc;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public record Tuple<A, B>(A first, B second) {

    @Contract("_, _ -> new")
    public static <A, B> @NotNull Tuple<A, B> of(A a, B b) {
        return new Tuple<>(a, b);
    }

    public static <A, B> @NotNull Tuple<A, B> empty() {
        return new Tuple<>(null, null);
    }
}
