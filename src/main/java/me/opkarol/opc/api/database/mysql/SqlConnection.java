package me.opkarol.opc.api.database.mysql;

import com.zaxxer.hikari.HikariDataSource;
import me.opkarol.opc.api.database.ISqlConnection;
import me.opkarol.opc.api.database.manager.settings.SqlDatabaseSettings;
import me.opkarol.opc.api.database.mysql.table.SqlDeleteTable;
import me.opkarol.opc.api.database.mysql.table.SqlInsertTable;
import me.opkarol.opc.api.database.mysql.table.SqlTable;
import me.opkarol.opc.api.file.Configuration;
import me.opkarol.opc.api.utils.VariableUtil;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SqlConnection implements ISqlConnection {
    private HikariDataSource source;

    public SqlConnection(@NotNull Configuration configuration, String path) {
        setup(configuration, path);
    }

    public SqlConnection() {
    }

    public SqlConnection(String jdbc, String user, String password) {
        setup(jdbc, user, password);
    }

    public SqlConnection(@NotNull SqlDatabaseSettings mysql) {
        this(mysql.getJdbc(), mysql.getUser(), mysql.getPassword());
    }

    @Override
    public void setup() {
        setup("jdbc:mysql://localhost:3306/simpsons", "bart", "51mp50n");
    }

    public void setup(@NotNull Configuration configuration, String path) {
        path = VariableUtil.ifNotEndsWithAdd(path, ".");
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
        source = new HikariDataSource(ds);
    }

    @Override
    public void close() {
        if (source != null && !source.isClosed()) {
            source.close();
            source = null;
        }
    }

    @Override
    public void create(@NotNull SqlTable table) {
        try (Connection conn = source.getConnection();
             PreparedStatement stmt = conn.prepareStatement(table.toCreateTableString())) {
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void insert(@NotNull SqlInsertTable table) {
        try (Connection conn = source.getConnection();
             PreparedStatement stmt = conn.prepareStatement(table.toInsertIntoString())) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(@NotNull SqlDeleteTable table) {
        try (Connection conn = source.getConnection();
             PreparedStatement stmt = conn.prepareStatement(table.toDeleteString())) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ResultSet get(SqlTable table) {
        try (Connection conn = source.getConnection();
             PreparedStatement stmt = conn.prepareStatement(String.format("SELECT * FROM %s;", table.getTableName()))) {
            return stmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void run(String statement) {
        try (Connection conn = source.getConnection();
             PreparedStatement stmt = conn.prepareStatement(statement)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ResultSet query(String statement) throws SQLException {
        try (Connection conn = source.getConnection();
             PreparedStatement stmt = conn.prepareStatement(statement)) {
            return stmt.executeQuery();
        }
    }
}