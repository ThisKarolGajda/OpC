package me.opkarol.opc.api.plugin;

import me.opkarol.opc.api.database.configuration.DatabaseConfiguration;
import org.jetbrains.annotations.NotNull;

public abstract class OpDatabasePlugin extends OpPlugin {

    public abstract @NotNull DatabaseConfiguration getDatabaseConfiguration();

    @Override
    public void onDisable() {
        DatabaseConfiguration databaseConfiguration = getDatabaseConfiguration();

        super.onDisable();
    }
}
