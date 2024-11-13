package util;

import exception.ConnectionException;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;

// To connection locally, use the env var:
// JDBC_DATABASE_URL=jdbc:postgresql://<host>:<port>/<dbname>?user=<username>&password=<password>

public class DBConnection {
    private static final ThreadLocal<DBConnection> current = new ThreadLocal<>();

    private final Connection connection;

    public static void newConnection() throws ConnectionException {
        current.remove();
        setCurrent(new DBConnection());
    }

    private static void setCurrent(DBConnection connection) {
        current.set(connection);
    }

    public static DBConnection getConnection() {
        return current.get();
    }

    private DBConnection() throws ConnectionException {
        try {
            DriverManager.registerDriver(new org.postgresql.Driver());
            // String DB_CONNECTION = System.getenv().get("JDBC_DATABASE_URL");
            // Please change the following to your own database connection string
            String DB_CONNECTION = "jdbc:postgresql://<host>:<port>/<dbname>?user=<username>&password=<password>";
            connection = DriverManager.getConnection(DB_CONNECTION);
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            throw new ConnectionException();
        }
    }

    public PreparedStatement prepare(String sql, ArrayList<String> strings) throws ConnectionException {
        PreparedStatement statement;
        try {
            statement = connection.prepareStatement(sql);
            for (int i = 1; i <= strings.size(); i++) {
                if (strings.get(i - 1) != null) {
                    try {
                        statement.setBigDecimal(i, new BigDecimal(strings.get(i - 1)));
                    } catch (NumberFormatException e) {
                        statement.setString(i, strings.get(i - 1));
                    }
                } else {
                    statement.setNull(i, Types.VARCHAR);
                }
            }
        } catch (SQLException e) {
            throw new ConnectionException();
        }
        return statement;
    }

    public void commit() throws ConnectionException {
        try {
            connection.commit();
        } catch (SQLException e) {
            throw new ConnectionException();
        }
        close();
    }

    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        current.remove();
    }
}
