package me.opkarol.opc.api.gui.misc;

import me.opkarol.opc.api.gui.type.OpInventoryBuilder;
import me.opkarol.opc.api.gui.type.OpPageInventoryBuilder;

public class OpInventoryObject {
    private OpInventoryBuilder builder;
    private OpPageInventoryBuilder pageBuilder;

    public OpInventoryObject(OpPageInventoryBuilder opPageInventoryBuilder) {
        setPageBuilder(opPageInventoryBuilder);
    }

    public OpInventoryObject(OpInventoryBuilder opInventoryBuilder) {
        setBuilder(opInventoryBuilder);
    }

    public OpInventoryBuilder getBuilder() {
        return builder;
    }

    public OpInventoryObject setBuilder(OpInventoryBuilder builder) {
        this.builder = builder;
        return this;
    }

    public OpPageInventoryBuilder getPageBuilder() {
        return pageBuilder;
    }

    public OpInventoryObject setPageBuilder(OpPageInventoryBuilder pageBuilder) {
        this.pageBuilder = pageBuilder;
        this.builder = pageBuilder;
        return this;
    }

    public boolean isMainBuilder() {
        return getPageBuilder() == null;
    }
}
