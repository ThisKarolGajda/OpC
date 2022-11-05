package me.opkarol.opc.api.database.mysql.reflection;

import me.opkarol.opc.api.database.mysql.reflection.objects.BetterObject;
import me.opkarol.opc.api.database.mysql.reflection.objects.Objects;
import me.opkarol.opc.api.database.mysql.reflection.symbols.Identification;
import me.opkarol.opc.api.database.mysql.reflection.symbols.Table;
import me.opkarol.opc.api.database.mysql.reflection.symbols.Value;
import me.opkarol.opc.api.database.mysql.resultset.OpMResultSet;
import me.opkarol.opc.api.utils.VariableUtil;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class OpMReflection {
    private final Class<?> classObject;
    private Table table;
    private final Objects objects = new Objects();
    private final List<Field> list;

    public OpMReflection(Class<?> object) {
        this.classObject = object;
        setTableName();
        list = buildFields();

        for (Field field : list) {
            objects.add(new BetterObject(field));
        }

        getIdentificationMethod().ifPresent(objects::setIdentificationObject);
        getConstructor().ifPresent(objects::setConstructor);
    }

    public java.lang.Object[] getObjects(OpMResultSet set) {
        final java.lang.Object[][] objects = new java.lang.Object[1][1];
        getConstructor().ifPresent(constructor -> {
            objects[0] = new java.lang.Object[constructor.getParameterCount()];
            this.objects.setConstructor(constructor);
            for (int i = 0; i < constructor.getParameterCount(); i++) {
                int finalI = i;
                this.objects.getParameter(i).ifPresent(object1 -> {
                    Class<?> parameterType = constructor.getParameterTypes()[finalI];
                    Function<OpMResultSet, java.lang.Object> function1 = set1 -> {
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
        return OpMReflectionUtils.getMethods(classObject, method -> method.isAnnotationPresent(Identification.class)).stream().findAny();
    }

    private @NotNull Optional<Constructor<?>> getConstructor() {
        return Arrays.stream(classObject.getDeclaredConstructors()).filter(constructor -> constructor.isAnnotationPresent(me.opkarol.opc.api.database.mysql.reflection.symbols.Constructor.class)).findAny();
    }

    private void setTableName() {
        OpMReflectionUtils.getAnnotations(classObject).stream().filter(a -> a.annotationType().equals(Table.class)).findAny().ifPresent(a -> table = (Table) a);
    }

    private List<Field> buildFields() {
        return OpMReflectionUtils.getFields(classObject, field -> field.isAnnotationPresent(Value.class));
    }

    public Class<?> getClassObject() {
        return classObject;
    }

    public List<Field> getFields() {
        return list;
    }

    public Table getTable() {
        return table;
    }

    public String getTableName() {
        return VariableUtil.getOrDefault(table.name(), "table");
    }

    public Objects getObjects() {
        return objects;
    }
}