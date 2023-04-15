package me.opkarol.opc.api.database.mysql.reflection.symbols.rework;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(value = ElementType.CONSTRUCTOR)

public @interface SqlConstructor {
    String table();
}
