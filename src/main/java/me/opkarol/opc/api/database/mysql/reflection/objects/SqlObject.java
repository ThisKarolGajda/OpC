package me.opkarol.opc.api.database.mysql.reflection.objects;

import me.opkarol.opc.api.database.mysql.reflection.SqlReflectionHelper;
import me.opkarol.opc.api.database.mysql.reflection.types.SqlReflectionType;
import me.opkarol.opc.api.database.mysql.types.SqlAttribute;
import me.opkarol.opc.api.database.mysql.types.SqlVariable;
import me.opkarol.opc.api.database.mysql.types.SqlVariableType;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class SqlObject {
    private final String name;
    private final Field object;
    private final SqlVariableType type;
    private final List<SqlAttribute> attributes;
    private int constructorParameter;

    public SqlObject(String name, Field object,
                     SqlVariableType type,
                     SqlAttribute... attributes) {
        this.name = name;
        this.object = object;
        this.type = type;
        this.attributes = new ArrayList<>(List.of(attributes));
    }

    public SqlObject(Field field) {
        this(field.getName(), field, SqlReflectionType.switchMySqlType(field));
    }

    public String toName() {
        return String.format("`%s`", name);
    }

    @Contract(" -> new")
    public @NotNull SqlVariable getVariable() {
        return new SqlVariable(name, type);
    }

    public List<SqlAttribute> getTypes() {
        return attributes;
    }

    public boolean isPrimary() {
        return getTypes().contains(SqlAttribute.PRIMARY);
    }

    public boolean isNotNull() {
        return getTypes().contains(SqlAttribute.NOTNULL);
    }

    public boolean isNotIgnoredInSearch() {
        return !getTypes().contains(SqlAttribute.IGNORE_IN_INSERT_SEARCH) && !getTypes().contains(SqlAttribute.IGNORE_IN_ALL_SEARCH);
    }

    public String getName() {
        return name;
    }

    public Field getField() {
        return object;
    }

    public SqlVariableType getType() {
        return type;
    }

    public SqlAttribute[] getAttributes() {
        return attributes.toArray(new SqlAttribute[0]);
    }

    public <O> java.lang.Object getObject(O main) {
        return SqlReflectionHelper.get(object, main);
    }

    public void addAttribute(SqlAttribute attribute) {
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