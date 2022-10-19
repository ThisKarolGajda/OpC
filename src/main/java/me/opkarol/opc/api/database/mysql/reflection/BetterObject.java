package me.opkarol.opc.api.database.mysql.reflection;

import java.lang.reflect.Field;

public class BetterObject extends Object {
    public BetterObject(Field field) {
        super(field.getName(), field, OpMType.switchMySqlType(field));
    }
}
