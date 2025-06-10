package li.cinnazeyy.langlibs.core.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class DatabaseConnection {
    private final static HikariConfig config = new HikariConfig();
    private static HikariDataSource dataSource;

    private static int connectionClosed, connectionOpened;
    private static final Logger logger = LoggerFactory.getLogger(DatabaseConnection.class);

    @SuppressWarnings("unused")
    public static void InitializeDatabase(DatabaseCredentials credentials) throws ClassNotFoundException {
        Class.forName("org.mariadb.jdbc.Driver");

        String URL = credentials.url();
        String name = credentials.name();
        String username = credentials.username();
        String password = credentials.password();

        config.setJdbcUrl(URL + name);
        config.setUsername(username);
        config.setPassword(password);
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        config.setMaximumPoolSize(3);

        dataSource = new HikariDataSource(config);

        createTables();
    }

    @Deprecated
    public static Connection getConnection() {
        int retries = 3;
        while (retries > 0) {
            try {
                return dataSource.getConnection();
            } catch (SQLException ex) {
                logger.error("Database connection failed!\n\n{}", ex.getMessage());
            }
            retries--;
        }
        return null;
    }

    public static StatementBuilder createStatement(String sql) {
        return new StatementBuilder(sql);
    }

    public static void closeResultSet(ResultSet resultSet) throws SQLException {
        if (resultSet.isClosed()
                && resultSet.getStatement().isClosed()
                && resultSet.getStatement().getConnection().isClosed())
            return;

        resultSet.close();
        resultSet.getStatement().close();
        resultSet.getStatement().getConnection().close();

        connectionClosed++;

        if (connectionOpened > connectionClosed + 5)
            logger.error("There are multiple database connections opened. Please report this issue.");
    }

    static void createTables() {
        try (Connection con = dataSource.getConnection()) {
            for (String table : StatementBuilder.Tables.getTables()) {
                Objects.requireNonNull(con).prepareStatement(table).executeUpdate();
            }
        } catch (SQLException ex) {
            logger.error("An error occurred while creating database table!", ex);
        }
    }

    public static class StatementBuilder implements AutoCloseable {
        private final String sql;
        private final List<Object> values = new ArrayList<>();

        private Connection connection = null;
        private PreparedStatement statement = null;
        private ResultSet resultSet = null;

        public StatementBuilder(String sql) {
            this.sql = sql;
        }

        private void openConnection() throws SQLException {
            connection = dataSource.getConnection();
            if (connection == null) throw new NullPointerException("Connection is invalid: null");
        }

        public ResultSet executeQuery() throws SQLException {
            if (connection != null) throw new IllegalStateException("Builder is already fired.");

            openConnection();
            statement = connection.prepareStatement(sql);
            resultSet = iterateValues(statement).executeQuery();

            connectionOpened++;

            return resultSet;
        }

        @SuppressWarnings("unused")
        public void executeUpdate() throws SQLException {
            if (connection != null) throw new IllegalStateException("Builder is already fired.");

            openConnection();
            statement = connection.prepareStatement(sql);
            iterateValues(statement).executeUpdate();
            close();
        }

        public StatementBuilder setValue(Object value) {
            values.add(value);
            return this;
        }

        private PreparedStatement iterateValues(PreparedStatement ps) throws SQLException {
            for (int i = 0; i < values.size(); i++) {
                ps.setObject(i + 1, values.get(i));
            }
            return ps;
        }

        @Override
        public void close() throws SQLException {
            if (connection != null) {
                connection.close();
                connection = null;
            }

            if (statement != null) {
                statement.close();
                statement = null;
            }

            if (resultSet != null) {
                resultSet.close();
                resultSet = null;
            }
        }

        private static class Tables {
            private final static List<String> tables;

            public static List<String> getTables() {
                return tables;
            }

            static {
                tables = Collections.singletonList(
                        // LangUser
                        "CREATE TABLE IF NOT EXISTS `langUsers` (" +
                                "`uuid` varchar(36) PRIMARY KEY," +
                                "`lang` varchar(10)" +
                                ");"
                );
            }
        }
    }
}
