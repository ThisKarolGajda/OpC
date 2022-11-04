package me.opkarol.opc.api.database.mysql.reflection.symbols;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value = ElementType.CONSTRUCTOR)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface Constructor {
}
