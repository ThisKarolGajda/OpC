package me.opkarol.opc.api.database.mysql.reflection;

import me.opkarol.opc.api.database.mysql.types.MySqlAttribute;
import me.opkarol.opc.api.database.mysql.types.MySqlVariable;
import me.opkarol.opc.api.database.mysql.types.MySqlVariableType;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public class Object {
    private final String name;
    private final Field object;
    private final MySqlVariableType type;
    private final MySqlAttribute[] attributes;

    public Object(String name, Field object,
                  MySqlVariableType type,
                  MySqlAttribute... attributes) {
        this.name = name;
        this.object = object;
        this.type = type;
        this.attributes = attributes;
    }

    public String toName() {
        return String.format("`%s`", name);
    }

    @Contract(" -> new")
    public @NotNull
    MySqlVariable getVariable() {
        return new MySqlVariable(name, type);
    }

    public List<MySqlAttribute> getTypes() {
        return Arrays.stream(attributes).toList();
    }

    public boolean isPrimary() {
        return getTypes().contains(MySqlAttribute.PRIMARY);
    }

    public boolean isNotNull() {
        return getTypes().contains(MySqlAttribute.NOTNULL);
    }

    public boolean isNotIgnoredInSearch() {
        return !getTypes().contains(MySqlAttribute.IGNORE_IN_SEARCH);
    }

    public String name() {
        return name;
    }

    public Field object() {
        return object;
    }

    public MySqlVariableType type() {
        return type;
    }

    public MySqlAttribute[] attributes() {
        return attributes;
    }

    public <O> java.lang.Object getObject(O main) {
        return Utils.get(object, main);
    }
}