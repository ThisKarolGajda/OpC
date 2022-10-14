package me.opkarol.opc.api.database.mysql.base;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;

public abstract class OpMDatabaseAuto<O> extends OpMDatabase<O> {

    public OpMDatabaseAuto(OpMSingleDatabase<O> database) {
        super(database);
    }

    public abstract Predicate<O> getPredicate(Object object);

    public boolean delete(UUID uuid, Object object) {
        return delete(uuid, getPredicate(object));
    }

    public boolean contains(UUID uuid, Object object) {
        return contains(uuid, getPredicate(object));
    }

    public int getId(UUID uuid, Object object) {
        return getId(uuid, getPredicate(object));
    }

    public Optional<O> get(UUID uuid, Object object) {
        return get(uuid, getPredicate(object));
    }
}