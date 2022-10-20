package me.opkarol.opc.api.database.mysql.reflection;

import me.opkarol.opc.api.utils.VariableUtil;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;

public class OpMReflection {
    private final Class<?> classObject;
    private OpMTable table;
    private final Objects objects = new Objects();
    private final List<Field> list;

    public OpMReflection(@NotNull Class<?> object) {
        this.classObject = object;
        setTableName();
        list = buildFields();

        for (Field field : list) {
            objects.add(new BetterObject(field));
        }

        getIdentificationMethod().ifPresent(objects::setIdentificationObject);
    }

    private @NotNull Optional<Method> getIdentificationMethod() {
        return Utils.getMethods(classObject, method -> method.isAnnotationPresent(OpMIdentification.class)).stream().findAny();
    }

    private void setTableName() {
        Utils.getAnnotations(classObject).stream().filter(a -> a.annotationType().equals(OpMTable.class)).findAny().ifPresent(a -> table = (OpMTable) a);
    }

    private List<Field> buildFields() {
        return Utils.getFields(classObject, field -> Utils.isAnnotationPresent(field.getClass(), OpMValue.class));
    }

    public Class<?> getClassObject() {
        return classObject;
    }

    public List<Field> getFields() {
        return list;
    }

    public OpMTable getTable() {
        return table;
    }

    public String getTableName() {
        return VariableUtil.getOrDefault(table.name(), "table");
    }

    public Objects getObjects() {
        return objects;
    }
}