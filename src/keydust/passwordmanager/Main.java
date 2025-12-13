package keydust.passwordmanager;

import javafx.application.Application;
import javafx.stage.Stage;
import keydust.gui.UnlockGui;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        UnlockGui unlockGui = new UnlockGui(primaryStage);
        unlockGui.show();
    }

    public static void main(String[] args){
        launch(args);
    }
}
