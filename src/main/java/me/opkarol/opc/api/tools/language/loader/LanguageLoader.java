package me.opkarol.opc.api.tools.language.loader;

import me.opkarol.opc.api.map.OpMap;
import me.opkarol.opc.api.tools.language.database.LanguageDatabase;
import me.opkarol.opc.api.tools.language.types.LanguageValue;
import me.opkarol.opc.api.tools.language.types.LanguageVariable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.reflections.Reflections;
import org.reflections.scanners.FieldAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Set;

public class LanguageLoader implements Serializable {
    private final LanguageDatabase language;

    public LanguageLoader(LanguageDatabase language) {
        this.language = language;
    }

    public @NotNull OpMap<String, Object> loadConfig(String directory) {
        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setScanners(new FieldAnnotationsScanner())
                .setUrls(ClasspathHelper.forPackage(directory)));

        Set<Field> fields = reflections.getFieldsAnnotatedWith(LanguageValue.class);

        OpMap<String, Object> configValues = new OpMap<>();

        for (Field field : fields) {
            LanguageValue annotation = field.getAnnotation(LanguageValue.class);
            Class<?> declaringClass = field.getDeclaringClass();
            Object object = null;

            if (field.getType().equals(LanguageVariable.class)) {
                object = getLanguageVariable(annotation);
                setField(field, object);
            } else if (field.getType().equals(String.class)) {
                object = annotation.defaultValue();
                setField(field, object);
            }

            configValues.put(declaringClass.getName() + "." + annotation.path(), object);
        }

        return configValues;
    }

    @Contract(pure = true)
    private @NotNull LanguageVariable getLanguageVariable(@NotNull LanguageValue annotation) {
        return new LanguageVariable(annotation.path(), annotation.defaultValue(), language);
    }

    private static void setField(Field field, Object value) {
        try {
            field.setAccessible(true);
            // We can only get static values, because we don't have the class object
            field.set(null, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
