package me.opkarol.opc.api.database.mysql.reflection.types;

import me.opkarol.opc.api.database.mysql.types.SqlVariableType;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;

public final class SqlReflectionType {

    public static SqlVariableType switchMySqlType(@NotNull Class<?> clazz) {
        switch (clazz.getSimpleName()) {
            case "Integer", "int" -> {
                return SqlVariableType.INT;
            }
            case "String", "UUID" -> {
                return SqlVariableType.TEXT;
            }
            case "Float", "float" -> {
                return SqlVariableType.FLOAT;
            }
            case "Double", "double" -> {
                return SqlVariableType.DOUBLE;
            }
            case "boolean", "Boolean" -> {
                return SqlVariableType.BOOLEAN;
            }
            default -> {
                return SqlVariableType.JSON;
            }
        }
    }

    public static SqlVariableType switchMySqlType(@NotNull Field field) {
        return switchMySqlType(field.getType());
    }

}
