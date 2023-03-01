package me.opkarol.opc.api.database.mysql.reflection.base;

import me.opkarol.opc.api.database.manager.settings.MySqlDatabaseSettings;
import me.opkarol.opc.api.database.mysql.reflection.MySqlReflectionUtils;
import me.opkarol.opc.api.database.mysql.reflection.objects.MySqlObject;
import me.opkarol.opc.api.database.mysql.resultset.MySqlResultSet;
import me.opkarol.opc.api.file.Configuration;
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

public abstract class MySqlDatabaseImpl<O, C> extends MySqlDatabase<O> {
    private static MySqlDatabaseImpl<?, ?> object;
    private final MySqlObject getIdentification;
    private final Method setIdentification;
    private final MySqlObject getUUID;
    private final MySqlObject comparableObject;
    private final OpMap<UUID, List<O>> uuidMap = new OpMap<>();
    private final OpMap<Integer, O> map = new OpMap<>();

    public MySqlDatabaseImpl() {
        create();
        this.getIdentification = getObjects().getIdentificationObject();
        if (getIdentification == null) {
            throw new RuntimeException("Provided getIdentification method is null.");
        }
        this.setIdentification = getObjects().getIdentification();
        this.getUUID = getObjects().getUUIDObject();
        if (getUUID == null) {
            throw new RuntimeException("Provided getUUID method is null.");
        }
        this.comparableObject = getObjects().getComparableObject();
        if (comparableObject == null) {
            throw new RuntimeException("Provided comparable object is null.");
        }
        load(set -> MySqlReflectionUtils.invokeSafeConstructor(getType(), getObjects().getObjects(set)));
        object = this;
    }

    public MySqlDatabaseImpl(String mysql) {
        super(mysql);
        create();
        this.getIdentification = getObjects().getIdentificationObject();
        if (getIdentification == null) {
            throw new RuntimeException("Provided getIdentification method is null.");
        }
        this.setIdentification = getObjects().getIdentification();
        this.getUUID = getObjects().getUUIDObject();
        if (getUUID == null) {
            throw new RuntimeException("Provided getUUID method is null.");
        }
        this.comparableObject = getObjects().getComparableObject();
        if (comparableObject == null) {
            throw new RuntimeException("Provided comparable object is null.");
        }
        load(set -> MySqlReflectionUtils.invokeSafeConstructor(getType(), getObjects().getObjects(set)));
        object = this;
    }

    public MySqlDatabaseImpl(MySqlDatabaseSettings mysql) {
        super(mysql);
        create();
        this.getIdentification = getObjects().getIdentificationObject();
        if (getIdentification == null) {
            throw new RuntimeException("Provided getIdentification method is null.");
        }
        this.setIdentification = getObjects().getIdentification();
        this.getUUID = getObjects().getUUIDObject();
        if (getUUID == null) {
            throw new RuntimeException("Provided getUUID method is null.");
        }
        this.comparableObject = getObjects().getComparableObject();
        if (comparableObject == null) {
            throw new RuntimeException("Provided comparable object is null.");
        }
        load(set -> MySqlReflectionUtils.invokeSafeConstructor(getType(), getObjects().getObjects(set)));
        object = this;
    }

    public MySqlDatabaseImpl(Class<O> clazz, MySqlDatabaseSettings mysql) {
        super(clazz, mysql);
        create();
        this.getIdentification = getObjects().getIdentificationObject();
        if (getIdentification == null) {
            throw new RuntimeException("Provided getIdentification method is null.");
        }
        this.setIdentification = getObjects().getIdentification();
        this.getUUID = getObjects().getUUIDObject();
        if (getUUID == null) {
            throw new RuntimeException("Provided getUUID method is null.");
        }
        this.comparableObject = getObjects().getComparableObject();
        if (comparableObject == null) {
            throw new RuntimeException("Provided comparable object is null.");
        }
        load(set -> MySqlReflectionUtils.invokeSafeConstructor(getType(), getObjects().getObjects(set)));
        object = this;
    }

    public MySqlDatabaseImpl(Configuration configuration, String mysql) {
        super(configuration, mysql);
        create();
        this.getIdentification = getObjects().getIdentificationObject();
        if (getIdentification == null) {
            throw new RuntimeException("Provided getIdentification method is null.");
        }
        this.setIdentification = getObjects().getIdentification();
        this.getUUID = getObjects().getUUIDObject();
        if (getUUID == null) {
            throw new RuntimeException("Provided getUUID method is null.");
        }
        this.comparableObject = getObjects().getComparableObject();
        if (comparableObject == null) {
            throw new RuntimeException("Provided comparable object is null.");
        }
        load(set -> MySqlReflectionUtils.invokeSafeConstructor(getType(), getObjects().getObjects(set)));
        object = this;
    }

    public MySqlDatabaseImpl(Class<?> clazz, Configuration configuration, String mysql) {
        super(clazz, configuration, mysql);
        create();
        this.getIdentification = getObjects().getIdentificationObject();
        if (getIdentification == null) {
            throw new RuntimeException("Provided getIdentification method is null.");
        }
        this.setIdentification = getObjects().getIdentification();
        this.getUUID = getObjects().getUUIDObject();
        if (getUUID == null) {
            throw new RuntimeException("Provided getUUID method is null.");
        }
        this.comparableObject = getObjects().getComparableObject();
        if (comparableObject == null) {
            throw new RuntimeException("Provided comparable object is null.");
        }
        load(set -> MySqlReflectionUtils.invokeSafeConstructor(getType(), getObjects().getObjects(set)));
        object = this;
    }

    public static <O, C> MySqlDatabaseImpl<O, C> getInstance() {
        return (MySqlDatabaseImpl<O, C>) object;
    }

    public OpMap<Integer, O> getMap() {
        return map;
    }

    public void add(O object) {
        int id = (int) getIdentification.getObject(object);
        int fixed = insert(object, id);
        if (fixed != id) {
            MySqlReflectionUtils.invokeSafeMethod(setIdentification, object, fixed);
        }
        getMap().put(fixed, object);
        addObject(object, (UUID) getUUID.getObject(object));
    }

    public Optional<O> get(Integer identification) {
        return getMap().getByKey(identification);
    }

    public boolean delete(Integer identification) {
        Optional<O> optional = get(identification);
        if (optional.isEmpty()) {
            return false;
        }

        O object = optional.get();
        delete(object);
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
        delete(object);
        getMap().remove(identification);
        removeObject(object, predicate);
        return true;
    }

    public void load(Function<MySqlResultSet, O> getObjectFromSet) {
        if (getObjectFromSet == null) {
            return;
        }

        ResultSet set = get();
        if (set == null) {
            return;
        }

        try {
            while (set.next()) {
                O object = getObjectFromSet.apply(new MySqlResultSet(set));
                putObject(object);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void putObject(O object) {
        getMap().put((Integer) getIdentification.getObject(object), object);
        addObject(object, (UUID) getUUID.getObject(object));
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

    public Optional<O> get(UUID uuid, C object) {
        return getList(uuid).stream()
                .filter(getPredicate(object))
                .findAny();
    }

    public int getId(UUID uuid, Predicate<O> predicate) {
        return (int) getList(uuid).stream()
                .filter(predicate)
                .findAny()
                .map(getIdentification::getObject)
                .orElse(-1);
    }

    public int getId(UUID uuid, C object) {
        return (int) getList(uuid).stream()
                .filter(getPredicate(object))
                .findAny()
                .map(getIdentification::getObject)
                .orElse(-1);
    }

    public boolean contains(UUID uuid, Predicate<O> predicate) {
        return get(uuid, predicate)
                .isPresent();
    }

    public boolean contains(UUID uuid, C object) {
        return get(uuid, getPredicate(object))
                .isPresent();
    }

    public boolean contains(Integer identification) {
        return get(identification).isPresent();
    }

    public @NotNull Predicate<O> getPredicate(C object) {
        return o -> MySqlReflectionUtils.get(comparableObject.getField(), o).equals(object);
    }

    public boolean delete(UUID uuid, C object) {
        int id = getId(uuid, getPredicate(object));
        if (id != -1) {
            delete(id, getPredicate(object));
            return true;
        }
        return false;
    }

}
