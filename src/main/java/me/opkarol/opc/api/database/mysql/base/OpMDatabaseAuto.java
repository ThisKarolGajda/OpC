package me.opkarol.opc.api.database.mysql.base;

import me.opkarol.opc.api.database.manager.IDefaultDatabase;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;

public abstract class OpMDatabaseAuto<O, C> extends OpMDatabase<O> implements IDefaultDatabase<O, C> {

    public OpMDatabaseAuto(OpMSingleDatabase<O> database) {
        super(database);
    }

    public abstract Predicate<O> getPredicate(C object);

    public boolean delete(UUID uuid, C object) {
        return delete(uuid, getPredicate(object));
    }

    public boolean contains(UUID uuid, C object) {
        return contains(uuid, getPredicate(object));
    }

    public int getId(UUID uuid, C object) {
        return getId(uuid, getPredicate(object));
    }

    public Optional<O> get(UUID uuid, C object) {
        return get(uuid, getPredicate(object));
    }
}