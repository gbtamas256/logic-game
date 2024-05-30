package tiltingpuzzle.game;

import javafx.application.Platform;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import org.tinylog.Logger;
import tiltingpuzzle.model.Direction;
import tiltingpuzzle.model.PuzzleState;
import util.OrdinalImageStorage;
import util.javafx.ImageStorage;

/**
 * Controller class for the tilting puzzle.
 */
public class PuzzleController {

    private static final ImageStorage<Integer> imageStorage = new OrdinalImageStorage(PuzzleController.class,
            "red_ball.png",
            "blue_ball.png");

    @FXML
    private GridPane grid;

    @FXML
    private TextField numberOfMovesField;

    private PuzzleState state;

    private final IntegerProperty numberOfMoves = new SimpleIntegerProperty(0);

    @FXML
    private void initialize() {
        bindNumberOfMoves();
        registerKeyEventHandler();
        restartGame();
    }

    private void bindNumberOfMoves() {
        numberOfMovesField.textProperty().bind(numberOfMoves.asString());
    }

    private void restartGame() {
        createState();
        numberOfMoves.set(0);
        clearAndPopulateGrid();
    }

    private void createState() {
        state = new PuzzleState();
        state.solvedProperty().addListener(this::handleSolved);
    }

    private void handleSolved(ObservableValue<? extends Boolean> observableValue, Boolean oldValue, Boolean newValue) {
        if (newValue) {
            Platform.runLater(this::showSolvedAlert);
        }
    }

    private void showSolvedAlert() {
        var alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Game Over");
        alert.setContentText("You have solved the puzzle!");
        alert.showAndWait();
        restartGame();
    }

    private void registerKeyEventHandler() {
        Platform.runLater(() -> grid.getScene().setOnKeyPressed(this::handleKeyPress));
    }

    @FXML
    private void handleKeyPress(KeyEvent keyEvent) {
        var restartKeyCombination = new KeyCodeCombination(KeyCode.R, KeyCombination.CONTROL_DOWN);
        var quitKeyCombination = new KeyCodeCombination(KeyCode.Q, KeyCombination.CONTROL_DOWN);
        if (restartKeyCombination.match(keyEvent)) {
            Logger.debug("Restarting game");
            restartGame();
        } else if (quitKeyCombination.match(keyEvent)) {
            Logger.debug("Exiting");
            Platform.exit();
        }
    }

    @FXML
    private void handleUpButtonClick(MouseEvent event) {
        Logger.debug("Up button pressed");
        makeMoveIfLegal(Direction.UP);
    }
    @FXML
    private void handleRightButtonClick(MouseEvent event) {
        Logger.debug("Right button pressed");
        makeMoveIfLegal(Direction.RIGHT);
    }
    @FXML
    private void handleDownButtonClick(MouseEvent event) {
        Logger.debug("Down button pressed");
        makeMoveIfLegal(Direction.DOWN);
    }
    @FXML
    private void handleLeftButtonClick(MouseEvent event) {
        Logger.debug("Left button pressed");
        makeMoveIfLegal(Direction.LEFT);
    }

    private void makeMoveIfLegal(Direction direction) {
        if (state.isLegalMove(direction)) {
            Logger.info("Moving {}", direction);
            state.makeMove(direction);
            Logger.trace("New state after move: {}", state);
            numberOfMoves.set(numberOfMoves.get() + 1);
        } else {
            Logger.warn("Illegal move: {}", direction);
        }
    }

    private void clearAndPopulateGrid() {
        grid.getChildren().clear();
        for (var row = 0; row < grid.getRowCount(); row++) {
            for (var col = 0; col < grid.getColumnCount(); col++) {
                var square = createSquare(row, col);
                grid.add(square, col, row);
            }
        }
    }

    private StackPane createSquare(int row, int col) {
        var square = new StackPane();
        square.getStyleClass().add("square");
        square.getStyleClass().add((row + col) % 2 == 0 ? "light": "dark");
        for (var i = 0; i < 2; i++) {
            var imageView = createImageViewForPieceOnPosition(i, row, col);
            square.getChildren().add(imageView);
        }
        return square;
    }

    private ImageView createImageViewForPieceOnPosition(int index, int row, int col) {
        var imageView = new ImageView(imageStorage.get(index).orElseThrow());
        imageView.visibleProperty().bind(createBindingToCheckPieceIsOnPosition(index, row, col));
        return imageView;
    }

    private BooleanBinding createBindingToCheckPieceIsOnPosition(int index, int row, int col) {
        return new BooleanBinding() {
            {
                super.bind(state.positionProperty(index));
            }
            @Override
            protected boolean computeValue() {
                var position = state.getPosition(index);
                return position.row() == row && position.col() == col;
            }
        };
    }
}
