package me.opkarol.opc.api.tools.language.loader;

import me.opkarol.opc.OpAPI;
import me.opkarol.opc.api.tools.language.types.LanguageObject;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

public class LanguageFileLoader implements Serializable {
    public void saveToFile(@NotNull Set<LanguageObject> languageSet, String fileName) {
        fileName = "plugins/" + OpAPI.getPlugin().getName() + "/" + fileName;
        try (PrintWriter writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(fileName), StandardCharsets.UTF_8))) {
            for (LanguageObject language : languageSet) {
                writer.println(language.getPath() + ": " + language.getObject());
            }
            System.out.println("Language set saved to file: " + fileName);
        } catch (IOException e) {
            System.out.println("Error saving language set to file: " + fileName);
            e.printStackTrace();
        }
    }

    public Set<LanguageObject> loadFromFile(String fileName) {
        fileName = "plugins/" + OpAPI.getPlugin().getName() + "/" + fileName;
        Set<LanguageObject> languageSet = new HashSet<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(": ");
                if (parts.length == 2) {
                    String path = parts[0].trim();
                    String object = parts[1].trim();
                    LanguageObject language = new LanguageObject(path, object);
                    languageSet.add(language);
                }
            }
            System.out.println("Language set loaded from file: " + fileName);
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + fileName);
        } catch (IOException e) {
            System.out.println("Error reading language set from file: " + fileName);
            e.printStackTrace();
        }
        return languageSet;
    }
}

