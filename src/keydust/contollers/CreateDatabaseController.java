package keydust.contollers;

import keydust.db.SqliteDB;
import keydust.models.CredentialModel;
import keydust.models.MetadataModel;
import keydust.passwordmanager.Password;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CreateDatabaseController {

    public void createDatabase(String path, String password) throws SQLException {
        SqliteDB sqlite = new SqliteDB(path);

        CredentialModel credential = new CredentialModel(sqlite);
        credential.createTable();

        MetadataModel metadata = new MetadataModel(sqlite);
        metadata.createTable();

        Password pwd = new Password(password);

        String hash = pwd.getHash();
        String salt = pwd.getSalt();
        metadata.saveHash(hash);
        metadata.saveSalt(salt);

    }
}