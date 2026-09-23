package ca.bcit.comp2522.project;

import ca.bcit.comp2522.project.myGame.MyGame;
import ca.bcit.comp2522.project.numberGame.NumberGame;
import ca.bcit.comp2522.project.wordGame.WordGame;
import javafx.application.Platform;

import java.io.IOException;
import java.util.Scanner;

/**
 * Acts as the dedicated application bootstrap and text-based navigation hub for the COMP term project games.
 * This final utility class contains only static methods, providing the initial control flow loop
 * that permits users to launch the Word Game, Number Game, MyGame (ESP Mission Control), or terminate the program.
 *
 * @author Ibrahim Abudalah
 * @version 1.0
 */
public final class Main
{
    private static final String WORD_GAME_OPTION   = "W";
    private static final String NUMBER_GAME_OPTION = "N";
    private static final String MY_GAME_OPTION     = "M";
    private static final String QUIT_OPTION        = "Q";

    /**
     * Private constructor to explicitly prevent the instantiation of this static utility class.
     * Declaring the constructor private enforces the design principle that this class should only
     * be utilized by its static methods, eliminating the need for object instances.
     */
    private Main()
    {
        // Intentionally empty to prevent instantiation.
    }

    /**
     * This is the program driver.
     *
     * @param args an array of command line arguments.
     * @throws IOException if any underlying I/O operation fails during game instantiation.
     */
    public static final void main(final String[] args) throws IOException
    {
        final Scanner scanner;
        scanner = new Scanner(System.in);

        boolean active;
        active = true;

        Platform.startup(() -> Platform.setImplicitExit(false));

        while (active)
        {
            displayMenu();
            final String input;
            input  = scanner.nextLine().trim().toUpperCase();
            active = processUserInput(input, scanner, active);
        }

        scanner.close();
        Platform.exit();
    }

    /**
     * Prints the interactive game selection menu to the console.
     * This method is responsibility for presenting all games selections for user interaction.
     * All options are referenced using symbolic constants {@value WORD_GAME_OPTION} to link the display.
     */
    private static void displayMenu()
    {
        System.out.println("Press " + WORD_GAME_OPTION + " to play the Word game.");
        System.out.println("Press " + NUMBER_GAME_OPTION + " to play the Number game.");
        System.out.println("Press " + MY_GAME_OPTION + " to play the ESP Mission Control game.");
        System.out.println("Press " + QUIT_OPTION + " to quit.");
    }


    /**
     * This method acts as the router for the project's navigation.
     * <p>
     * Project routing logic is:
     * Case W: Launches {@code WordGame} static {@code play} method, passing the shared {@code Scanner}.
     * Case N: Launches the JavaFX {@code NumberGame.play()} static launcher.
     * Case M: Instantiates and launches {@code MyGame}.
     * Case Q: Terminates the program.
     * Default: Prints an error and continues to get use input.
     *
     * @param input        the user's menu choice.
     * @param scanner      the shared {@code Scanner} instance, passed through to games requiring console input
     * @param currentState the current boolean flag indicating whether the main loop should proceed
     * @return updatedState boolean state of the application loop; {@code false} only if the quit option was selected
     * @throws IOException if file or resource access fails during the instantiation or execution.
     */
    private static boolean processUserInput(final String input,
                                            final Scanner scanner,
                                            final boolean currentState) throws IOException
    {
        boolean updatedState;
        updatedState = currentState;

        switch (input)
        {
            case WORD_GAME_OPTION:
                System.out.println("\nStarting Word Game...");
                WordGame.play(scanner);
                break;
            case NUMBER_GAME_OPTION:
                System.out.println("\nStarting Number Game...");
                NumberGame.play();
                break;
            case MY_GAME_OPTION:
                System.out.println("\nStarting ESP Mission Control Game...");
                final MyGame game;
                game = new MyGame();
                game.play();
                break;
            case QUIT_OPTION:
                updatedState = false;
                System.out.println("Goodbye!");
                break;
            default:
                System.out.println("Invalid input! Please press " +
                                   WORD_GAME_OPTION + ", " + NUMBER_GAME_OPTION + ", " +
                                   MY_GAME_OPTION + ", or " + QUIT_OPTION + ".");
                break;
        }
        return updatedState;
    }
}