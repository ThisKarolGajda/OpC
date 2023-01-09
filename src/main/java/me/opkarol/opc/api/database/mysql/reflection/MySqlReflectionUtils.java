package me.opkarol.opc.api.database.mysql.reflection;

import me.opkarol.opc.api.database.mysql.reflection.objects.MySqlObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class MySqlReflectionUtils {

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

    public static <O> @Nullable O invokeMethod(@NotNull Method method, O object, MySqlObject... args) {
        try {
            method.invoke(object, args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <O> @Nullable O invokeMethod(@NotNull Method method, O object, int... i) {
        try {
            method.invoke(object, i);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void invokeSafeMethod(@NotNull Method method, @NotNull java.lang.Object object, @NotNull java.lang.Object arg) {
        try {
            object.getClass().getMethod(method.getName(), arg.getClass()).invoke(object, arg);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public static @Nullable Constructor<?> getConstructor(@NotNull Class<?> clazz, Class<?>... args) {
        try {
            return clazz.getDeclaredConstructor(args);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static @Nullable Constructor<?> getConstructor(Class<?> classObject, java.lang.Object... args) {
        if (args == null || args.length == 0) {
            return MySqlReflectionUtils.getConstructor(classObject);
        }
        Class<?>[] argTypes = new Class<?>[args.length];
        for ( int i = 0; i < args.length; ++i)
        {
            if (args[i] == null) {
                argTypes[i] = MySqlObject.class;
            } else {
                argTypes[i] = args[i].getClass();
            }
        }
        return MySqlReflectionUtils.getConstructor(classObject, argTypes);
    }

    public static <T> @Nullable T invokeSafeConstructor(Class<?> clazz, java.lang.Object... objects) {
        try {
            return (T) getConstructor(clazz, objects).newInstance(objects);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException ignore) {}
        return null;
    }
}
