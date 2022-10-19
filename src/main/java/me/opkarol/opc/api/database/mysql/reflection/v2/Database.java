package me.opkarol.opc.api.database.mysql.reflection.v2;

import me.opkarol.opc.OpC;
import me.opkarol.opc.api.database.mysql.objects.IObjectDatabase;
import me.opkarol.opc.api.database.mysql.reflection.Object;
import me.opkarol.opc.api.database.mysql.reflection.Utils;
import me.opkarol.opc.api.database.mysql.resultset.OpMResultSet;
import me.opkarol.opc.api.map.OpMap;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Predicate;

public abstract class Database<O> extends IObjectDatabase<O, Integer> {
    private final SingleDatabase<O> database;
    private final Object getIdentification;
    private final Method setIdentification;
    private final Object getUUID;
    private final OpMap<UUID, List<O>> uuidMap = new OpMap<>();

    public Database(SingleDatabase<O> database) throws InstantiationException {
        this.database = database;
        if (this.database != null) {
            database.create();
            load(getObjectAsResultSet());
        } else {
            throw new InstantiationException("Database provided is null.");
        }
        this.getIdentification = database.getObjects().getIdentificationObject();
        this.setIdentification = database.getObjects().getIdentification();
        this.getUUID = database.getObjects().getUUIDObject();
    }

    public SingleDatabase<O> getDatabase() {
        return database;
    }

    @Override
    public void add(O object) {
        int id = (int) getIdentification.getObject(object);
        int fixed = database.insert(object, id);
        if (fixed != id) {
            object = Utils.invokeMethod(setIdentification, object, fixed);
        }
        getMap().put(fixed, object);
        addObject(object, (UUID) getUUID.getObject(object));
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
        removeObject(object, o -> getUUID.getObject(o).equals(getUUID.getObject(object)));
        return true;
    }

    public boolean delete(Integer identification, Predicate<O> predicate) {
        Optional<O> optional = get(identification);
        if (optional.isEmpty()) {
            return false;
        }

        O object = optional.get();
        database.delete(object);
        getMap().remove(identification);
        removeObject(object, predicate);
        return true;
    }

    public void load(Function<OpMResultSet, O> getObjectFromSet) {
        ResultSet set = database.get();
        try {
            while (set.next()) {
                O object = getObjectFromSet.apply(new OpMResultSet(set));
                getMap().put((Integer) getIdentification.getObject(object), object);
                addObject(object, (UUID) getUUID.getObject(object));
                OpC.getInstance().getLogger().info(object.toString());
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

    private @NotNull List<O> removeObject(O object, Predicate<O> predicate) {
        UUID uuid = (UUID) getUUID.getObject(object);
        List<O> list = uuidMap.getOrDefault(uuid, new ArrayList<>());
        list.removeIf(predicate);
        uuidMap.set(uuid, list);
        return list;
    }

    public List<O> getList(UUID uuid) {
        return uuidMap.getOrDefault(uuid, new ArrayList<>());
    }

    public Optional<O> get(UUID uuid, Predicate<O> predicate) {
        return getList(uuid).stream()
                .filter(predicate)
                .findAny();
    }

    public int getId(UUID uuid, Predicate<O> predicate) {
        return (int) getList(uuid).stream()
                .filter(predicate)
                .findAny()
                .map(getIdentification::getObject)
                .orElse(-1);
    }

    public boolean contains(UUID uuid, Predicate<O> predicate) {
        return get(uuid, predicate)
                .isPresent();
    }

    public boolean delete(UUID uuid, Predicate<O> predicate) {
        int id = getId(uuid, predicate);
        if (id != -1) {
            delete(id, predicate);
            return true;
        }
        return false;
    }

    public abstract Function<OpMResultSet, O> getObjectAsResultSet();
}
