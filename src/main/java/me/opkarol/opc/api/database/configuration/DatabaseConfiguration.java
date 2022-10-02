package me.opkarol.opc.api.database.configuration;

import me.opkarol.opc.OpAPI;
import me.opkarol.opc.api.files.Configuration;

import java.util.ArrayList;
import java.util.List;

public class DatabaseConfiguration {
    private final List<SUPPORTED_TYPES> supportedTypes = new ArrayList<>();
    private final String fileName;
    private final Configuration configuration;
    private FlatDatabaseConfiguration flatDatabaseConfiguration;

    public DatabaseConfiguration(String fileName, SUPPORTED_TYPES... types) {
        this(List.of(types), fileName);
    }

    public DatabaseConfiguration(List<SUPPORTED_TYPES> types, String fileName) {
        this.fileName = fileName;
        this.configuration = new Configuration(OpAPI.getInstance(), fileName);
        supportedTypes.addAll(types);
    }

    public List<SUPPORTED_TYPES> getSupportedTypes() {
        return supportedTypes;
    }

    public String getFileName() {
        return fileName;
    }

    public void createFile() {
        configuration.createNewFile();
    }



    public Configuration getConfiguration() {
        return configuration;
    }

    public FlatDatabaseConfiguration getFlatDatabaseConfiguration() {
        return flatDatabaseConfiguration;
    }

    public void setFlatDatabaseConfiguration(FlatDatabaseConfiguration flatDatabaseConfiguration) {
        this.flatDatabaseConfiguration = flatDatabaseConfiguration;
    }

    public enum SUPPORTED_TYPES {
        FLAT, MYSQL
    }
}
