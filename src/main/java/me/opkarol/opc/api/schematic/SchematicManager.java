package me.opkarol.opc.api.schematic;

import java.util.ArrayList;
import java.util.List;

public class SchematicManager {
    private final List<SchematicQueue> queues = new ArrayList<>();

    public List<SchematicQueue> getQueues() {
        return queues;
    }
}
