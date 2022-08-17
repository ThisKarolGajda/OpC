package me.opkarol.opc.api.utils;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

@Deprecated
public class LoaderUtil {
    private static final List<String> blockedURLs = Arrays.asList("META-INF", "javassist", "javax", "org.intellij", "org.slf4j", "org.reflections", "org.jetbrains");

    public static @NotNull Set<String> getClassNamesFromJarFile(File givenFile) {
        Set<String> classNames = new HashSet<>();
        try (JarFile jarFile = new JarFile(givenFile)) {
            Enumeration<JarEntry> e = jarFile.entries();
            while (e.hasMoreElements()) {
                JarEntry jarEntry = e.nextElement();

                if (jarEntry.getName().endsWith(".class")) {
                    String className = jarEntry.getName()
                            .replace("/", ".")
                            .replace(".class", "");

                    if (blockedURLs.stream().noneMatch(className::contains)) {
                        classNames.add(className);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return classNames;
    }
}
