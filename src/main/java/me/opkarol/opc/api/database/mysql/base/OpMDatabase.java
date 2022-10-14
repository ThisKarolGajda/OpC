package me.opkarol.opc.api.database.mysql.base;

import me.opkarol.opc.api.database.mysql.objects.IObjectDatabase;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public class OpMDatabase<O> extends IObjectDatabase<O, Integer> {
    private final OpMSingleDatabase<O> database;
    private final Function<O, Integer> getIdentification;
    private final BiFunction<O, Integer, O> setIdentification;

    public OpMDatabase(OpMSingleDatabase<O> database, Function<O, Integer> getIdentificationFunction, BiConsumer<O, Integer> setIdentification) {
        this.database = database;
        if (this.database != null) {
            database.create();
        }
        this.getIdentification = getIdentificationFunction;
        this.setIdentification = (o, integer) -> {
            setIdentification.accept(o, integer);
            return o;
        };
    }

    public OpMSingleDatabase<O> getDatabase() {
        return database;
    }

    @Override
    public void add(O object) {
        int id = getIdentification.apply(object);
        getMap().put(id, object);
        int fixed = database.insert(object, id);
        if (fixed == -1) {
            setIdentification.apply(object, fixed);
        }
    }

    @Override
    public Optional<O> get(Integer identification) {
        return getMap().getByKey(identification);
    }

    @Override
    public boolean delete(Integer identification) {
        Optional<O> object = get(identification);
        if (object.isEmpty()) {
            return false;
        }

        database.delete(object.get());
        getMap().remove(identification);
        return true;
    }

    public void load(Function<ResultSet, O> getObjectFromSet) {
        ResultSet set = database.get();
        try {
            while (set.next()) {
                O object = getObjectFromSet.apply(set);
                getMap().put(getIdentification.apply(object), object);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}