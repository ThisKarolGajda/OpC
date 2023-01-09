package me.opkarol.opc.api.database.mysql.reflection;

import me.opkarol.opc.api.database.mysql.reflection.objects.BetterMySqlObject;
import me.opkarol.opc.api.database.mysql.reflection.objects.MySqlObjects;
import me.opkarol.opc.api.database.mysql.reflection.symbols.MySqlIdentification;
import me.opkarol.opc.api.database.mysql.reflection.symbols.MySqlConstructor;
import me.opkarol.opc.api.database.mysql.reflection.symbols.MySqlTable;
import me.opkarol.opc.api.database.mysql.reflection.symbols.MySqlValue;
import me.opkarol.opc.api.database.mysql.resultset.MySqlResultSet;
import me.opkarol.opc.api.utils.VariableUtil;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class MySqlReflection {
    private final Class<?> classObject;
    private MySqlTable table;
    private final MySqlObjects objects = new MySqlObjects();
    private final List<Field> list;

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

    public java.lang.Object[] getObjects(MySqlResultSet set) {
        final java.lang.Object[][] objects = new java.lang.Object[1][1];
        getConstructor().ifPresent(constructor -> {
            objects[0] = new java.lang.Object[constructor.getParameterCount()];
            this.objects.setConstructor(constructor);
            for (int i = 0; i < constructor.getParameterCount(); i++) {
                int finalI = i;
                this.objects.getParameter(i).ifPresent(object1 -> {
                    Class<?> parameterType = constructor.getParameterTypes()[finalI];
                    Function<MySqlResultSet, java.lang.Object> function1 = set1 -> {
                        switch (parameterType.getSimpleName()) {
                            case "UUID" -> {
                                return set1.getUUID(object1.getName());
                            }
                            case "String" -> {
                                return set1.getText(object1.getName());
                            }
                            default -> {
                                return set1.getObject(object1.getName());
                            }
                        }
                    };

                    objects[0][finalI] = function1.apply(set);
                });
            }

        });
        return objects[0];
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