package me.opkarol.opc.api.database.configuration;

import me.opkarol.opc.OpAPI;
import me.opkarol.opc.api.database.flat.FlatDatabase;

public class FlatDatabaseConfiguration {
    private Object object;
    private String fileName;
    private final FlatDatabase<Object> flatDatabase;

    public FlatDatabaseConfiguration(String fileName, Object object) {
        this.fileName = fileName;
        this.object = object;
        this.flatDatabase = new FlatDatabase<>(OpAPI.getInstance(), fileName);
    }

    public void save() {
        flatDatabase.saveObject(object);
    }

    public Object get() {
        this.object = flatDatabase.loadObject();
        return object;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public FlatDatabase<Object> getFlatDatabase() {
        return flatDatabase;
    }
}
