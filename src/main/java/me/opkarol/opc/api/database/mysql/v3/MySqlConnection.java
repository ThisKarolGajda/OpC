package me.opkarol.opc.api.database.mysql.v3;

import com.zaxxer.hikari.HikariDataSource;
import me.opkarol.opc.api.database.IMySqlDatabase;
import me.opkarol.opc.api.database.mysql.MySqlDeleteTable;
import me.opkarol.opc.api.database.mysql.MySqlInsertTable;
import me.opkarol.opc.api.database.mysql.MySqlTable;
import me.opkarol.opc.api.files.Configuration;
import org.jetbrains.annotations.NotNull;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MySqlConnection implements IMySqlDatabase {
    private HikariDataSource hikariDataSource;
    private DataSource source;

    public MySqlConnection(@NotNull Configuration configuration, String path) {
        setup(configuration, path);
    }

    public MySqlConnection() {

    }

    @Override
    public void setup() {
        setup("jdbc:mysql://localhost:3306/simpsons", "bart", "51mp50n");
    }

    public void setup(@NotNull Configuration configuration, String path) {
        setup(configuration.get(path + "jdbc"), configuration.get("user"), configuration.get("password"));
    }

    public void setup(String jdbcUrl, String username, String password) {
        HikariDataSource ds = new HikariDataSource();
        ds.setJdbcUrl(jdbcUrl);
        ds.setUsername(username);
        ds.setPassword(password);
        ds.addDataSourceProperty("cachePrepStmts", "true");
        ds.addDataSourceProperty("prepStmtCacheSize", "250");
        ds.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        ds.addDataSourceProperty("useServerPrepStmts", "true");
        ds.addDataSourceProperty("useLocalSessionState", "true");
        ds.addDataSourceProperty("rewriteBatchedStatements", "true");
        ds.addDataSourceProperty("cacheResultSetMetadata", "true");
        ds.addDataSourceProperty("cacheServerConfiguration", "true");
        ds.addDataSourceProperty("elideSetAutoCommits", "true");
        ds.addDataSourceProperty("maintainTimeStats", "false");
        source = ds;
        hikariDataSource = new HikariDataSource(ds);
    }

    @Override
    public void close() {
        if (hikariDataSource != null && !hikariDataSource.isClosed()) {
            hikariDataSource.close();
        }
    }

    @Override
    public void create(@NotNull MySqlTable table) {
        try (Connection conn = source.getConnection();
             PreparedStatement stmt = conn.prepareStatement(table.toCreateTableString())) {
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void insert(@NotNull MySqlInsertTable table) {
        try (Connection conn = source.getConnection();
             PreparedStatement stmt = conn.prepareStatement(table.toInsertIntoString())) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(@NotNull MySqlDeleteTable table) {
        try (Connection conn = source.getConnection();
             PreparedStatement stmt = conn.prepareStatement(table.toDeleteString())) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ResultSet get(MySqlTable table) {
        try (Connection conn = source.getConnection();
             PreparedStatement stmt = conn.prepareStatement(String.format("SELECT * FROM `%s`", table.getTableName()))) {
            return stmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
