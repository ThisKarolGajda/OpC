package me.opkarol.opc.api.database.mysql.reflection.base;

import me.opkarol.opc.api.database.manager.settings.SqlDatabaseSettings;
import me.opkarol.opc.api.database.mysql.reflection.SqlReflectionHelper;
import me.opkarol.opc.api.database.mysql.reflection.objects.SqlObject;
import me.opkarol.opc.api.database.mysql.resultset.SqlResultSet;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Optional;
import java.util.function.Predicate;

public abstract class SqlDatabase<O, C> extends SqlDatabaseHolder<O> {
    private final SqlObject getIdentification;
    private final Method setIdentification;
    private final SqlObject comparableObject;
    private final HashSet<O> idCache = new HashSet<>();

    public SqlDatabase(Class<O> clazz, SqlDatabaseSettings settings) {
        super(clazz, settings);
        createTable();
        this.getIdentification = getObjects().getIdentificationObject();
        this.setIdentification = getObjects().getIdentificationSetter();
        this.comparableObject = getObjects().getComparableObject();
        if (comparableObject == null) {
            throw new RuntimeException("Provided comparable object is null.");
        }
        loadObjects();
    }

    public HashSet<O> getIdCache() {
        return idCache;
    }

    public synchronized void add(O object) {
        int id = (int) getIdentification.getObject(object);
        int fixed = insertObject(object, id);
        if (fixed != id) {
            SqlReflectionHelper.invokeSafeMethod(setIdentification, object, fixed);
        }
        getIdCache().add(object);
    }

    public Optional<O> get(Integer identification) {
        return getIdCache().stream().filter(o -> getIdentification.getObject(o).equals(identification)).findAny();
    }

    public boolean delete(Integer identification) {
        Optional<O> optional = get(identification);
        if (optional.isEmpty()) {
            return false;
        }

        O object = optional.get();
        deleteObject(object);
        return getIdCache().removeIf(o -> getIdentification.getObject(o).equals(identification));
    }

    public void loadObjects() {
        ResultSet set = getResultSet();
        if (set == null) {
            return;
        }

        try {
            while (set.next()) {
                O object = SqlReflectionHelper.invokeSafeConstructor(getClassType(), getObjects()
                        .getObjects(new SqlResultSet(set)));
                if (object != null) {
                    getIdCache().add(object);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean contains(Integer identification) {
        return get(identification).isPresent();
    }

    public @NotNull Predicate<O> getPredicate(C object) {
        return o -> SqlReflectionHelper.get(comparableObject.getField(), o).equals(object);
    }
}
