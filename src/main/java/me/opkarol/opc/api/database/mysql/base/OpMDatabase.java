package me.opkarol.opc.api.database.mysql.base;

import me.opkarol.opc.api.database.mysql.objects.IObjectDatabase;
import me.opkarol.opc.api.map.OpMap;
import org.jetbrains.annotations.NotNull;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public class OpMDatabase<O> extends IObjectDatabase<O, Integer> {
    private final OpMSingleDatabase<O> database;
    private final Function<O, Integer> getIdentification;
    private final BiFunction<O, Integer, O> setIdentification;
    private final Function<O, UUID> getUUID;
    private final OpMap<UUID, List<O>> uuidMap = new OpMap<>();

    public OpMDatabase(OpMSingleDatabase<O> database, Function<O, Integer> getIdentificationFunction, BiConsumer<O, Integer> setIdentification, Function<O, UUID> getUUID) {
        this.database = database;
        if (this.database != null) {
            database.create();
        }
        this.getIdentification = getIdentificationFunction;
        this.setIdentification = (o, integer) -> {
            setIdentification.accept(o, integer);
            return o;
        };
        this.getUUID = getUUID;
    }

    public OpMSingleDatabase<O> getDatabase() {
        return database;
    }

    @Override
    public void add(O object) {
        int id = getIdentification.apply(object);
        int fixed = database.insert(object, id);
        if (fixed != id) {
            object = setIdentification.apply(object, fixed);
        }
        getMap().put(fixed, object);
        addObject(object, getUUID.apply(object));
    }

    @Override
    public Optional<O> get(Integer identification) {
        return getMap().getByKey(identification);
    }

    @Override
    public boolean delete(Integer identification) {
        Optional<O> optional = get(identification);
        if (optional.isEmpty()) {
            return false;
        }

        O object = optional.get();
        database.delete(object);
        getMap().remove(identification);
        removeObject(object);
        return true;
    }

    public void load(Function<ResultSet, O> getObjectFromSet) {
        ResultSet set = database.get();
        try {
            while (set.next()) {
                O object = getObjectFromSet.apply(set);
                getMap().put(getIdentification.apply(object), object);
                addObject(object, getUUID.apply(object));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private @NotNull List<O> addObject(O object, UUID uuid) {
        List<O> list = uuidMap.getOrDefault(uuid, new ArrayList<>());
        list.add(object);
        uuidMap.set(uuid, list);
        return list;
    }

    private @NotNull List<O> removeObject(O object) {
        UUID uuid = getUUID.apply(object);
        List<O> list = uuidMap.getOrDefault(uuid, new ArrayList<>());
        list.removeIf(o -> getUUID.apply(o).equals(uuid));
        uuidMap.set(uuid, list);
        return list;
    }

    public List<O> get(UUID uuid) {
        return uuidMap.getOrDefault(uuid, new ArrayList<>());
    }
}