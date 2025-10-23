package keydust.contollers;

import keydust.db.SqliteDB;
import keydust.passwordmanager.Password;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CreateDatabaseController {

    public void createDatabase(String path, String password) throws SQLException {
        SqliteDB sqlite = new SqliteDB(path);

        String sql =
                "CREATE TABLE IF NOT EXISTS metadata (" +
                "id INTEGER PRIMARY KEY, " +
                        "Key VARCHAR(255) NOT NULL," +
                        "value VARCHAR(255) NOT NULL)";

        sqlite.createTable(sql);

        String sqlCredentialsTable =
                "CREATE TABLE IF NOT EXISTS credential ( " +
                        "id INTEGER PRIMARY KEY, " +
                        "description VARCHAR(255) NOT NULL, " +
                        "username VARCHAR(255) NOT NULL, " +
                        "password VARCHAR(255) NOT NULL)";

        sqlite.createTable(sqlCredentialsTable);

        Password pwd = new Password(password);

        String hash = pwd.getHash();
        String salt = pwd.getSalt();

        String sqlInsertMetadata =
                "INSERT INTO metadata (Key, value)" +
                        "VALUES (?, ?)";

        Connection connection = sqlite.getConnection();
        PreparedStatement ps = connection.prepareStatement(sqlInsertMetadata);
        
        ps.setString(1, "hash");
        ps.setString(2, hash);
        ps.executeUpdate();

        ps.setString(1, "salt");
        ps.setString(2, salt);
        ps.executeUpdate();

    }
}
