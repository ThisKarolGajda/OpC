package me.opkarol.opc.api.database.mysql.reflection;

import me.opkarol.opc.OpAPI;
import me.opkarol.opc.api.utils.VariableUtil;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class OpMReflection<O> {
    private final O object;
    private final Class<?> classObject;
    private OpMTable table;
    private final Objects objects = new Objects();
    private final List<Field> list;

    public OpMReflection(@NotNull O object) {
        this.object = object;
        this.classObject = object.getClass();
        setTableName();
        list = buildFields();

        for (Field field : list) {
            objects.add(new BetterObject(field));
        }

        getIdentificationMethod().ifPresent(objects::setIdentificationObject);

        for (Object object1 : objects.getObjectList()) {
            OpAPI.logInfo(object1.toName() + " -- " + object1.type() + " -- " + Arrays.toString(object1.attributes()));
            OpAPI.logInfo(object1.getObject(object).toString());
        }
    }

    private @NotNull Optional<Method> getIdentificationMethod() {
        return Utils.getMethods(classObject, method -> method.isAnnotationPresent(OpMIdentification.class)).stream().findAny();
    }

    private void setTableName() {
        Utils.getAnnotations(classObject).stream().filter(a -> a.annotationType().equals(OpMTable.class)).findAny().ifPresent(a -> table = (OpMTable) a);
    }

    private List<Field> buildFields() {
        return Utils.getFields(classObject, field -> {
            OpAPI.logInfo(field.getName() + " -- " + field.toString() + " -- " + Utils.isAnnotationPresent(field.getClass(), OpMValue.class)) ;
            OpAPI.logInfo((field.getAnnotation(OpMValue.class) == null) + " ---");
            OpAPI.logInfo(Arrays.toString(field.getAnnotations()));
            OpAPI.logInfo(field.getType().getSimpleName());
            OpAPI.logInfo(OpMType.switchMySqlType(field).toString());
            OpAPI.logInfo(Utils.get(field, object).toString());
            return Utils.isAnnotationPresent(field.getClass(), OpMValue.class);
        });
    }

    public O getObject() {
        return object;
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