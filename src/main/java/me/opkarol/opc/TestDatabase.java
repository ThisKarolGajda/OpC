package me.opkarol.opc;

import me.opkarol.opc.api.database.mysql.reflection.v2.AutoDatabase;
import me.opkarol.opc.api.database.mysql.reflection.v2.SingleDatabase;
import me.opkarol.opc.api.database.mysql.resultset.OpMResultSet;

import java.util.function.Function;
import java.util.function.Predicate;

public class TestDatabase extends AutoDatabase<TestObject> {

    public TestDatabase(SingleDatabase<TestObject> database) throws InstantiationException {
        super(database);
    }

    @Override
    public Predicate<TestObject> getPredicate(Object object) {
        return testObject -> testObject.equals(object);
    }

    @Override
    public Function<OpMResultSet, TestObject> getObjectAsResultSet() {
        return null;
    }
}
