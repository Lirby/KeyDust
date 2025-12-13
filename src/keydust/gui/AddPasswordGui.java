package keydust.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import keydust.contollers.AddPwdController;
import keydust.db.SqliteDB;
import keydust.gui.core.Builder;

import java.sql.SQLException;

public class AddPasswordGui extends Stage {

    private final TextField descriptionField = new TextField();
    private final TextField usernameField = new TextField();
    private final PasswordField passwordField = new PasswordField();
    private final Label feedback = new Label();

    private final String encryptionPassword;
    private final SqliteDB sqlite;
    private final Runnable onSaved;

    public AddPasswordGui(String password, SqliteDB sqlite, Runnable onSaved) {
        this(null, password, sqlite, onSaved);
    }

    public AddPasswordGui(Stage owner, String password, SqliteDB sqlite, Runnable onSaved) {
        this.encryptionPassword = password;
        this.sqlite = sqlite;
        this.onSaved = onSaved;

        if (owner != null) {
            initOwner(owner);
        }
        setTitle("Add entry");
        setScene(buildScene());
        setResizable(false);
    }

    private Scene buildScene() {
        Label title = new Label("Add new entry");
        title.getStyleClass().add("heading");

        Label helper = new Label("Save a description, username, and password. Everything stays encrypted.");
        helper.getStyleClass().add("muted");

        descriptionField.setPromptText("Description (URL, APP)");
        descriptionField.getStyleClass().add("input-field");

        usernameField.setPromptText("Username or email");
        usernameField.getStyleClass().add("input-field");

        passwordField.setPromptText("Password");
        passwordField.getStyleClass().add("input-field");
        passwordField.setOnAction(e -> save());

        Button saveButton = new Button("Save password");
        saveButton.getStyleClass().add("primary");
        saveButton.setOnAction(e -> save());

        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(e -> close());

        HBox actions = new HBox(10, saveButton, cancelButton);
        actions.setAlignment(Pos.CENTER_RIGHT);

        feedback.getStyleClass().add("muted");

        VBox content = new VBox(12,
                title,
                helper,
                descriptionField,
                usernameField,
                passwordField,
                actions,
                feedback
        );
        content.setAlignment(Pos.CENTER_LEFT);
        content.setPadding(new Insets(6, 2, 4, 2));
        content.setPrefWidth(420);

        return Builder.createScene(content, 500, 380);
    }

    private void save() {
        String description = descriptionField.getText().trim();
        String username = usernameField.getText().trim();
        String pwd = passwordField.getText();

        if (description.isEmpty() || username.isEmpty() || pwd.isEmpty()) {
            showFeedback("Fill in all fields to save the entry.", "danger-text");
            return;
        }

        AddPwdController controller = new AddPwdController(encryptionPassword, sqlite);
        try {
            controller.savePassword(description, username, pwd);
            if (onSaved != null) {
                onSaved.run();
            }
            close();
        } catch (SQLException ex) {
            showFeedback("Could not add entry: " + ex.getMessage(), "danger-text");
        }
    }

    private void showFeedback(String message, String styleClass) {
        feedback.getStyleClass().removeAll("danger-text", "success-text", "muted");
        feedback.getStyleClass().add(styleClass);
        feedback.setText(message);
    }
}
