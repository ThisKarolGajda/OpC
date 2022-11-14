package me.opkarol.opc.api.database.manager;

import java.lang.reflect.ParameterizedType;

public abstract class AbstractDatabase<O> {
    private final Class<O> clazz;

    @SuppressWarnings("unchecked")
    public AbstractDatabase() {
        clazz = (Class<O>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];
    }

    public Class<O> getClazz() {
        return clazz;
    }
}
