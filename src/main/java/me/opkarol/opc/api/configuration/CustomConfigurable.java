package me.opkarol.opc.api.configuration;

import me.opkarol.opc.OpC;

import java.util.function.Consumer;

public interface CustomConfigurable {
    default void save(String path) {
        save().accept(new CustomConfiguration().setPath(path));
    }

    default void get(String path) {
        OpC.getLog().info(path + " --- 2");
        get().accept(new CustomConfiguration().setPath(path));
    }
    Consumer<CustomConfiguration> get();

    Consumer<CustomConfiguration> save();
}
