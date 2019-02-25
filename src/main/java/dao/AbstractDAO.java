package dao;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.pool.HikariPool;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public abstract class AbstractDAO<E, PK extends Serializable> {
    private static final String DRIVER = "org.postgresql.Driver";
    private static final String SERVER_NAME = "localhost";
    private static final int SERVER_PORT = 5432;
    private static final String DB_NAME = "WebLibrary";
    private static final String USER = "postgres";
    private static final String PASSWORD = "root";
    private static final String JDBC_URL;

    private HikariPool connectionPool;

    static {
        JDBC_URL = "jdbc:postgresql://"
                + SERVER_NAME
                + ":"
                + SERVER_PORT
                + "/"
                + DB_NAME;
    }

    public AbstractDAO() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(JDBC_URL);
        config.setDriverClassName(DRIVER);
        config.addDataSourceProperty("serverName", SERVER_NAME);
        config.addDataSourceProperty("port", SERVER_PORT);
        config.addDataSourceProperty("user", USER);
        config.addDataSourceProperty("password", PASSWORD);
        config.addDataSourceProperty("databaseName", DB_NAME);
        HikariDataSource dataSource = new HikariDataSource(config);
        connectionPool = new HikariPool(dataSource);
    }

    public Connection getConnection() throws SQLException {
        return connectionPool.getConnection();
    }

    public ResultSet getPreparedStatementResult(Connection connection, String sql, Object... params) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(sql);
        for (int i = 0; i < params.length; i++) {
            statement.setObject(i + 1, params[i]);
        }
        return statement.executeQuery();
    }

    public abstract List<E> runQuery(String sql);

    public abstract List<E> runQueryWithParams(String sql, Object... params);

    public abstract E getById(PK id);

    public abstract List<E> getAll();

    public abstract boolean save(E entity);

    public abstract boolean update(E entity);

    public abstract boolean delete(E entity);
}
