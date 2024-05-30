package tiltingpuzzle.game;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Application class to load the starting screen.
 */
public class StartApplication extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/start.fxml"));
        stage.setTitle("Start");
        stage.setScene(new Scene(root));
        stage.show();
    }
}
