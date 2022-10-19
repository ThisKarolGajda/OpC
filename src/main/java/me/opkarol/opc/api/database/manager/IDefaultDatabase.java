package me.opkarol.opc.api.database.manager;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;

public interface IDefaultDatabase<O> {

    Predicate<O> getPredicate(Object object);

    boolean delete(UUID uuid, Object object);

    boolean contains(UUID uuid, Object object);

    Optional<O> get(UUID uuid, Object object);
}
