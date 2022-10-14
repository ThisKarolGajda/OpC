package me.opkarol.opc.api.database.mysql.base;

import me.opkarol.opc.api.database.mysql.objects.IObjectDatabase;
import me.opkarol.opc.api.database.mysql.resultset.OpMResultSet;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.function.Function;

public class OpMObjectDatabase<O, I> extends IObjectDatabase<O, I> {
    private final OpMySqlDatabase<O> database;
    private final Function<O, I> getIdentification;

    public OpMObjectDatabase(OpMySqlDatabase<O> database, Function<O, I> getIdentificationFunction) {
        this.database = database;
        if (this.database != null) {
            database.create();
        }
        this.getIdentification = getIdentificationFunction;
    }

    public OpMySqlDatabase<O> getDatabase() {
        return database;
    }

    @Override
    public void add(O object) {
        getMap().put(getIdentification.apply(object), object);
        database.insert(object);
    }

    @Override
    public Optional<O> get(I identification) {
        return getMap().getByKey(identification);
    }

    @Override
    public boolean delete(I identification) {
        Optional<O> object = get(identification);
        if (object.isEmpty()) {
            return false;
        }

        database.delete(object.get());
        getMap().remove(identification);
        return true;

    }

    public void load(Function<OpMResultSet, O> getObjectFromSet) {
        ResultSet set = database.get();
        try {
            while (set.next()) {
                O object = getObjectFromSet.apply(new OpMResultSet(set));
                getMap().put(getIdentification.apply(object), object);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}