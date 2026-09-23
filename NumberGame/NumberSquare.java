package ca.bcit.comp2522.project.numberGame;

import javafx.scene.control.Button;

/**
 * Concrete JavaFX button that represents a single square on the number game board grid.
 * This class extends the standard {@code Button} control and implements the
 * {@code ClickableSquare} interface, binding the visual component to the game logic.
 * It encapsulates the index, value, and a reference to the main game controller {@code NumberGame}.
 * The button is styled using the {@value GRID_BUTTON_STYLE} CSS class.
 *
 * @author Ibrahim Abudalah
 * @version 1.0
 */
public final class NumberSquare extends Button implements ClickableSquare
{
    private static final String     GRID_BUTTON_STYLE = "grid-button";
    private static final int        DEFAULT_VALUE     = 0;
    private              int        value;
    private final        int        index;
    private final        NumberGame controller;

    /**
     * Constructs a {@code NumberSquare} instance.
     * This constructor links the visual button to a specific logical board index
     * and registers the game controller for handling click events. It initializes
     * the square with a default value, corresponding to an empty state.
     *
     * @param index      the board index corresponding to this square's position
     * @param controller the main game controller that handles all core game logic
     */
    public NumberSquare(final int index,
                        final NumberGame controller)
    {
        this.index      = index;
        this.controller = controller;
        this.value      = DEFAULT_VALUE;

        this.getStyleClass().add(GRID_BUTTON_STYLE);
        this.setOnAction(e -> onClick());
    }

    /**
     * Implements the single abstract method from the {@code ClickableSquare} interface.
     * This method is triggered when the user clicks the JavaFX button. It passes
     * the square's unique board index to the {@code NumberGame} controller's {@code handleSquareClick}
     * method for processing the move.
     */
    @Override
    public void onClick()
    {
        this.controller.handleSquareClick(this.index);
    }

    /**
     * Sets and visually updates the numeric value displayed on the square.
     * This method converts the integer value to a string and sets it as the button's text.
     *
     * @param value the positive integer number to display in the square
     */
    @Override
    public void setValue(final int value)
    {
        this.value = value;
        this.setText(String.valueOf(value));
    }

    /**
     * Disables the interaction capabilities of this square.
     * This is called by the controller {@code NumberGame} after a number has been successfully placed,
     * following the rule that filled squares cannot be reused.
     */
    @Override
    public void disable()
    {
        this.setDisable(true);
    }
}