package keydust.models;

import keydust.db.SqliteDB;

import java.sql.SQLException;

public abstract class Model {

    protected SqliteDB sqlite;

    public Model(SqliteDB sqlite) {
        this.sqlite = sqlite;
    }

    protected void createGenericTable(String sql) throws SQLException {
        sqlite.createTable(sql);
    }
}
