package me.opkarol.opc.api.utils;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectionUtil {

    public static <M> @NotNull Field getField(@NotNull M main, String fieldName) {
        try {
            return main.getClass().getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    public static <M> @NotNull Field getAccessibleField(@NotNull M main, String fieldName)  {
        Field field = getField(main, fieldName);
        field.setAccessible(true);
        return field;
    }

    public static <M> void set(M main, String fieldName, Object toSet) {
        try {
            getAccessibleField(main, fieldName).set(main, toSet);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static <M> @NotNull Method getStringMethod(@NotNull M main, String method) {
        try {
            return main.getClass().getDeclaredMethod(method, String.class);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public static <M> @NotNull Method getAccessibleStringMethod(M main, String method) {
        Method method1 = getStringMethod(main, method);
        method1.setAccessible(true);
        return method1;
    }

    public static <M> void invokeAccessibleStringMethod(M main, String method, Object... args) {
        try {
            getAccessibleStringMethod(main, method).invoke(main, args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public static <M> Object getObject(M main, String fieldName) {
        try {
            return getAccessibleField(main, fieldName).get(main);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static <M> @NotNull M getInstance(@NotNull Class<M> clazz) {
        try {
            return clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static <M> @NotNull Constructor<M> getConstructor(@NotNull Class<M> clazz, Class<?>... args) {
        try {
            return clazz.getDeclaredConstructor(args);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public static <M> @NotNull M getInstance(@NotNull Constructor<M> constructor, Object... args) {
        try {
            return constructor.newInstance(args);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

}
