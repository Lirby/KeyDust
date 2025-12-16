package keydust.gui;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import keydust.contollers.ShowPwdController;
import keydust.db.SqliteDB;
import keydust.gui.core.Builder;

import java.sql.SQLException;

public class PasswordGui extends Stage {

    private final String encryptionPassword;
    private final SqliteDB sqlite;

    private final TableView<CredentialRow> tableView = new TableView<>();
    private final ObservableList<CredentialRow> rows = FXCollections.observableArrayList();
    private final Label feedback = new Label();

    public PasswordGui(String password, SqliteDB sqlite) {
        this.encryptionPassword = password;
        this.sqlite = sqlite;

        setTitle("KeyDust v0.2s ");
        setScene(buildScene());
        setMinWidth(760);
        setMinHeight(520);

        refreshTable();
    }

    private Scene buildScene() {
        Label title = new Label("Welcome to KeyDust");
        title.getStyleClass().add("heading");

        TableColumn<CredentialRow, String> descriptionCol = new TableColumn<>("Description");
        descriptionCol.setCellValueFactory(data -> data.getValue().descriptionProperty());
        TableColumn<CredentialRow, String> usernameCol = new TableColumn<>("Username");
        usernameCol.setCellValueFactory(data -> data.getValue().usernameProperty());
        TableColumn<CredentialRow, String> passwordCol = new TableColumn<>("Password");
        passwordCol.setCellValueFactory(data -> data.getValue().passwordProperty());

        tableView.getColumns().addAll(descriptionCol, usernameCol, passwordCol);
        tableView.setItems(rows);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableView.setPlaceholder(new Label("No Password yet. Add one to get started."));

        Button addPwd = new Button("Add Password");
        addPwd.getStyleClass().add("primary");
        addPwd.setOnAction(e -> openAddDialog());

        Button editPwd = new Button("Edit Password");
        editPwd.setOnAction(e -> openEditDialog());

        HBox actions = new HBox(10, editPwd, addPwd);
        actions.setAlignment(Pos.CENTER_RIGHT);

        feedback.getStyleClass().add("muted");

        VBox header = new VBox(4, title);
        header.setAlignment(Pos.CENTER_LEFT);

        VBox content = new VBox(14,
                header,
                tableView,
                actions,
                feedback
        );
        content.setAlignment(Pos.TOP_LEFT);
        content.setPadding(new Insets(6, 4, 6, 4));
        content.setPrefWidth(880);

        return Builder.createScene(content, 900, 580);
    }

    private void openAddDialog() {
        AddPasswordGui dialog = new AddPasswordGui(this, encryptionPassword, sqlite, this::refreshTable);
        dialog.show();
    }

    private void openEditDialog() {
        CredentialRow selected = tableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showFeedback("Select a credential to edit.", "danger-text");
            return;
        }

        EditPasswordGui dialog = new EditPasswordGui(
                this,
                encryptionPassword,
                sqlite,
                selected.getId(),
                selected.getDescription(),
                selected.getUsername(),
                selected.getPassword(),
                this::refreshTable
        );
        dialog.show();
        showFeedback("", "muted");
    }

    public void refreshTable() {
        ShowPwdController controller = new ShowPwdController(encryptionPassword, sqlite);
        try {
            String[][] data = controller.loadPasswords();
            rows.clear();
            for (String[] entry : data) {
                if (entry.length >= 4) {
                    rows.add(new CredentialRow(entry[0], entry[1], entry[2], entry[3]));
                }
            }
            showFeedback("", "muted");
        } catch (SQLException e) {
            showFeedback("Could not reload passwords: " + e.getMessage(), "danger-text");
        }
    }

    private void showFeedback(String message, String styleClass) {
        feedback.getStyleClass().removeAll("danger-text", "success-text", "muted");
        feedback.getStyleClass().add(styleClass);
        feedback.setText(message);
    }

    private static class CredentialRow {
        private final StringProperty id;
        private final StringProperty description;
        private final StringProperty username;
        private final StringProperty password;

        CredentialRow(String id, String description, String username, String password) {
            this.id = new SimpleStringProperty(id);
            this.description = new SimpleStringProperty(description);
            this.username = new SimpleStringProperty(username);
            this.password = new SimpleStringProperty(password);
        }

        public int getId() {
            return Integer.parseInt(id.get());
        }

        public String getDescription() {
            return description.get();
        }

        public StringProperty descriptionProperty() {
            return description;
        }

        public String getUsername() {
            return username.get();
        }

        public StringProperty usernameProperty() {
            return username;
        }

        public String getPassword() {
            return password.get();
        }

        public StringProperty passwordProperty() {
            return password;
        }
    }
}
