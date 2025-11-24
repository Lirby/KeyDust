package keydust.contollers;

import keydust.db.SqliteDB;
import keydust.models.MetadataModel;
import keydust.passwordmanager.Password;

import java.sql.SQLException;

public class OpenDBController {

    SqliteDB sqlite;

    public OpenDBController(String path) {
        this.sqlite = new SqliteDB(path);

    }

    public boolean checkPassword(String password) throws SQLException {
        MetadataModel metadata = new MetadataModel(sqlite);

        String salt = metadata.getSalt();
        String pwdHash = metadata.getHash();

        Password pwd = new Password(password, salt);
        return pwd.checkHash(pwdHash);
    }

    public SqliteDB getSqlite() {
        return this.sqlite;
    }
}
