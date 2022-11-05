package me.opkarol.opc.api.database.mysql.reflection.symbols;

import me.opkarol.opc.api.database.mysql.reflection.types.OpMObjectValues;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used for assigning identification method to mysql database object.
 * <p>
 * The annotation is used on fields in a class to specify the value of that field in terms of the OpMObjectValues enum.
 * The parameter attribute is used to specify a specific value for the field, if needed
 */
@Target(value = ElementType.FIELD)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface Value {
    OpMObjectValues value() default OpMObjectValues.EMPTY;

    int parameter() default -1;
}
