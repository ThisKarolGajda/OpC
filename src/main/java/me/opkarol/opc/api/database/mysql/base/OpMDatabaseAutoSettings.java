package me.opkarol.opc.api.database.mysql.base;

import me.opkarol.opc.api.database.mysql.resultset.OpMResultSet;

import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;

public abstract class OpMDatabaseAutoSettings<O> {

    public abstract Predicate<O> getPredicate(Object object);

    public abstract Function<OpMResultSet, O> getObjectAsResultSet();

    public abstract Function<O, Integer> getIdentification();

    public abstract BiConsumer<O, Integer> setIdentification();

    public abstract Function<O, UUID> getUUID();
}
