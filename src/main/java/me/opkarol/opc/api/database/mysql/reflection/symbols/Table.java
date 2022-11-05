package me.opkarol.opc.api.database.mysql.reflection.symbols;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used for assigning table to mysql database object.
 * <p>
 * The name() abstract method contains the table name, which will be going tested and used in mysql process.
 */
@Target(value = ElementType.TYPE)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface Table {
    /**
     * Table name.
     *
     * @return the string that represents table generated later-on in mysql process
     */
    String name();
}
