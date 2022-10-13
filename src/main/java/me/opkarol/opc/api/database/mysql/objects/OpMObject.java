package me.opkarol.opc.api.database.mysql.objects;

import me.opkarol.opc.api.database.mysql.types.MySqlAttribute;
import me.opkarol.opc.api.database.mysql.types.MySqlVariable;
import me.opkarol.opc.api.database.mysql.types.MySqlVariableType;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public record OpMObject<O>(String name, Function<O, Object> object,
                           MySqlVariableType type,
                           MySqlAttribute... attributes) {

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
}