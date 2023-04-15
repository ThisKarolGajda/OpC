package me.opkarol.opc.api.database.manager;

import me.opkarol.opc.api.map.OpMap;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;

public interface IDefaultDatabase<O, C> {

    Predicate<O> getPredicate(C object);

    boolean delete(C uuid);

    boolean contains(C uuid);

    Optional<O> get(C uuid);

    void add(C uuid, O object);

    List<O> getList(C uuid);

    OpMap<C, List<O>> getObjectsMap();

    default void checkAndUse(C object, Runnable noObject, Consumer<O> objectFound) {
        if (contains(object)) {
            Optional<O> optional = get(object);
            if (optional.isEmpty()) {
                noObject.run();
            } else {
                objectFound.accept(optional.get());
            }
        } else {
            noObject.run();
        }
    }

    default void checkAndUse(C object, Runnable noObject, Runnable objectFound) {
        if (contains(object)) {
            Optional<O> optional = get(object);
            if (optional.isEmpty()) {
                noObject.run();
            } else {
                objectFound.run();
            }
        } else {
            noObject.run();
        }
    }

    default void deleteAndUse(C object, Runnable noObject, Runnable objectFound) {
        if (delete(object)) {
            objectFound.run();
        } else {
            noObject.run();
        }
    }

    default void addAndUse(C object, O obj, Runnable noObject, Consumer<O> objectFound) {
        if (contains(object)) {
            noObject.run();
        } else {
            add(object, obj);
            objectFound.accept(obj);
        }
    }

    default void addAndUse(C object, O obj, Runnable noObject, Runnable objectFound) {
        if (contains(object)) {
            noObject.run();
        } else {
            add(object, obj);
            objectFound.run();
        }
    }
}