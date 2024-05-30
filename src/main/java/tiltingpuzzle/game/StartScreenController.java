package tiltingpuzzle.game;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Controller class for the staring screen.
 */
public class StartScreenController {

    @FXML
    private TextField nameField;

    @FXML
    private void initialize() {
        nameField.setText(System.getProperty("user.name"));
    }
    @FXML
    private void playButton(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/puzzle.fxml"));
        stage.setScene(new Scene(root));
        stage.show();
    }
}
