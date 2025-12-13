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
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import keydust.contollers.OpenDBController;
import keydust.gui.core.Builder;

import java.io.File;
import java.sql.SQLException;

public class UnlockGui {

    private final Stage stage;
    private final TextField pathField = new TextField();
    private final PasswordField passwordField = new PasswordField();
    private final Label feedback = new Label();

    public UnlockGui(Stage stage) {
        this.stage = stage;
        stage.setTitle("KeyDust v0.2s");
        stage.setScene(buildScene());
        stage.setResizable(false);
        stage.centerOnScreen();
    }

    private Scene buildScene() {
        Label title = new Label("KeyDust (Snapshot Build 13.12.25)");
        title.getStyleClass().add("heading");

        Label subtitle = new Label("Database:");
        subtitle.getStyleClass().add("subtitle");

        pathField.setPromptText("Choose your database file");
        pathField.getStyleClass().add("input-field");
        pathField.setPrefWidth(320);

        Button browse = new Button("Browse");
        browse.setOnAction(e -> selectDatabase());

        HBox pathRow = new HBox(10, pathField, browse);
        pathRow.setAlignment(Pos.CENTER);

        Label label = new Label("Password:");
        label.getStyleClass().add("subtitle");

        passwordField.setPromptText("Master password");
        passwordField.getStyleClass().add("input-field");
        passwordField.setOnAction(e -> handleOpen());

        Button openBtn = new Button("Unlock Database");
        openBtn.getStyleClass().add("primary");
        openBtn.setOnAction(e -> handleOpen());

        Button newDbBtn = new Button("Create new Database");
        newDbBtn.setOnAction(e -> new NewDBgui(stage, createdPath -> {
            pathField.setText(createdPath);
            showMessage("New Database created. Enter the password to unlock it.", "success-text");
        }).show());

        HBox actions = new HBox(12, openBtn, newDbBtn);
        actions.setAlignment(Pos.CENTER);

        feedback.getStyleClass().add("muted");

        VBox content = new VBox(14, title, subtitle, pathRow, label, passwordField, actions, feedback);
        content.setAlignment(Pos.CENTER);
        content.setFillWidth(false);
        content.setPadding(new Insets(4, 6, 2, 6));
        content.setPrefWidth(540);

        return Builder.createScene(content, 620, 420);
    }

    private void selectDatabase() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Select KeyDust database");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("KeyDust Database", "*.db", "*.kdbd", "*.*"));

        File selected = chooser.showOpenDialog(stage);
        if (selected != null) {
            pathField.setText(selected.getAbsolutePath());
            showMessage("", "muted");
        }
    }

    private void handleOpen() {
        String path = pathField.getText().trim();
        String pwd = passwordField.getText();

        if (path.isEmpty()) {
            showMessage("Please choose a database file.", "danger-text");
            return;
        }

        if (pwd.isEmpty()) {
            showMessage("Enter the master password to continue.", "danger-text");
            return;
        }

        OpenDBController controller = new OpenDBController(path);
        try {
            if (controller.checkPassword(pwd)) {
                PasswordGui passwords = new PasswordGui(pwd, controller.getSqlite());
                passwords.show();
                stage.close();
            } else {
                showMessage("Password does not match this vault.", "danger-text");
            }
        } catch (SQLException ex) {
            showMessage("Could not open vault: " + ex.getMessage(), "danger-text");
        }
    }

    private void showMessage(String message, String styleClass) {
        feedback.getStyleClass().removeAll("danger-text", "success-text", "muted");
        feedback.getStyleClass().add(styleClass);
        feedback.setText(message);
    }

    public void show() {
        stage.show();
    }
}
