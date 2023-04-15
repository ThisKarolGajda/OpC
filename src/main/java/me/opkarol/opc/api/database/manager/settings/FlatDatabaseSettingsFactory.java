package me.opkarol.opc.api.database.manager.settings;

import java.util.function.BiFunction;
import java.util.function.Predicate;

public class FlatDatabaseSettingsFactory<O, C> extends FlatDatabaseSettings<O, C> {
    private final BiFunction<O, C, Boolean> biFunction;

    public FlatDatabaseSettingsFactory(String fileName, BiFunction<O, C, Boolean> biFunction) {
        super(fileName);
        this.biFunction = biFunction;
    }

    @Override
    public Predicate<O> getPredicate(C object) {
        return o -> biFunction.apply(o, object);
    }
}
