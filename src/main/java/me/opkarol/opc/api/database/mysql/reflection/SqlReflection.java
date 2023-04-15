package me.opkarol.opc.api.database.mysql.reflection;

import me.opkarol.opc.api.database.mysql.reflection.objects.SqlObject;
import me.opkarol.opc.api.database.mysql.reflection.objects.SqlReflectionObjects;
import me.opkarol.opc.api.database.mysql.reflection.symbols.SqlIdentificationAnnotation;
import me.opkarol.opc.api.database.mysql.reflection.symbols.rework.SqlConstructor;
import me.opkarol.opc.api.database.mysql.reflection.symbols.rework.SqlValue;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class SqlReflection {
    private final Class<?> classObject;
    private final SqlReflectionObjects objects = new SqlReflectionObjects();

    public SqlReflection(Class<?> object) {
        this.classObject = object;
        getConstructor().ifPresent(objects::setConstructor);

        getConstructor().ifPresent(constructor -> {
            for (int i = 0; i < constructor.getParameterCount(); i++) {
                Annotation[] annotations = constructor.getParameterAnnotations()[i];
                SqlValue sqlValue = (SqlValue) Arrays.stream(annotations)
                        .filter(annotation -> annotation.annotationType().equals(SqlValue.class))
                        .findAny().orElseThrow();
                List<Field> fields = SqlReflectionHelper.getFields(object, field -> field.getName().equals(sqlValue.name()));
                if (fields.size() == 1) {
                    objects.add(new SqlObject(fields.get(0)), i, sqlValue.value());
                }
            }
        });

        getIdentificationMethod().ifPresent(objects::setIdentificationObject);
    }

    private @NotNull Optional<Method> getIdentificationMethod() {
        return SqlReflectionHelper.getMethods(classObject, method -> method.isAnnotationPresent(SqlIdentificationAnnotation.class)).stream().findAny();
    }

    private @NotNull Optional<Constructor<?>> getConstructor() {
        return Arrays.stream(classObject.getDeclaredConstructors()).filter(constructor -> constructor.isAnnotationPresent(SqlConstructor.class)).findAny();
    }

    public Class<?> getClassObject() {
        return classObject;
    }


    public String getTableName() {
        return getConstructor().map(constructor -> constructor.getAnnotation(SqlConstructor.class).table()).orElse("table");
    }

    public SqlReflectionObjects getObjects() {
        return objects;
    }
}