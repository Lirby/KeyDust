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
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import keydust.contollers.CreateDatabaseController;
import keydust.gui.core.Builder;

import java.io.File;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.function.Consumer;

public class NewDBgui extends Stage {

    private final TextField folderField = new TextField();
    private final TextField nameField = new TextField();
    private final PasswordField passwordField = new PasswordField();
    private final Label feedback = new Label();
    private File selectedDirectory;
    private final Consumer<String> onCreated;

    public NewDBgui(Stage owner) {
        this(owner, null);
    }

    public NewDBgui(Stage owner, Consumer<String> onCreated) {
        this.onCreated = onCreated;
        initOwner(owner);
        setTitle("Create new vault");
        setScene(buildScene());
        setResizable(false);
    }

    private Scene buildScene() {
        Label title = new Label("New encrypted vault");
        title.getStyleClass().add("heading");

        Label subtitle = new Label("Choose where to store the database and set a strong master password.");
        subtitle.getStyleClass().add("subtitle");

        folderField.setPromptText("Pick a folder");
        folderField.setEditable(false);
        folderField.getStyleClass().add("input-field");
        folderField.setPrefWidth(340);

        Button chooseFolder = new Button("Browse");
        chooseFolder.setOnAction(e -> selectFolder());

        HBox folderRow = new HBox(10, folderField, chooseFolder);
        folderRow.setAlignment(Pos.CENTER_LEFT);

        nameField.setPromptText("Database name (e.g. keydust.db)");
        nameField.getStyleClass().add("input-field");

        passwordField.setPromptText("Master password");
        passwordField.getStyleClass().add("input-field");
        passwordField.setOnAction(e -> save());

        Button createButton = new Button("Create database");
        createButton.getStyleClass().add("primary");
        createButton.setOnAction(e -> save());

        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(e -> close());

        HBox actions = new HBox(10, createButton, cancelButton);
        actions.setAlignment(Pos.CENTER_RIGHT);

        feedback.getStyleClass().add("muted");

        VBox content = new VBox(12,
                title,
                subtitle,
                folderRow,
                nameField,
                passwordField,
                actions,
                feedback
        );
        content.setAlignment(Pos.CENTER_LEFT);
        content.setPadding(new Insets(6, 2, 4, 2));
        content.setPrefWidth(520);

        return Builder.createScene(content, 620, 400);
    }

    private void selectFolder() {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Select save directory");
        File chosen = chooser.showDialog(this);
        if (chosen != null) {
            selectedDirectory = chosen;
            folderField.setText(selectedDirectory.getAbsolutePath());
            setFeedback("", "muted");
        }
    }

    private void save() {
        if (selectedDirectory == null) {
            setFeedback("Pick a folder for the database.", "danger-text");
            return;
        }

        String name = nameField.getText().trim();
        if (name.isEmpty()) {
            setFeedback("Name your database file.", "danger-text");
            return;
        }

        String pwd = passwordField.getText();
        if (pwd.isEmpty()) {
            setFeedback("Enter a master password.", "danger-text");
            return;
        }

        String normalizedName = name.endsWith(".db") ? name : name + ".db";
        Path dbPath = selectedDirectory.toPath().resolve(normalizedName);

        CreateDatabaseController controller = new CreateDatabaseController();
        try {
            controller.createDatabase(dbPath.toString(), pwd);
            setFeedback("Database created at " + dbPath, "success-text");
            if (onCreated != null) {
                onCreated.accept(dbPath.toString());
            }
            close();
        } catch (SQLException ex) {
            setFeedback("Could not create database: " + ex.getMessage(), "danger-text");
        }
    }

    private void setFeedback(String message, String styleClass) {
        feedback.getStyleClass().removeAll("danger-text", "success-text", "muted");
        feedback.getStyleClass().add(styleClass);
        feedback.setText(message);
    }
}
