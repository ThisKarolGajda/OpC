package me.opkarol.opc.api.database.manager;

import me.opkarol.opc.OpAPI;
import me.opkarol.opc.api.database.flat.DefaultFlatDatabase;
import me.opkarol.opc.api.database.flat.DefaultFlatDatabaseSettings;
import me.opkarol.opc.api.database.mysql.OpMConnection;
import me.opkarol.opc.api.database.mysql.base.OpMDatabaseAuto;
import me.opkarol.opc.api.database.mysql.base.OpMDatabaseAutoSettings;
import me.opkarol.opc.api.database.mysql.base.OpMSingleDatabase;
import me.opkarol.opc.api.database.mysql.objects.OpMObjects;
import me.opkarol.opc.api.database.mysql.resultset.OpMResultSet;
import me.opkarol.opc.api.plugin.OpPlugin;
import me.opkarol.opc.api.utils.VariableUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class DefaultDatabasePlugin<O> extends OpPlugin {
    private final boolean mysqlEnabled;
    private final String mysqlConfigurationPath;
    private final String mysqlTable;
    private final IDefaultDatabase<O> database;

    public DefaultDatabasePlugin(String mysqlConfigurationPath, OpMDatabaseAutoSettings<O> mySqlDatabaseSettings, OpMObjects<O> objects, DefaultFlatDatabaseSettings<O> flatDatabaseSettings) {
        this.mysqlConfigurationPath = VariableUtil.ifNotEndsWithAdd(mysqlConfigurationPath, ".");
        this.mysqlEnabled = OpAPI.getConfig().getConfig().getBoolean(mysqlConfigurationPath + "enabled");
        if (mysqlEnabled) {
            this.mysqlTable = OpAPI.getConfig().get(mysqlConfigurationPath + "table");
            this.database = new OpMDatabaseAuto<>(new OpMSingleDatabase<>(mysqlTable, new OpMConnection(OpAPI.getConfig(), mysqlConfigurationPath), objects)) {
                @Override
                public Predicate<O> getPredicate(Object object) {
                    return mySqlDatabaseSettings.getPredicate(object);
                }

                @Override
                public Function<OpMResultSet, O> getObjectAsResultSet() {
                    return mySqlDatabaseSettings.getObjectAsResultSet();
                }

                @Override
                public Function<O, Integer> getIdentification() {
                    return mySqlDatabaseSettings.getIdentification();
                }

                @Override
                public BiConsumer<O, Integer> setIdentification() {
                    return mySqlDatabaseSettings.setIdentification();
                }

                @Override
                public Function<O, UUID> getUUID() {
                    return mySqlDatabaseSettings.getUUID();
                }
            };
        } else {
            this.mysqlTable = null;
            this.database = new DefaultFlatDatabase<>(flatDatabaseSettings.getFileName()) {
                @Override
                public Predicate<O> getPredicate(Object object) {
                    return flatDatabaseSettings.getPredicate(object);
                }
            };
        }
    }

    public DefaultDatabasePlugin(@NotNull DefaultFlatDatabaseSettings<O> flatDatabaseSettings) {
        this.mysqlConfigurationPath = null;
        this.mysqlEnabled = false;
        this.mysqlTable = null;
        this.database = new DefaultFlatDatabase<>(flatDatabaseSettings.getFileName()) {
            @Override
            public Predicate<O> getPredicate(Object object) {
                return flatDatabaseSettings.getPredicate(object);
            }
        };
    }

    public DefaultDatabasePlugin(String mysqlConfigurationPath, OpMDatabaseAutoSettings<O> mySqlDatabaseSettings, OpMObjects<O> objects) {
        this.mysqlConfigurationPath = VariableUtil.ifNotEndsWithAdd(mysqlConfigurationPath, ".");
        this.mysqlEnabled = OpAPI.getConfig().getConfig().getBoolean(mysqlConfigurationPath + "enabled");
        if (mysqlEnabled) {
            this.mysqlTable = OpAPI.getConfig().get(mysqlConfigurationPath + "table");
            this.database = new OpMDatabaseAuto<>(new OpMSingleDatabase<>(mysqlTable, new OpMConnection(OpAPI.getConfig(), mysqlConfigurationPath), objects)) {
                @Override
                public Predicate<O> getPredicate(Object object) {
                    return mySqlDatabaseSettings.getPredicate(object);
                }

                @Override
                public Function<OpMResultSet, O> getObjectAsResultSet() {
                    return mySqlDatabaseSettings.getObjectAsResultSet();
                }

                @Override
                public Function<O, Integer> getIdentification() {
                    return mySqlDatabaseSettings.getIdentification();
                }

                @Override
                public BiConsumer<O, Integer> setIdentification() {
                    return mySqlDatabaseSettings.setIdentification();
                }

                @Override
                public Function<O, UUID> getUUID() {
                    return mySqlDatabaseSettings.getUUID();
                }
            };
        } else {
            this.mysqlTable = null;
            this.database = null;
        }
    }

    public boolean mysqlEnabled() {
        return mysqlEnabled;
    }

    public String mysqlConfigurationPath() {
        return mysqlConfigurationPath;
    }

    public IDefaultDatabase<O> getDatabase() {
        return database;
    }

    public boolean delete(UUID uuid, Object object) {
        return getDatabase().delete(uuid, object);
    }

    public boolean contains(UUID uuid, Object object) {
        return getDatabase().contains(uuid, object);
    }

    public Optional<O> get(UUID uuid, Object object) {
        return getDatabase().get(uuid, object);
    }
}
