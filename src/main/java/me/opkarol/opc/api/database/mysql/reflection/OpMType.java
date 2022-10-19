package me.opkarol.opc.api.database.mysql.reflection;

import me.opkarol.opc.api.database.mysql.types.MySqlVariableType;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;

public final class OpMType {

    public static MySqlVariableType switchMySqlType(@NotNull Class<?> clazz) {
        switch (clazz.getSimpleName()) {
            case "Integer", "int" -> {
                return MySqlVariableType.INT;
            }
            case "String" -> {
                return MySqlVariableType.TEXT;
            }
            case "Float", "float" -> {
                return MySqlVariableType.FLOAT;
            }
            case "Double", "double" -> {
                return MySqlVariableType.DOUBLE;
            }
            case "boolean", "Boolean" -> {
                return MySqlVariableType.BOOLEAN;
            }
            default -> {
                return MySqlVariableType.JSON;
            }
        }
    }

    public static MySqlVariableType switchMySqlType(@NotNull Field field) {
        return switchMySqlType(field.getType());
    }

}
