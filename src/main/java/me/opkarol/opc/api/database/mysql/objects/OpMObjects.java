package me.opkarol.opc.api.database.mysql.objects;

import me.opkarol.opc.api.database.mysql.types.MySqlAttribute;
import me.opkarol.opc.api.database.mysql.types.MySqlVariableType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class OpMObjects<O> {
    private final List<OpMObject<O>> objectList = new ArrayList<>();

    public List<OpMObject<O>> getObjectList() {
        return objectList;
    }

    public OpMObjects<O> add(OpMObject<O> object) {
        objectList.add(object);
        return this;
    }

    public OpMObjects<O> addNotNullInt(String name, Function<O, Object> object) {
        return add(new OpMObject<>(name, object, MySqlVariableType.INT, MySqlAttribute.NOTNULL));
    }

    public OpMObjects<O> addPrimaryNotNullInt(String name, Function<O, Object> object) {
        return add(new OpMObject<>(name, object, MySqlVariableType.INT, MySqlAttribute.NOTNULL, MySqlAttribute.PRIMARY));
    }

    public OpMObjects<O> addNotNullText(String name, Function<O, Object> object) {
        return add(new OpMObject<>(name, object, MySqlVariableType.TEXT, MySqlAttribute.NOTNULL));
    }

    @SafeVarargs
    public OpMObjects(OpMObject<O>... objects) {
        objectList.addAll(Arrays.stream(objects).toList());
    }

    public OpMObjects(List<OpMObject<O>> objects) {
        objectList.addAll(objects);
    }

    public Optional<OpMObject<O>> getPrimary() {
        return objectList.stream().filter(OpMObject::isPrimary)
                .findFirst();
    }
}
