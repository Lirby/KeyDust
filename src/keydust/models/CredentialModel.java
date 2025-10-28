package keydust.models;

import keydust.db.SqliteDB;
import org.jasypt.util.text.StrongTextEncryptor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CredentialModel extends Model {
    public CredentialModel(SqliteDB sqlite) {
        super(sqlite);
    }

    public void createTable() throws SQLException {
        String sqlCredentialsTable =
                "CREATE TABLE IF NOT EXISTS credential ( " +
                        "id INTEGER PRIMARY KEY, " +
                        "description VARCHAR(255) NOT NULL, " +
                        "username VARCHAR(255) NOT NULL, " +
                        "password VARCHAR(255) NOT NULL)";

        createGenericTable(sqlCredentialsTable);
    }

    public void saveCredential(String encryptionPassword, String description, String username, String password) throws SQLException {
        String sql =
                "INSERT INTO credential " +
                "(description, username, password)" +
                        "VALUES (?, ?, ?)";

        Connection connection = sqlite.getConnection();
        PreparedStatement st = connection.prepareStatement(sql);

        StrongTextEncryptor txtEncryptor = new StrongTextEncryptor();
        txtEncryptor.setPassword(encryptionPassword);

        String encryptedDescription = txtEncryptor.encrypt(description);
        String encryptedUsername = txtEncryptor.encrypt(username);
        String encryptedPassword = txtEncryptor.encrypt(password);

        st.setString(1, encryptedDescription);
        st.setString(2, encryptedUsername);
        st.setString(3, encryptedPassword);

        st.executeUpdate();
    }
}
