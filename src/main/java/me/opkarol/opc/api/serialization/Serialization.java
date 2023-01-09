package me.opkarol.opc.api.serialization;

import com.google.common.base.Preconditions;
import me.opkarol.opc.api.map.OpMap;
import me.opkarol.opc.api.map.OpMapBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.logging.Level;
import java.util.logging.Logger;

public record Serialization(
        Class<? extends Serializable> clazz) {
    public static final String SERIALIZED_TYPE_KEY = "--";
    private static final OpMap<String, Class<? extends Serializable>> aliases = new OpMap<>();

    public Serialization(@NotNull Class<? extends Serializable> clazz) {
        this.clazz = clazz;
    }

    @Nullable
    private Method getMethod(@NotNull String name) {
        try {
            Method method = clazz.getDeclaredMethod(name, OpMap.class);

            if (!Serializable.class.isAssignableFrom(method.getReturnType())) {
                return null;
            }
            if (!Modifier.isStatic(method.getModifiers())) {
                return null;
            }

            return method;
        } catch (NoSuchMethodException | SecurityException ex) {
            return null;
        }
    }

    private @Nullable Constructor<? extends Serializable> getConstructor() {
        try {
            return clazz.getConstructor(OpMapBuilder.class);
        } catch (NoSuchMethodException | SecurityException ex) {
            try {
                return clazz.getConstructor(OpMap.class);
            } catch (NoSuchMethodException | SecurityException e) {
                return null;
            }
        }
    }

    @Nullable
    private Serializable deserializeViaMethod(@NotNull Method method, @NotNull OpMap<String, ?> args) {
        try {
            Serializable result = (Serializable) method.invoke(null, args);

            if (result == null) {
                Logger.getLogger(Serialization.class.getName()).log(Level.SEVERE, "Could not call method '" + method + "' of " + clazz + " for deserialization: method returned null");
            } else {
                return result;
            }
        } catch (Throwable ex) {
            Logger.getLogger(Serialization.class.getName()).log(
                    Level.SEVERE,
                    "Could not call method '" + method + "' of " + clazz + " for deserialization",
                    ex instanceof InvocationTargetException ? ex.getCause() : ex);
        }

        return null;
    }

    @Nullable
    private Serializable deserializeViaConstructor(@NotNull Constructor<? extends Serializable> constructor, @NotNull OpMapBuilder<String, ?> args) {
        try {
            return constructor.newInstance(args);
        } catch (Throwable ex) {
            Logger.getLogger(Serialization.class.getName()).log(
                    Level.SEVERE,
                    "Could not call constructor '" + constructor + "' of " + clazz + " for deserialization",
                    ex instanceof InvocationTargetException ? ex.getCause() : ex);
        }

        return null;
    }

    @Nullable
    public Serializable deserialize(OpMapBuilder<String, ?> args) {
        Preconditions.checkArgument(args != null, "Args must not be null");

        Serializable result = null;
        Method method;

        method = getMethod("deserialize");

        if (method != null) {
            result = deserializeViaMethod(method, args);
        }

        if (result == null) {
            method = getMethod("valueOf");

            if (method != null) {
                result = deserializeViaMethod(method, args);
            }
        }

        if (result == null) {
            Constructor<? extends Serializable> constructor = getConstructor();

            if (constructor != null) {
                result = deserializeViaConstructor(constructor, args);
            }
        }


        return result;
    }

    @Nullable
    public static Serializable deserializeObject(@NotNull OpMapBuilder<String, ?> args, @NotNull Class<? extends Serializable> clazz) {
        return new Serialization(clazz).deserialize(args);
    }

    @Nullable
    public static Serializable deserializeObject(@NotNull OpMapBuilder<String, ?> args) {
        Class<? extends Serializable> clazz;

        if (!args.containsKey(SERIALIZED_TYPE_KEY)) {
            throw new IllegalArgumentException("Args doesn't contain type key ('" + SERIALIZED_TYPE_KEY + "')");
        }

        try {
            String alias = (String) args.unsafeGet(SERIALIZED_TYPE_KEY);

            if (alias == null) {
                throw new IllegalArgumentException("Cannot have null alias");
            }
            clazz = getClassByAlias(alias);
            if (clazz == null) {
                throw new IllegalArgumentException("Specified class does not exist ('" + alias + "')");
            }
        } catch (ClassCastException ex) {
            ex.fillInStackTrace();
            throw ex;
        }

        return new Serialization(clazz).deserialize(args);
    }

    public static void registerClass(@NotNull Class<? extends Serializable> clazz) {
        TransformSerialization transform = clazz.getAnnotation(TransformSerialization.class);

        if (transform == null) {
            registerClass(clazz, getAlias(clazz));
            registerClass(clazz, clazz.getName());
        }
    }

    public static void registerClass(@NotNull Class<? extends Serializable> clazz, @NotNull String alias) {
        aliases.put(alias, clazz);
    }

    public static void unregisterClass(@NotNull String alias) {
        aliases.remove(alias);
    }

    public static void unregisterClass(@NotNull Class<? extends Serializable> clazz) {
        aliases.getValues().remove(clazz);
    }

    @Nullable
    public static Class<? extends Serializable> getClassByAlias(@NotNull String alias) {
        return aliases.unsafeGet(alias);
    }

    @NotNull
    public static String getAlias(@NotNull Class<? extends Serializable> clazz) {
        TransformSerialization delegate = clazz.getAnnotation(TransformSerialization.class);

        if (delegate != null) {
            if (delegate.value() != clazz) {
                return getAlias(delegate.value());
            }
        }

        SerializableName alias = clazz.getAnnotation(SerializableName.class);

        if (alias != null) {
            return alias.value();
        }

        return clazz.getName();
    }

    public static @Nullable String getAlias(String alias) {
        try {
            return getAlias((Class<? extends Serializable>) Class.forName(alias));
        } catch (Throwable ex) {
            Logger.getLogger(Serialization.class.getName()).log(
                    Level.SEVERE,
                    "Specified class does not exist ('" + alias + "')",
                    ex);
        }
        return null;
    }
}
