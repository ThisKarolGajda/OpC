package me.opkarol.opc.api.database.mysql.base;

import me.opkarol.opc.api.map.OpMap;

import java.util.function.Function;

public class OpMTripleDatabase<O, T, I> {
    private final OpMySqlDatabase<O> database;
    private final OpMap<I, T> map = new OpMap<>();
    private final Function<O, I> getIdentification;
    private final Function<O, T> getObjects;
    private final Function<T, O> getObject;

    public OpMTripleDatabase(OpMySqlDatabase<O> database, Function<O, I> getIdentificationFunction, Function<O, T> getObjectsFunction, Function<T, O> getObjectFunction) {
        this.database = database;
        if (this.database != null) {
            database.create();
        }
        this.getIdentification = getIdentificationFunction;
        this.getObjects = getObjectsFunction;
        this.getObject = getObjectFunction;
    }

    public OpMySqlDatabase<O> getDatabase() {
        return database;
    }

    public OpMap<I, T> getMap() {
        return map;
    }

    public void add(O object) {
        map.put(getIdentification.apply(object), getObjects.apply(object));
        database.insert(object);
    }

    public O getObject(I identification) {
        T object = getTransit(identification);
        if (object == null) {
            return null;
        }
        return getObject.apply(object);
    }

    public T getTransit(I identification) {
        return map.getOrDefault(identification, null);
    }

    public boolean contains(I identification) {
        return getTransit(identification) != null;
    }

    public boolean delete(I identification) {
        O object = getObject(identification);
        if (object == null) {
            return false;
        }

        database.delete(object);
        map.remove(identification);
        return true;

    }
}