package ca.bcit.comp2522.project.numberGame;

import java.util.Arrays;

/**
 * Abstract class containing all the core logic for the Number Game board state.
 * It manages the internal array storage of twenty numbers and enforces the
 * primary game rule of strictly ascending numerical placement from left to right.
 * Because the game logic is independent of the UI, this class is declared {@code abstract}.
 *
 * @author Ibrahim Abudalah
 * @version 1.0
 */
public abstract class GameBoard
{
    protected static final int   NUM_SQUARES  = 20;
    protected static final int   MIN_VALUE    = 1;
    protected static final int   MAX_VALUE    = 1000;
    private static final   int   EMPTY_SQUARE = 0;
    private static final   int   MIN_INDEX    = 0;
    protected final        int[] values;

    /**
     * Constructs a new {@code GameBoard} instance.
     * Initializes the internal array to the exact size required ({@value NUM_SQUARES})
     * and sets all elements to the defined empty square value.
     */
    protected GameBoard()
    {
        this.values = new int[NUM_SQUARES];
        Arrays.fill(values, EMPTY_SQUARE);
    }

    /**
     * Attempts to place a given number value at a specified index on the board.
     * This method first validates the index and value range, checks if the square
     * is empty, and finally validates the placement against the ascending rule
     * relative to all existing neighbors.
     *
     * @param index the square index where the value should be placed
     * @param value the positive integer number attempting to be placed
     * @return {@code true} if the placement is valid and the number is successfully placed,
     * {@code false} if the placement violates the ascending rule or the square is already has a value.
     * @throws IllegalArgumentException if the provided index or value is outside the defined valid range
     */
    public boolean placeValue(final int index,
                              final int value)
    {
        validateIndex(index);
        validateValue(value);

        // Prevent overwriting a placed number.
        if (values[index] != EMPTY_SQUARE)
        {
            return false;
        }

        // Enforce the core game logic.
        if (!isPlacementValid(index, value))
        {
            return false;
        }

        values[index] = value;
        return true;
    }

    /**
     * Determines if a value can be placed at a specified index without violating
     * the ascending constraint. The rule mandates:
     * All non-empty values to the left must be less than or equal to the new value,
     * and all non-empty values to the right must be greater than or equal to the new value.
     *
     * @param index the position to check for placement
     * @param value the number being tested for validity
     * @return {@code true} if the placement satisfies the ascending rule {@code false} otherwise
     */
    private boolean isPlacementValid(final int index,
                                     final int value)
    {

        // Fails if any existing left value is strictly greater than the new value.
        for (int i = index - 1; i >= MIN_INDEX; i--)
        {
            if (values[i] != EMPTY_SQUARE && values[i] > value)
            {
                return false;
            }
        }

        // Fails if any existing right value is strictly less than the new value.
        for (int i = index + 1; i < NUM_SQUARES; i++)
        {
            if (values[i] != EMPTY_SQUARE && values[i] < value)
            {
                return false;
            }
        }

        return true;
    }

    /**
     * Validation method to ensure the provided index is a legal position
     * on the board (between {@value MIN_INDEX} and {@value NUM_SQUARES} minus {@value MIN_VALUE}).
     * This promotes code reuse and encapsulation of validation logic.
     *
     * @param index the index value to validate
     * @throws IllegalArgumentException if the index is less than {@value MIN_INDEX} or greater
     *                                  than or equal to the total number of squares.
     */
    private static void validateIndex(final int index)
    {
        if (index < MIN_INDEX || index >= NUM_SQUARES)
        {
            throw new IllegalArgumentException("Invalid square index.");
        }
    }

    /**
     * Validation method to ensure the number value is within the
     * acceptable game range (between {@value MIN_VALUE} and
     * {@value MAX_VALUE}). This method guarantees a valid state for
     * the instance data.
     *
     * @param value the number value to validate
     * @throws IllegalArgumentException if the value is outside the {@code MIN_VALUE} and {@code MAX_VALUE} bounds
     */
    private static void validateValue(final int value)
    {
        if (value < MIN_VALUE || value > MAX_VALUE)
        {
            throw new IllegalArgumentException("Invalid board value.");
        }
    }

    /**
     * Iterates through the internal storage array to count how many squares
     * currently hold a valid number (are not equal to the empty square value).
     *
     * @return the count of filled squares on the board
     */
    public int getPlacedCount()
    {
        int count = EMPTY_SQUARE;
        for (final int v : values)
        {
            if (v != EMPTY_SQUARE)
            {
                count++;
            }
        }
        return count;
    }

    /**
     * Resets the entire board state to its initial condition.
     * This is achieved by filling the array with the {@value MIN_INDEX} value, effectively
     * clearing all previously placed numbers.
     */
    public void reset()
    {
        Arrays.fill(values, EMPTY_SQUARE);
    }
}