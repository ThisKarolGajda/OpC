package me.opkarol.opc.api.database.mysql.reflection.objects;

import me.opkarol.opc.api.database.mysql.reflection.MySqlReflectionUtils;
import me.opkarol.opc.api.database.mysql.types.MySqlAttribute;
import me.opkarol.opc.api.database.mysql.types.MySqlVariable;
import me.opkarol.opc.api.database.mysql.types.MySqlVariableType;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class MySqlObject {
    private final String name;
    private final Field object;
    private final MySqlVariableType type;
    private final List<MySqlAttribute> attributes;
    private int constructorParameter;

    public MySqlObject(String name, Field object,
                       MySqlVariableType type,
                       MySqlAttribute... attributes) {
        this.name = name;
        this.object = object;
        this.type = type;
        this.attributes = new ArrayList<>(List.of(attributes));
    }

    public String toName() {
        return String.format("`%s`", name);
    }

    @Contract(" -> new")
    public @NotNull MySqlVariable getVariable() {
        return new MySqlVariable(name, type);
    }

    public List<MySqlAttribute> getTypes() {
        return attributes;
    }

    public boolean isPrimary() {
        return getTypes().contains(MySqlAttribute.PRIMARY);
    }

    public boolean isNotNull() {
        return getTypes().contains(MySqlAttribute.NOTNULL);
    }

    public boolean isNotIgnoredInSearch() {
        return !getTypes().contains(MySqlAttribute.IGNORE_IN_SEARCH) && !getTypes().contains(MySqlAttribute.IGNORE_IN_ALL_SEARCH);
    }

    public String getName() {
        return name;
    }

    public Field getField() {
        return object;
    }

    public MySqlVariableType getType() {
        return type;
    }

    public MySqlAttribute[] getAttributes() {
        return attributes.toArray(new MySqlAttribute[0]);
    }

    public <O> java.lang.Object getObject(O main) {
        return MySqlReflectionUtils.get(object, main);
    }

    public void addAttribute(MySqlAttribute attribute) {
        attributes.add(attribute);
    }

    public List<Annotation> getFieldAnnotations() {
        return new ArrayList<>(List.of(getField().getAnnotations()));
    }

    public int getConstructorParameter() {
        return constructorParameter;
    }

    public void setConstructorParameter(int constructorParameter) {
        this.constructorParameter = constructorParameter;
    }
}