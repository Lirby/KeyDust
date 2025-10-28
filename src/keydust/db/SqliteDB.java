package keydust.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class SqliteDB {
    private String connectionStr = "jdbc:sqlite:";

    public SqliteDB(String path) {
        connectionStr += path;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(connectionStr);
    }

    public void createTable(String sql) throws SQLException {
        Connection connection = getConnection();
        Statement stmt = connection.createStatement();
        stmt.execute(sql);
        connection.close();
    }
}