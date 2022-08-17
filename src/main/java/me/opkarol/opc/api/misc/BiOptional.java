package me.opkarol.opc.api.misc;

/*
 * Copyright (c) 2021-2022.
 * [OpPets] ThisKarolGajda
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

@SuppressWarnings("all")
public final class BiOptional<A, B> {
    private final Optional<A> first;
    private final Optional<B> second;

    public BiOptional(Optional<A> first, Optional<B> second){
        this.first = first;
        this.second = second;
    }

    public BiOptional(A first, B second) {
        this.first = Optional.of(first);
        this.second = Optional.of(second);
    }

    public Optional<A> getFirst() {
        return first;
    }

    public Optional<B> getSecond() {
        return second;
    }

    public boolean firstPresent() {
        return first.isPresent();
    }

    public boolean secondPresent() {
        return second.isPresent();
    }

    public boolean bothPresent() {
        return first.isPresent() && second.isPresent();
    }

    public boolean atLeastOnePresent() {
        return first.isPresent() || second.isPresent();
    }

    public BiOptional<A, B> ifFirstPresent(Consumer<? super A> ifPresent){
        if (!second.isPresent()) {
            first.ifPresent(ifPresent);
        }
        return this;
    }

    public BiOptional<A, B> ifSecondPresent(Consumer<? super B> ifPresent){
        if (!first.isPresent()) {
            second.ifPresent(ifPresent);
        }
        return this;
    }

    public BiOptional<A, B> ifBothPresent(BiConsumer<? super A, ? super B> ifPresent){
        if(first.isPresent() && second.isPresent()){
            ifPresent.accept(first.get(), second.get());
        }
        return this;
    }

    public <T extends Throwable> void orElseThrow(Supplier<? extends T> exProvider) throws T{
        if(!first.isPresent() && !second.isPresent()){
            throw exProvider.get();
        }
    }

    public static <A, B> @NotNull BiOptional<A, B> empty() {
        return of(null, null);
    }

    public static <A, B> @NotNull BiOptional<A, B> of(A f, B s) {
        return new BiOptional<>(Optional.ofNullable(f), Optional.ofNullable(s));
    }
}
