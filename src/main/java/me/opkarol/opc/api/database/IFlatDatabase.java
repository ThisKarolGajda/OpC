package me.opkarol.opc.api.database;

import java.io.IOException;

public interface IFlatDatabase<K> {
    K loadObject();

    void saveObject(K objectToSave);

    void saveFile(K object, String path) throws IOException;

    K readFile(String path) throws ClassNotFoundException, IOException;
}
