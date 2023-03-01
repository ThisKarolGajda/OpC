package me.opkarol.opc.api.database.mysql.reflection.objects;

import me.opkarol.opc.api.database.mysql.reflection.symbols.MySqlValue;
import me.opkarol.opc.api.database.mysql.reflection.types.MySqlObjectValues;
import me.opkarol.opc.api.database.mysql.resultset.MySqlResultSet;
import me.opkarol.opc.api.database.mysql.types.MySqlAttribute;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Function;

import static me.opkarol.opc.api.database.mysql.reflection.types.MySqlObjectValues.*;

public class MySqlObjects {
    private final List<MySqlObject> objectList = new ArrayList<>();
    private MySqlObject identificationObject;
    private MySqlObject uuidObject;
    private Method setIdentification;
    private MySqlObject comparableObject;
    private Constructor<?> constructor;

    public MySqlObjects(MySqlObject... objects) {
        objectList.addAll(Arrays.stream(objects).toList());
    }

    public MySqlObjects(List<MySqlObject> objects) {
        objectList.addAll(objects);
    }

    public List<MySqlObject> getObjectList() {
        return objectList;
    }

    public MySqlObjects add(@NotNull MySqlObject object) {
        object.addAttribute(MySqlAttribute.NOTNULL);
        MySqlValue value = object.getField().getAnnotation(MySqlValue.class);
        List<MySqlObjectValues> list = Arrays.stream(value.value()).toList();
        if (list.contains(IDENTIFICATION_OBJECT)) {
            object.addAttribute(MySqlAttribute.PRIMARY);
            identificationObject = object;
        }
        if (list.contains(PRIMARY)) {
            object.addAttribute(MySqlAttribute.PRIMARY);
        }
        if (list.contains(EMPTY)) {
            object.addAttribute(MySqlAttribute.IGNORE_IN_SEARCH);
        }
        if (list.contains(UUID_OBJECT)) {
            object.addAttribute(MySqlAttribute.IGNORE_IN_ALL_SEARCH);
            uuidObject = object;
        }
        if (list.contains(COMPARABLE_OBJECT)) {
            object.addAttribute(MySqlAttribute.IGNORE_IN_SEARCH);
            comparableObject = object;
        }
        int constructorParameter = value.parameter();
        if (constructorParameter != -1) {
            object.setConstructorParameter(constructorParameter);
        }
        objectList.add(object);
        return this;
    }

    public Optional<MySqlObject> getParameter(int parameter) {
        return getObjectList().stream().filter(object -> object.getConstructorParameter() == parameter).findAny();
    }

    public Optional<MySqlObject> getPrimary() {
        return objectList.stream().filter(MySqlObject::isPrimary)
                .findFirst();
    }

    public MySqlObject getIdentificationObject() {
        return identificationObject;
    }

    public void setIdentificationObject(Method setIdentification) {
        this.setIdentification = setIdentification;
    }

    public MySqlObject getUUIDObject() {
        return uuidObject;
    }

    public Method getIdentification() {
        return setIdentification;
    }

    public Constructor<?> getConstructor() {
        return constructor;
    }

    public void setConstructor(Constructor<?> constructor) {
        this.constructor = constructor;
    }

    public MySqlObject getComparableObject() {
        return comparableObject;
    }

    public java.lang.Object[] getObjects(MySqlResultSet set) {
        final java.lang.Object[][] objects = new java.lang.Object[1][1];
        objects[0] = new java.lang.Object[constructor.getParameterCount()];
        for (int i = 0; i < constructor.getParameterCount(); i++) {
            int finalI = i;
            getParameter(i).ifPresent(object1 -> {
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
        return objects[0];
    }
}
