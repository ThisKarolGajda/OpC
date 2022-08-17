package me.opkarol.opc.api.database.mysql;

import com.zaxxer.hikari.HikariDataSource;
import me.opkarol.opc.api.database.IMySqlDatabase;
import org.jetbrains.annotations.NotNull;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MySqlDatabase implements IMySqlDatabase {
    private HikariDataSource hikariDataSource;
    private DataSource source;

    @Override
    public void setup() {
        HikariDataSource ds = new HikariDataSource();
        ds.setJdbcUrl("jdbc:mysql://localhost:3306/simpsons");
        ds.setUsername("bart");
        ds.setPassword("51mp50n");
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
    public ResultSet get(String table) {
        try (Connection conn = source.getConnection();
             PreparedStatement stmt = conn.prepareStatement(String.format("SELECT * FROM `%s`", table))) {
            return stmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
