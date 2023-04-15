package me.opkarol.opc.api.database.mysql.reflection.symbols;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used for assigning identification method to mysql database object.
 * <p>
 * Should be only used if the database doesn't include it`s own identification methods.
 * Method has to have 1 parameter which is integer which later-on sets object id, that is used to know which object is the right one.
 */
@Target(value = ElementType.METHOD)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface SqlIdentificationAnnotation {
}
