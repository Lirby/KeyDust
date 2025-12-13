package keydust.gui.core;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class Builder {

    private Builder() {
    }

    public static Scene createScene(Region content, double width, double height) {
        StackPane root = buildRoot(content);
        Scene scene = new Scene(root, width, height);
        loadStyles(scene);
        return scene;
    }

    private static void loadStyles(Scene scene) {
        URL stylesheet = Builder.class.getResource("theme.css");
        if (stylesheet != null) {
            scene.getStylesheets().add(stylesheet.toExternalForm());
            return;
        }

        Path fallback = Paths.get("src/main/resources/css/theme.css");
        if (Files.exists(fallback)) {
            scene.getStylesheets().add(fallback.toUri().toString());
        }
    }

    private static StackPane buildRoot(Region content) {
        StackPane root = new StackPane();

        Pane gradient = new Pane();
        gradient.getStyleClass().add("gradient");
        gradient.setEffect(new GaussianBlur(48));
        gradient.setMouseTransparent(true);
        gradient.prefWidthProperty().bind(root.widthProperty());
        gradient.prefHeightProperty().bind(root.heightProperty());

        Pane accentLeft = new Pane();
        accentLeft.getStyleClass().addAll("accent-bubble", "accent-left");
        accentLeft.setPrefSize(360, 360);
        accentLeft.setTranslateX(-260);
        accentLeft.setTranslateY(-200);
        accentLeft.setRotate(-6);
        accentLeft.setMouseTransparent(true);

        Pane accentRight = new Pane();
        accentRight.getStyleClass().addAll("accent-bubble", "accent-right");
        accentRight.setPrefSize(360, 360);
        accentRight.setTranslateX(280);
        accentRight.setTranslateY(220);
        accentRight.setRotate(8);
        accentRight.setMouseTransparent(true);

        Pane noiseOverlay = new Pane();
        noiseOverlay.getStyleClass().add("noise");
        noiseOverlay.setMouseTransparent(true);
        noiseOverlay.prefWidthProperty().bind(root.widthProperty());
        noiseOverlay.prefHeightProperty().bind(root.heightProperty());

        StackPane card = wrapInGlass(content);

        root.getChildren().addAll(gradient, accentLeft, accentRight, noiseOverlay, card);
        return root;
    }

    public static StackPane wrapInGlass(Region content) {
        content.getStyleClass().add("card-content");

        StackPane card = new StackPane(content);
        card.getStyleClass().add("glass-card");
        card.setMaxWidth(980);
        StackPane.setAlignment(card, Pos.CENTER);
        return card;
    }
}
