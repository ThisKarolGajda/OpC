package me.opkarol.opc.api.database.manager.settings;

import java.util.function.Predicate;

public abstract class FlatDatabaseSettings<O, C> {
    private final String fileName;

    public FlatDatabaseSettings(String fileName) {
        this.fileName = fileName;
    }

    public abstract Predicate<O> getPredicate(C object);

    public String getFileName() {
        return fileName;
    }
}
