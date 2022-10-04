package me.opkarol.opc.api.database.mysql.v3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class MySqlObjects<O> {
    private final List<MySqlObject<O>> objectList = new ArrayList<>();

    public List<MySqlObject<O>> getObjectList() {
        return objectList;
    }

    public MySqlObjects<O> add(MySqlObject<O> object) {
        objectList.add(object);
        return this;
    }

    @SafeVarargs
    public MySqlObjects(MySqlObject<O>... objects) {
        objectList.addAll(Arrays.stream(objects).toList());
    }

    public MySqlObjects(List<MySqlObject<O>> objects) {
        objectList.addAll(objects);
    }

    public Optional<MySqlObject<O>> getPrimary() {
        return objectList.stream().filter(MySqlObject::isPrimary).findFirst();
    }
}
