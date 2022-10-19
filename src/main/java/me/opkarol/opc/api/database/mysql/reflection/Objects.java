package me.opkarol.opc.api.database.mysql.reflection;

import me.opkarol.opc.api.database.mysql.types.MySqlAttribute;
import me.opkarol.opc.api.database.mysql.types.MySqlVariableType;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Objects {
    private final List<Object> objectList = new ArrayList<>();
    private Object identificationObject;
    private Object uuidObject;
    private Method setIdentification;

    public List<Object> getObjectList() {
        return objectList;
    }

    public Objects add(Object object) {
        objectList.add(object);
        if (object.object().isAnnotationPresent(OpMIdentificationObject.class)) {
            identificationObject = object;
        }
        if (object.object().isAnnotationPresent(OpMUUIDObject.class)) {
            uuidObject = object;
        }
        return this;
    }

    public Objects addNotNullInt(String name, Field object) {
        return add(new Object(name, object, MySqlVariableType.INT, MySqlAttribute.NOTNULL, MySqlAttribute.IGNORE_IN_SEARCH));
    }

    public Objects addPrimaryNotNullInt(String name, Field object) {
        return add(new Object(name, object, MySqlVariableType.INT, MySqlAttribute.NOTNULL, MySqlAttribute.PRIMARY));
    }

    public Objects addNotNullText(String name, Field object) {
        return add(new Object(name, object, MySqlVariableType.TEXT, MySqlAttribute.NOTNULL, MySqlAttribute.IGNORE_IN_SEARCH));
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
}
