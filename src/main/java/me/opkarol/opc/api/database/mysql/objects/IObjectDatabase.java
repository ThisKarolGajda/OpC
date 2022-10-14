package me.opkarol.opc.api.database.mysql.objects;

import me.opkarol.opc.api.database.mysql.resultset.OpMResultSet;
import me.opkarol.opc.api.map.OpMap;

import java.util.Optional;
import java.util.function.Function;

public abstract class IObjectDatabase<O, I> {
    private final OpMap<I, O> map = new OpMap<>();

    public OpMap<I, O> getMap() {
        return map;
    }

    public abstract void add(O object);

    public abstract Optional<O> get(I identification);

    public boolean contains(I identification) {
        return get(identification).isPresent();
    }

    public abstract boolean delete(I identification);

    public abstract void load(Function<OpMResultSet, O> getObjectFromSet);
}
