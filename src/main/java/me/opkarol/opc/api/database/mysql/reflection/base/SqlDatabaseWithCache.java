package me.opkarol.opc.api.database.mysql.reflection.base;

import me.opkarol.opc.api.database.manager.IDefaultDatabase;
import me.opkarol.opc.api.database.manager.settings.SqlDatabaseSettings;
import me.opkarol.opc.api.map.OpMap;
import me.opkarol.opc.api.tools.autostart.IDisable;
import me.opkarol.opc.api.tools.autostart.OpAutoDisable;
import me.opkarol.opc.api.utils.VariableUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class SqlDatabaseWithCache<O, C> extends SqlDatabase<O, C> implements IDefaultDatabase<O, C>, IDisable {
    private final OpMap<C, List<O>> objectMap = new OpMap<>();

    public SqlDatabaseWithCache(SqlDatabaseSettings settings, Class<O> clazz) {
        super(clazz, settings);
        initDatabaseCloseRegister();
        loadSqlObjectsIntoMap();
    }

    public void initDatabaseCloseRegister() {
        OpAutoDisable.add(this);
    }

    @SuppressWarnings("unchecked")
    public void loadSqlObjectsIntoMap() {
        for (O o : getIdCache()) {
            C comparable = (C) getObjects().getComparableObject().getObject(o);
            objectMap.set(comparable, VariableUtil.getWith(objectMap.getOrDefault(comparable, new ArrayList<>()), o));
        }
    }

    @Override
    public void onDisable() {
        this.close();
    }

    @Override
    public boolean delete(C object) {
        int id = getId(object, getPredicate(object));
        if (id != -1) {
            delete(id);
            return true;
        }
        return false;
    }

    public int getId(C uuid, Predicate<O> predicate) {
        return (int) getList(uuid).stream()
                .filter(predicate)
                .findAny()
                .map(getObjects().getIdentificationObject()::getObject)
                .orElse(-1);
    }

    public int getId(C object) {
        return getId(object, getPredicate(object));
    }

    @Override
    public boolean contains(C object) {
        return get(object).isPresent();
    }

    @Override
    public Optional<O> get(C object) {
        return get(object, getPredicate(object));
    }

    @Override
    public void add(C uuid, O object) {
        add(object);
        List<O> list = getList(uuid);
        list.add(object);
        objectMap.set(uuid, list);
    }

    @SuppressWarnings("unchecked")
    public void add(O object) {
        C compare = (C) getObjects().getComparableObject().getObject(object);
        add(compare, object);
    }

    public Optional<O> get(C uuid, Predicate<O> predicate) {
        return getList(uuid).stream()
                .filter(predicate)
                .findAny();
    }

    @Override
    public List<O> getList(C uuid) {
        return objectMap.getOrDefault(uuid, new ArrayList<>());
    }

    @Override
    public OpMap<C, List<O>> getObjectsMap() {
        return objectMap;
    }
}
