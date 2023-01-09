package me.opkarol.opc.api.database.mysql.reflection.objects;

import me.opkarol.opc.api.database.mysql.reflection.types.MySqlReflectionType;

import java.lang.reflect.Field;

public class BetterMySqlObject extends MySqlObject {
    public BetterMySqlObject(Field field) {
        super(field.getName(), field, MySqlReflectionType.switchMySqlType(field));
    }
}
