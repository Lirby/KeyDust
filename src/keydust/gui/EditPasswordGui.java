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
import keydust.contollers.EditPwdController;
import keydust.db.SqliteDB;
import keydust.gui.core.Builder;

import java.sql.SQLException;

public class EditPasswordGui extends Stage {

    private final TextField descriptionField = new TextField();
    private final TextField usernameField = new TextField();
    private final PasswordField passwordField = new PasswordField();
    private final Label feedback = new Label();

    private final int credentialId;
    private final String encryptionPassword;
    private final SqliteDB sqlite;
    private final Runnable onSaved;

    public EditPasswordGui(Stage owner,
                           String encryptionPassword,
                           SqliteDB sqlite,
                           int credentialId,
                           String description,
                           String username,
                           String password,
                           Runnable onSaved) {
        this.encryptionPassword = encryptionPassword;
        this.sqlite = sqlite;
        this.credentialId = credentialId;
        this.onSaved = onSaved;

        initOwner(owner);
        setTitle("Edit entry");
        setScene(buildScene(description, username, password));
        setResizable(false);
    }

    private Scene buildScene(String description, String username, String password) {
        Label title = new Label("Edit entry");
        title.getStyleClass().add("heading");

        Label helper = new Label("Update the description, username, or password and save the changes.");
        helper.getStyleClass().add("muted");

        descriptionField.setText(description);
        descriptionField.setPromptText("Description (e.g. Email, GitHub, Bank)");
        descriptionField.getStyleClass().add("input-field");

        usernameField.setText(username);
        usernameField.setPromptText("Username or email");
        usernameField.getStyleClass().add("input-field");

        passwordField.setText(password);
        passwordField.setPromptText("Password");
        passwordField.getStyleClass().add("input-field");
        passwordField.setOnAction(e -> save());

        Button saveButton = new Button("Save changes");
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

        EditPwdController controller = new EditPwdController(encryptionPassword, sqlite);
        try {
            controller.updatePassword(credentialId, description, username, pwd);
            if (onSaved != null) {
                onSaved.run();
            }
            close();
        } catch (SQLException ex) {
            showFeedback("Could not update entry: " + ex.getMessage(), "danger-text");
        }
    }

    private void showFeedback(String message, String styleClass) {
        feedback.getStyleClass().removeAll("danger-text", "success-text", "muted");
        feedback.getStyleClass().add(styleClass);
        feedback.setText(message);
    }
}
