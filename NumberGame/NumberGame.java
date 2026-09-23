package ca.bcit.comp2522.project.numberGame;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Objects;
import java.util.Random;

/**
 * JavaFX GUI application class that puts together the entire Number Game.
 * This class extends {@code javafx.application.Application}, handling the game lifecycle,
 * all UI construction, event binding, game state management, and score tracking.
 * It serves as the main controller, linking the visual squares ({@code NumberSquare})
 * with the game rules ({@code GameBoard}). Since it manages the application state,
 * it is declared {@code final} to prevent subclassing, promoting stability and encapsulation.
 *
 * @author Ibrahim Abudalah
 * @version 1.0
 */
public final class NumberGame extends Application
{
    private static final int            NUM_SQUARES                = 20;
    private static final int            ROWS                       = 4;
    private static final int            COLS                       = 5;
    private static final int            ZERO_COUNT                 = 0;
    private static final double         ZERO_DOUBLE_VALUE          = 0.0;
    private static final int            ROOT_SPACING               = 20;
    private static final String         ROOT_STYLE                 = "root";
    private static final int            SCENE_WIDTH                = 600;
    private static final int            SCENE_HEIGHT               = 600;
    private static final int            DIALOG_WIDTH               = 550;
    private static final int            DIALOG_HEIGHT              = 450;
    private static final String         DIALOG_TITLE               = "Game Over";
    private static final int            DIALOG_SPACING             = 15;
    private static final String         DIALOG_VBOX_STYLE          = "dialog-vbox";
    private static final String         DIALOG_MESSAGE_STYLE       = "dialog-message";
    private static final String         DIALOG_BUTTON_STYLE        = "dialog-button";
    private static final int            BUTTON_SPACING             = 10;
    private static final String         APP_TITLE                  = "COMP2522 Number Game";
    private static final String         STYLESHEET_PATH            = "/resources/styles.css";
    private static final String         EMPTY_TEXT                 = "";
    private static final String         CURRENT_NUMBER_LABEL_STYLE = "current-number-label";
    private static final String         GRID_PANE_STYLE            = "grid-pane";
    private static final String         TRY_AGAIN_TEXT             = "Try Again";
    private static final String         QUIT_TEXT                  = "Quit";
    private static final String         PLACE_NUMBER_PROMPT        = "Place this number: ";
    private final        Random         random                     = new Random();
    private final        GameBoard      board                      = new GameBoard()
    {
    };
    private final        NumberSquare[] squares;
    private final        Label          currentNumberLabel;
    private final        Stage          stage;
    private              int            gamesPlayed                = ZERO_COUNT;
    private              int            gamesWon                   = ZERO_COUNT;
    private              int            gamesLost                  = ZERO_COUNT;
    private              int            totalPlacements            = ZERO_COUNT;
    private              int            currentNumber;


    /**
     * Constructs the {@code NumberGame}, initializing all final UI component fields.
     * The initial assembly of the scene graph is performed here, setting up the grid
     * and binding event handlers to the squares. This preparation ensures that the
     * scene is ready before {@code start()} is called by the JavaFX runtime.
     */
    public NumberGame()
    {
        currentNumberLabel = new Label();
        currentNumberLabel.getStyleClass().add(CURRENT_NUMBER_LABEL_STYLE);

        final GridPane grid;
        grid = new GridPane();
        grid.getStyleClass().add(GRID_PANE_STYLE);
        grid.setAlignment(Pos.CENTER);

        final NumberSquare[] squaresArray;
        squaresArray = new NumberSquare[NUM_SQUARES];
        int idx = ZERO_COUNT;

        // Loop to create and position the individual NumberSquare components in the grid.
        for (int row = ZERO_COUNT; row < ROWS; row++)
        {
            // Inner loop iterates over cols.
            for (int col = ZERO_COUNT; col < COLS; col++)
            {
                final NumberSquare sq;
                sq                = new NumberSquare(idx, this);
                squaresArray[idx] = sq;
                grid.add(sq, col, row);
                idx++;
            }
        }
        squares = squaresArray;

        final VBox root;
        root = new VBox(ROOT_SPACING, currentNumberLabel, grid);
        root.getStyleClass().add(ROOT_STYLE);
        root.setAlignment(Pos.TOP_CENTER);

        final Scene scene;
        scene = new Scene(root, SCENE_WIDTH, SCENE_HEIGHT);

        // Loads the external CSS file
        scene.getStylesheets().add(
            Objects.requireNonNull(
                getClass().getResource(STYLESHEET_PATH)
                                  ).toExternalForm()
                                  );

        final Stage initStage;
        initStage = new Stage();
        initStage.setTitle(APP_TITLE);
        initStage.setScene(scene);
        stage = initStage;
    }


    /**
     * Prepares the JavaFX environment to launch the game.
     * This static method serves as the public entry point, ensuring the application
     * is launched safely on the JavaFX Application thread using {@code Platform.runLater}.
     */
    public static void play()
    {
        Platform.runLater(() ->
                          {
                              try
                              {
                                  final Stage primaryStage;
                                  primaryStage = new Stage();
                                  // Instantiates the NumberGame and calls its entry point.
                                  new NumberGame().start(primaryStage);
                              }
                              catch (final Exception e)
                              {
                                  System.err.println("Unexpected error: " + e.getMessage());

                              }
                          });
    }

    /**
     * The primary entry point for the JavaFX application life cycle.
     * It sets the title, links the scene created in the constructor to the stage,
     * displays the window, and initiates the first round of the game by calling
     * {@code startNewRound()}.
     *
     * @param primaryStage the primary stage for this application, provided by the JavaFX runtime
     */
    @Override
    public void start(final Stage primaryStage)
    {
        stage.show();
        startNewRound();
    }

    /**
     * Resets the game state for a new round without clearing accumulated statistics
     * (win/loss record). This method clears the {@code GameBoard}, resets all
     * visual squares, and generates the initial random number for the player.
     */
    private void startNewRound()
    {
        board.reset();

        // Reset all visual squares to their initial empty, enabled state
        for (final NumberSquare sq : squares)
        {
            sq.setText(EMPTY_TEXT);
            sq.setDisable(false);
        }

        // Generate a new random number in the valid range [MIN_VALUE, MAX_VALUE]
        currentNumber = random.nextInt(GameBoard.MAX_VALUE) + GameBoard.MIN_VALUE;
        currentNumberLabel.setText(PLACE_NUMBER_PROMPT + currentNumber);
    }

    /**
     * Processes a user's attempt to place the current number. This is the central
     * method for game flow: it validates the move using the {@code GameBoard}
     * logic, updates the UI and statistics upon success, checks for the win condition,
     * or triggers a game over state on failure.
     *
     * @param index the square index that was clicked by the user
     */
    public void handleSquareClick(final int index)
    {
        final boolean placed;
        placed = board.placeValue(index, currentNumber);

        if (!placed)
        {
            // Invalid placement due to ascending rule violation or occupied square.
            showGameOverDialog(false, "Invalid placement! You lose.");
            return;
        }

        // On successful placement, update the UI and track stats.
        squares[index].setValue(currentNumber);
        squares[index].disable();

        totalPlacements++;

        // Check for win condition once board is full
        if (board.getPlacedCount() == NUM_SQUARES)
        {
            showGameOverDialog(true, "Board Full! You WIN!");
            return;
        }

        // Generate the next number
        currentNumber = random.nextInt(GameBoard.MAX_VALUE) + GameBoard.MIN_VALUE;
        currentNumberLabel.setText(PLACE_NUMBER_PROMPT + currentNumber);
    }

    /**
     * Displays a styled, modal end of game popup window with the game result
     * and cumulative statistics. It increments the appropriate game count (won/lost)
     * before generating the summary.
     *
     * @param won     A boolean indicating if the game was won (true) or lost (false)
     * @param message A string detailing the reason for the game ending (win message or loss reason).
     */
    private void showGameOverDialog(final boolean won,
                                    final String message)
    {
        // Update game stats
        gamesPlayed++;
        if (won)
        {
            gamesWon++;
        }
        else
        {
            gamesLost++;
        }

        // UI setup dialog root
        final VBox dialogRoot;
        dialogRoot = new VBox(DIALOG_SPACING);
        dialogRoot.getStyleClass().add(DIALOG_VBOX_STYLE);
        dialogRoot.setAlignment(Pos.CENTER);

        final Label msg;
        msg = new Label(message);
        msg.getStyleClass().add(DIALOG_MESSAGE_STYLE);

        final String summaryText;
        summaryText = buildScoreSummary();

        final Label summaryLabel;
        summaryLabel = new Label(summaryText);
        summaryLabel.getStyleClass().add(DIALOG_MESSAGE_STYLE);
        summaryLabel.setWrapText(true);
        summaryLabel.setAlignment(Pos.CENTER);

        // Buttons
        final Button tryAgain;
        tryAgain = new Button(TRY_AGAIN_TEXT);
        tryAgain.getStyleClass().add(DIALOG_BUTTON_STYLE);
        final Button quit;
        quit = new Button(QUIT_TEXT);
        quit.getStyleClass().add(DIALOG_BUTTON_STYLE);

        final VBox buttons;
        buttons = new VBox(BUTTON_SPACING, tryAgain, quit);
        buttons.setAlignment(Pos.CENTER);

        // Put together dialog content
        dialogRoot.getChildren().addAll(msg, summaryLabel, buttons);

        // Stage setup (popup window)
        final Stage popup;
        popup = new Stage();
        popup.setTitle(DIALOG_TITLE);
        popup.setScene(new Scene(dialogRoot, DIALOG_WIDTH, DIALOG_HEIGHT));

        // Apply styles to the popup
        popup.getScene().getStylesheets().add(
            Objects.requireNonNull(getClass().getResource(STYLESHEET_PATH))
                   .toExternalForm()
                                             );

        // Event handler to close the popup and start a new game without loosing previous score.
        tryAgain.setOnAction(e ->
                             {
                                 popup.close();
                                 startNewRound();
                             });

        // Event handler to close the popup and stage.
        quit.setOnAction(e ->
                         {
                             popup.close();
                             if (this.stage != null)
                             {
                                 this.stage.close();
                             }
                         });

        popup.show();
    }


    /**
     * Constructs a detailed string summary of the accumulated game statistics,
     * including the win/loss record and the average successful placements per game.
     * The method uses {@code StringBuilder} for efficient string construction,
     * accommodating the required conditional phrasing based on win/loss records.
     *
     * @return A string containing the full score summary
     */
    private String buildScoreSummary()
    {
        final double avg;
        avg = calculateAverage();

        final StringBuilder result;
        result = new StringBuilder();

        if (gamesWon > ZERO_COUNT && gamesLost > ZERO_COUNT)
        {
            result.append("You won ")
                  .append(gamesWon)
                  .append(" out of ")
                  .append(gamesPlayed)
                  .append(" games and you lost ")
                  .append(gamesLost)
                  .append(" out of ")
                  .append(gamesPlayed)
                  .append(" games");
        }
        else if (gamesWon > ZERO_COUNT)
        {
            result.append("You won ")
                  .append(gamesWon)
                  .append(" out of ")
                  .append(gamesPlayed)
                  .append(" games");
        }
        else
        {
            result.append("You lost ")
                  .append(gamesLost)
                  .append(" out of ")
                  .append(gamesPlayed)
                  .append(" games");
        }

        final String avgFormat = "%.2f";
        final String averageString;
        averageString = String.format(avgFormat, avg);

        result.append(", with ")
              .append(totalPlacements)
              .append(" successful placements, an average of ")
              .append(averageString)
              .append(" per game.");

        return result.toString();
    }

    /**
     * Computes the average number of successful placements made per game played.
     * Ensures against division by {@value ZERO_COUNT} by checking if any games have been played.
     *
     * @return the average placements per game, or {@value ZERO_DOUBLE_VALUE} if no games have been completed
     */
    private double calculateAverage()
    {
        if (gamesPlayed == ZERO_COUNT)
        {
            return ZERO_DOUBLE_VALUE;
        }
        return (double) totalPlacements / gamesPlayed;
    }
}