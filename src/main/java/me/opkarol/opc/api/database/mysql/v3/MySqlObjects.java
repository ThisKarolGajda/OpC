package me.opkarol.opc.api.database.mysql.v3;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MySqlObjects {
    private final List<MySqlObject> objectList = new ArrayList<>();

    public List<MySqlObject> getObjectList() {
        return objectList;
    }

    public Optional<MySqlObject> getPrimary() {
        return objectList.stream().filter(MySqlObject::isPrimary).findFirst();
    }
}
