package me.opkarol.opc.api.database.mysql.v2;

import me.opkarol.opc.api.database.mysql.v3.MySqlObject;

import java.util.ArrayList;
import java.util.List;

public class MySqlIdentification {
    private final List<MySqlObject> identifications;

    public MySqlIdentification(List<MySqlObject> list) {
        this.identifications = list;
    }

    public MySqlIdentification() {
        this.identifications = new ArrayList<>();
    }

    public List<MySqlObject> getIdentifications() {
        return identifications;
    }
}
