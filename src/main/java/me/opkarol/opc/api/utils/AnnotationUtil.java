package me.opkarol.opc.api.utils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;

public class AnnotationUtil {

    public static <T extends Annotation> @Nullable T getAnnotationObject(@NotNull Class<?> clazz, Class<? extends T> annotation) {
        if (!clazz.isAnnotationPresent(annotation)) {
            return null;
        }

        return clazz.getDeclaredAnnotation(annotation);
    }
}
