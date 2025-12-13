package keydust.contollers;

import keydust.db.SqliteDB;
import keydust.models.CredentialModel;

import java.sql.SQLException;

public class EditPwdController {

    private final String password;
    private final SqliteDB sqlite;

    public EditPwdController(String password, SqliteDB sqlite) {
        this.password = password;
        this.sqlite = sqlite;
    }

    public void updatePassword(int id, String description, String username, String password) throws SQLException {
        CredentialModel credential = new CredentialModel(sqlite);
        credential.updateCredential(this.password, id, description, username, password);
    }
}
