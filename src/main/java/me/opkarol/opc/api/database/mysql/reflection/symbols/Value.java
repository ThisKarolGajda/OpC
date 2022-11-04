package me.opkarol.opc.api.database.mysql.reflection.symbols;

import me.opkarol.opc.api.database.mysql.reflection.types.OpMObjectValues;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value = ElementType.FIELD)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface Value {
    OpMObjectValues value() default OpMObjectValues.EMPTY;

    int parameter() default -1;
}
