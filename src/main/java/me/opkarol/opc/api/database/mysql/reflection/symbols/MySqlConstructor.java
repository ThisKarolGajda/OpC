package me.opkarol.opc.api.database.mysql.reflection.symbols;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used for assigning constructor method to mysql database object.
 * <p>
 * This annotation should be only attached to one constructor, and it`s objects should go in the same order as the value assigning in the mysql database class.
 */
@Target(value = ElementType.CONSTRUCTOR)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface MySqlConstructor {
}
