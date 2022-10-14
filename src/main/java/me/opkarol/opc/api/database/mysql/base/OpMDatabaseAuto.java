package me.opkarol.opc.api.database.mysql.base;

import java.util.UUID;
import java.util.function.Function;
import java.util.function.Predicate;

public abstract class OpMDatabaseAuto<O> extends OpMDatabase<O> {
    private final Function<O, Object> defaultSearch;

    public OpMDatabaseAuto(OpMSingleDatabase<O> database) {
        super(database);
        this.defaultSearch = getDefaultSearch();
    }

    public abstract Predicate<O> getPredicate(Object object, Function<O, Object> defaultSearch);

    public abstract Function<O, Object> getDefaultSearch();

    public boolean delete(UUID uuid, Object object) {
        return delete(uuid, getPredicate(object, defaultSearch));
    }

    public boolean contains(UUID uuid, Object object) {
        return contains(uuid, getPredicate(object, defaultSearch));
    }

    public int getId(UUID uuid, Object object) {
        return getId(uuid, getPredicate(object, defaultSearch));
    }
}