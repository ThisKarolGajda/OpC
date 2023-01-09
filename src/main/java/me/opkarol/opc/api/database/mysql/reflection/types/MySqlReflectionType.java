package me.opkarol.opc.api.database.mysql.reflection.types;

import me.opkarol.opc.api.database.mysql.types.MySqlVariableType;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;

public final class MySqlReflectionType {

    public static MySqlVariableType switchMySqlType(@NotNull Class<?> clazz) {
        switch (clazz.getSimpleName()) {
            case "Integer", "int" -> {
                return MySqlVariableType.INT;
            }
            case "String", "UUID" -> {
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
