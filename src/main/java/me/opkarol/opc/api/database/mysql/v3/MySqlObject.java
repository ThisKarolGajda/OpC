package me.opkarol.opc.api.database.mysql.v3;

import me.opkarol.opc.api.database.mysql.MySqlAttribute;
import me.opkarol.opc.api.database.mysql.MySqlVariable;
import me.opkarol.opc.api.database.mysql.MySqlVariableType;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public record MySqlObject(String name, Function<Object, Object> object, MySqlVariableType type, MySqlAttribute... attributes) {
    public String toName() {
        return String.format("`%s`", name);
    }

    @Contract(" -> new")
    public @NotNull MySqlVariable getVariable() {
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

}