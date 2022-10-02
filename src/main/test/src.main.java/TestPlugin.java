import me.opkarol.opc.api.database.configuration.DatabaseConfiguration;
import me.opkarol.opc.api.plugin.OpDatabasePlugin;
import org.jetbrains.annotations.NotNull;

public class TestPlugin extends OpDatabasePlugin {

    @Override
    public @NotNull DatabaseConfiguration getDatabaseConfiguration() {
        return new DatabaseConfiguration("fileName", DatabaseConfiguration.SUPPORTED_TYPES.MYSQL, DatabaseConfiguration.SUPPORTED_TYPES.FLAT);
    }
}
