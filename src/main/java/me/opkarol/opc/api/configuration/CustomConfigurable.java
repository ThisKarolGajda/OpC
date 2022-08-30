package me.opkarol.opc.api.configuration;

import java.util.function.Consumer;

public interface CustomConfigurable {
    default void save(String path) {
        save().accept(new CustomConfiguration().setPath(path));
    }

    default void get(String path) {
        get().accept(new CustomConfiguration().setPath(path));
    }
    Consumer<CustomConfiguration> get();

    Consumer<CustomConfiguration> save();
}
