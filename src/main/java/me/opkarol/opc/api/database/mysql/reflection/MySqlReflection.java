package me.opkarol.opc.api.database.mysql.reflection;

import me.opkarol.opc.api.database.mysql.reflection.objects.BetterMySqlObject;
import me.opkarol.opc.api.database.mysql.reflection.objects.MySqlObjects;
import me.opkarol.opc.api.database.mysql.reflection.symbols.MySqlConstructor;
import me.opkarol.opc.api.database.mysql.reflection.symbols.MySqlIdentification;
import me.opkarol.opc.api.database.mysql.reflection.symbols.MySqlTable;
import me.opkarol.opc.api.database.mysql.reflection.symbols.MySqlValue;
import me.opkarol.opc.api.utils.VariableUtil;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class MySqlReflection {
    private final Class<?> classObject;
    private final MySqlObjects objects = new MySqlObjects();
    private final List<Field> list;
    private MySqlTable table;

    public MySqlReflection(Class<?> object) {
        this.classObject = object;
        setTableName();
        list = buildFields();

        for (Field field : list) {
            objects.add(new BetterMySqlObject(field));
        }

        getIdentificationMethod().ifPresent(objects::setIdentificationObject);
        getConstructor().ifPresent(objects::setConstructor);
    }

    private @NotNull Optional<Method> getIdentificationMethod() {
        return MySqlReflectionUtils.getMethods(classObject, method -> method.isAnnotationPresent(MySqlIdentification.class)).stream().findAny();
    }

    private @NotNull Optional<Constructor<?>> getConstructor() {
        return Arrays.stream(classObject.getDeclaredConstructors()).filter(constructor -> constructor.isAnnotationPresent(MySqlConstructor.class)).findAny();
    }

    private void setTableName() {
        MySqlReflectionUtils.getAnnotations(classObject).stream().filter(a -> a.annotationType().equals(MySqlTable.class)).findAny().ifPresent(a -> table = (MySqlTable) a);
    }

    private List<Field> buildFields() {
        return MySqlReflectionUtils.getFields(classObject, field -> field.isAnnotationPresent(MySqlValue.class));
    }

    public Class<?> getClassObject() {
        return classObject;
    }

    public List<Field> getFields() {
        return list;
    }

    public MySqlTable getTable() {
        return table;
    }

    public String getTableName() {
        return VariableUtil.getOrDefault(table.name(), "table");
    }

    public MySqlObjects getObjects() {
        return objects;
    }
}