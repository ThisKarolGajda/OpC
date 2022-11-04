package me.opkarol.opc.api.database.mysql.reflection.objects;

import me.opkarol.opc.api.database.mysql.reflection.symbols.Value;
import me.opkarol.opc.api.database.mysql.resultset.OpMResultSet;
import me.opkarol.opc.api.database.mysql.types.MySqlAttribute;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class Objects {
    private final List<Object> objectList = new ArrayList<>();
    private Object identificationObject;
    private Object uuidObject;
    private Method setIdentification;
    private Object comparableObject;
    private Constructor<?> constructor;

    public List<Object> getObjectList() {
        return objectList;
    }

    public Objects add(@NotNull Object object) {
        object.addAttribute(MySqlAttribute.NOTNULL);
        Value value = object.getField().getAnnotation(Value.class);
        switch (value.value()) {
            case IDENTIFICATION_OBJECT -> {
                object.addAttribute(MySqlAttribute.PRIMARY);
                identificationObject = object;
            }
            case PRIMARY -> object.addAttribute(MySqlAttribute.PRIMARY);
            case EMPTY -> object.addAttribute(MySqlAttribute.IGNORE_IN_SEARCH);
            case UUID_OBJECT -> {
                object.addAttribute(MySqlAttribute.IGNORE_IN_ALL_SEARCH);
                uuidObject = object;
            }
            case COMPARABLE_OBJECT -> {
                object.addAttribute(MySqlAttribute.IGNORE_IN_SEARCH);
                comparableObject = object;
            }
        }
        int constructorParameter = value.parameter();
        if (constructorParameter != -1) {
            object.setConstructorParameter(constructorParameter);
        }
        objectList.add(object);
        return this;
    }

    public Optional<Object> getParameter(int parameter) {
        return getObjectList().stream().filter(object -> object.getConstructorParameter() == parameter).findAny();
    }

    public Objects(Object... objects) {
        objectList.addAll(Arrays.stream(objects).toList());
    }

    public Objects(List<Object> objects) {
        objectList.addAll(objects);
    }

    public Optional<Object> getPrimary() {
        return objectList.stream().filter(Object::isPrimary)
                .findFirst();
    }

    public Object getIdentificationObject() {
        return identificationObject;
    }

    public Object getUUIDObject() {
        return uuidObject;
    }

    public Method getIdentification() {
        return setIdentification;
    }

    public void setIdentificationObject(Method setIdentification) {
        this.setIdentification = setIdentification;
    }

    public Constructor<?> getConstructor() {
        return constructor;
    }

    public void setConstructor(Constructor<?> constructor) {
        this.constructor = constructor;
    }

    public Object getComparableObject() {
        return comparableObject;
    }

    public java.lang.Object[] getObjects(OpMResultSet set) {
        final java.lang.Object[][] objects = new java.lang.Object[1][1];
            objects[0] = new java.lang.Object[constructor.getParameterCount()];
            for (int i = 0; i < constructor.getParameterCount(); i++) {
                int finalI = i;
                getParameter(i).ifPresent(object1 -> {
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
        return objects[0];
    }
}
