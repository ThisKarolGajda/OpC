package me.opkarol.opc.api.database.mysql.reflection.objects;

import me.opkarol.opc.api.database.mysql.reflection.types.SqlObjectValues;
import me.opkarol.opc.api.database.mysql.resultset.SqlResultSet;
import me.opkarol.opc.api.database.mysql.types.SqlAttribute;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static me.opkarol.opc.api.database.mysql.reflection.types.SqlObjectValues.*;

public class SqlReflectionObjects {
    private final List<SqlObject> objectList = new ArrayList<>();
    private SqlObject identificationObject;
    private Method setIdentification;
    private SqlObject comparableObject;
    private Constructor<?> constructor;

    public SqlReflectionObjects(SqlObject... objects) {
        objectList.addAll(Arrays.stream(objects).toList());
    }

    public SqlReflectionObjects(List<SqlObject> objects) {
        objectList.addAll(objects);
    }

    public List<SqlObject> getObjectList() {
        return objectList;
    }

    public SqlReflectionObjects add(@NotNull SqlObject object, int parameter, SqlObjectValues[] values) {
        object.addAttribute(SqlAttribute.NOTNULL);
        List<SqlObjectValues> list = Arrays.stream(values).toList();
        if (list.contains(IDENTIFICATION)) {
            object.addAttribute(SqlAttribute.PRIMARY);
            identificationObject = object;
        }
        if (list.contains(PRIMARY)) {
            object.addAttribute(SqlAttribute.PRIMARY);
        }
        if (list.contains(EMPTY)) {
            object.addAttribute(SqlAttribute.IGNORE_IN_INSERT_SEARCH);
        }
        if (list.contains(COMPARE)) {
            object.addAttribute(SqlAttribute.IGNORE_IN_INSERT_SEARCH);
            comparableObject = object;
        }
        object.setConstructorParameter(parameter);
        objectList.add(object);
        return this;
    }

    public Optional<SqlObject> getParameter(int parameter) {
        return getObjectList().stream()
                .filter(object -> object.getConstructorParameter() == parameter)
                .findAny();
    }

    public Optional<SqlObject> getPrimary() {
        return objectList.stream().filter(SqlObject::isPrimary)
                .findFirst();
    }

    public SqlObject getIdentificationObject() {
        return identificationObject;
    }

    public void setIdentificationObject(Method setIdentification) {
        this.setIdentification = setIdentification;
    }

    public Method getIdentificationSetter() {
        return setIdentification;
    }

    public Constructor<?> getConstructor() {
        return constructor;
    }

    public void setConstructor(Constructor<?> constructor) {
        this.constructor = constructor;
    }

    public SqlObject getComparableObject() {
        return comparableObject;
    }

    public java.lang.Object[] getObjects(SqlResultSet set) {
        final java.lang.Object[][] objects = new java.lang.Object[1][1];
        objects[0] = new java.lang.Object[constructor.getParameterCount()];
        for (int i = 0; i < constructor.getParameterCount(); i++) {
            int finalI = i;
            getParameter(i).ifPresent(object1 -> {
                Class<?> parameterType = constructor.getParameterTypes()[finalI];
                switch (parameterType.getSimpleName()) {
                    case "UUID" -> objects[0][finalI] = set.getUUID(object1.getName());
                    case "String" -> objects[0][finalI] = set.getText(object1.getName());
                    default -> objects[0][finalI] = set.getObject(object1.getName());
                }
            });
        }
        return objects[0];
    }
}
