package me.opkarol.opc.api.database.flat;

import me.opkarol.opc.api.database.IFlatDatabase;
import me.opkarol.opc.api.files.Configuration;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

import java.io.*;

public class FlatDatabase<K> implements IFlatDatabase<K> {
    private final Configuration configuration;

    public FlatDatabase(Plugin plugin, String fileName) {
        this.configuration = new Configuration(plugin, fileName, true);
        this.configuration.createConfig();
    }

    @Nullable
    public K loadObject() {
        File file = configuration.getConfiguration();
        if (!file.isFile() || !file.exists()) {
            return null;
        }
        try {
            return readFile(configuration.getConfiguration().getPath());
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void saveObject(K objectToSave) {
        try {
            saveFile(objectToSave, configuration.getConfiguration().getPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveFile(K object, String path) throws IOException {
        if (object == null) {
            return;
        }
        try (ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(path))) {
            os.writeObject(object);
        }
    }

    @Nullable
    public K readFile(String path) throws ClassNotFoundException, IOException {
        try (ObjectInputStream is = new ObjectInputStream(new FileInputStream(path))) {
            return (K) is.readObject();
        } catch (StreamCorruptedException ignore) {
            return null;
        }
    }
}
