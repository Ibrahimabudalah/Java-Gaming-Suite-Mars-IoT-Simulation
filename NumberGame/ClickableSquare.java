package ca.bcit.comp2522.project.numberGame;

/**
 * Represents a contract for a clickable square component within the Number Game GUI.
 * This interface establishes an abstraction layer, separating the concrete UI element (like a button)
 * from the high level game logic the {@code NumberGame} and the {@code GameBoard}).
 * Any class that implements this interface must define how to handle user clicks, display
 * a value, retrieve its value, and disable itself after use.
 *
 * @author Ibrahim Abudalah
 * @version 1.0
 */
public interface ClickableSquare
{
    /**
     * Executes the necessary actions when the user interacts with clicks this square.
     * This method typically delegates the event handling back to the main game controller.
     */
    void onClick();

    /**
     * Assigns and displays a new numeric value within the square's UI representation.
     * This method is responsible for updating the visual state of the square.
     *
     * @param value the positive integer number to be displayed in the square
     */
    void setValue(int value);

    /**
     * Renders the square unusable after a successful placement, typically by visually
     * disabling the associated UI component to prevent further clicks.
     */
    void disable();
}