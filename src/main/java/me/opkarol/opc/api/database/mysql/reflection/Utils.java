package me.opkarol.opc.api.database.mysql.reflection;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Utils {

    public static @NotNull List<Annotation> getFieldAnnotations(@NotNull Class<?> classObject) {
        List<Annotation> list = new ArrayList<>();
        Arrays.stream(classObject.getDeclaredFields())
                .map(AccessibleObject::getAnnotations)
                .map(Arrays::asList)
                .collect(Collectors.toList())
                .forEach(list::addAll);
        return list;
    }

    public static @NotNull List<Annotation> getAnnotations(@NotNull Class<?> classObject) {
        return new ArrayList<>(List.of(classObject.getAnnotations()));
    }

    public static @NotNull List<Method> getMethods(@NotNull Class<?> classObject) {
        return Arrays.stream(classObject.getDeclaredMethods()).collect(Collectors.toList());
    }

    public static List<Method> getMethods(@NotNull Class<?> classObject, Predicate<Method> predicate) {
        return getMethods(classObject).stream().filter(predicate).collect(Collectors.toList());
    }

    public static @NotNull List<Field> getFields(@NotNull Class<?> classObject) {
        return Arrays.stream(classObject.getDeclaredFields()).collect(Collectors.toList());
    }

    public static List<Field> getFields(@NotNull Class<?> classObject, Predicate<Field> predicate) {
        return getFields(classObject).stream().filter(predicate).collect(Collectors.toList());
    }

    public static <A extends Annotation> boolean isAnnotationPresent(@NotNull Class<?> classObject, Class<A> equals) {
        return classObject.getAnnotation(equals) == null;
    }

    public static <A> java.lang.Object get(@NotNull Field field, A object) {
        field.setAccessible(true);
        try {
            return field.get(object);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return object;
    }

    public static void invokeMethod(@NotNull Method method, Object object, Object... args) {
        try {
            method.invoke(object, args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Contract(pure = true)
    public static <O> @NotNull O invokeMethod(Method method, O object, int i) {
        return invokeMethod(method, object, i);
    }
}
