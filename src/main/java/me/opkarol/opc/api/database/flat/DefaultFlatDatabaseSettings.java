package me.opkarol.opc.api.database.flat;

import java.util.function.Predicate;

public abstract class DefaultFlatDatabaseSettings<O> {

    public abstract Predicate<O> getPredicate(Object object);

    public abstract String getFileName();
}
