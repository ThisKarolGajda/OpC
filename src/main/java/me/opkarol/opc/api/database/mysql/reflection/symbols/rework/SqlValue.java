package me.opkarol.opc.api.database.mysql.reflection.symbols.rework;

import me.opkarol.opc.api.database.mysql.reflection.types.SqlObjectValues;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(value = ElementType.PARAMETER)

public @interface SqlValue {
    String name();

    SqlObjectValues[] value() default {SqlObjectValues.EMPTY};
}
