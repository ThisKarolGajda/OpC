package me.opkarol.opc.api.database.mysql.reflection.objects;

import me.opkarol.opc.api.database.mysql.reflection.types.OpMType;

import java.lang.reflect.Field;

public class BetterObject extends Object {
    public BetterObject(Field field) {
        super(field.getName(), field, OpMType.switchMySqlType(field));
    }
}
