package keydust.contollers;

import keydust.db.SqliteDB;
import keydust.models.CredentialModel;

import java.sql.SQLException;

public class AddPwdController {

    String password;
    SqliteDB sqlite;

    public AddPwdController(String password, SqliteDB sqlite) {
        this.password = password;
        this.sqlite = sqlite;
    }

    public  void savePassword(String description, String username, String password) throws SQLException {
        CredentialModel credential = new CredentialModel(sqlite);
        credential.saveCredential(this.password, description, username, password);
    }
}
