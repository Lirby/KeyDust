package keydust.models;

import keydust.db.SqliteDB;

import java.sql.*;

public class MetadataModel extends Model {

    private static final String sqlInsertMetadata =
            "INSERT INTO metadata (Key, value)" +
                    "VALUES (?, ?)";

    public MetadataModel(SqliteDB sqlite) {
        super(sqlite);
    }

    public void createTable() throws SQLException {
        String sql =
                "CREATE TABLE IF NOT EXISTS metadata (" +
                        "key VARCHAR(255) NOT NULL," +
                        "value VARCHAR(255) NOT NULL)";

        createGenericTable(sql);
    }

    public void saveHash(String hash) throws SQLException {
        saveData("hash", hash);
    }

    public String getHash() throws SQLException {
        return getValue("hash");
    }

    public void saveSalt(String salt) throws SQLException {
        saveData("salt", salt);
    }

    public String getSalt() throws SQLException {
        return getValue("salt");
    }

    private String getValue(String key) throws SQLException {
        String sql =
                "SELECT * FROM metadata " +
                        "WHERE key = '" + key +"'";

        Connection con = sqlite.getConnection();
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(sql);

        String value = "";
        while (rs.next()) {
            value = rs.getString("value");
        }

        return value;
    }

    private void saveData(String name, String data) throws SQLException {
        Connection connection = sqlite.getConnection();
        PreparedStatement ps = connection.prepareStatement(sqlInsertMetadata);

        ps.setString(1, name);
        ps.setString(2, data);
        ps.executeUpdate();
    }

}
