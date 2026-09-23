package ca.bcit.comp2522.project.wordGame;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

/**
 * Represents a single scoring record for the WordGame.
 * <p>
 * This class is designed to handle score calculation, file reading, and file
 * writing in a format compatible with the ScoreTest unit tests.
 *
 * @author Ibrahim Abudalah
 * @version 1.0
 */
public final class Score
{
    private static final int               FIRST_ATTEMPT_POINTS  = 2;
    private static final int               SECOND_ATTEMPT_POINTS = 1;
    private static final int               ZERO                  = 0;
    private static final double            DOUBLE_ZERO           = 0.0;
    private static final DateTimeFormatter FORMATTER             =
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final String            PREFIX_DATE           = "Date and Time:";
    private static final String            PREFIX_GAMES          = "Games Played:";
    private static final String            PREFIX_FIRST          = "Correct First Attempts:";
    private static final String            PREFIX_SECOND         = "Correct Second Attempts:";
    private static final String            PREFIX_INCORRECT      = "Incorrect Attempts:";
    private static final String            PREFIX_TOTAL          = "Total Score:";
    private static final List<Score>       ALL_SCORES            = new ArrayList<>();
    private final        LocalDateTime     dateTimePlayed;
    private final        int               numGamesPlayed;
    private final        int               numCorrectFirstAttempt;
    private final        int               numCorrectSecondAttempt;
    private final        int               numIncorrectTwoAttempts;

    /**
     * Constructs a Score object, validating the input parameters.
     *
     * @param dateTimePlayed          the date and time the game was played
     * @param numGamesPlayed          the number of games played
     * @param numCorrectFirstAttempt  the number of correct first attempts
     * @param numCorrectSecondAttempt the number of correct second attempts
     * @param numIncorrectTwoAttempts the number of incorrect two attempts
     * @throws IllegalArgumentException if any numerical score values are negative or dateTimePlayed is null
     */
    public Score(final LocalDateTime dateTimePlayed,
                 final int numGamesPlayed,
                 final int numCorrectFirstAttempt,
                 final int numCorrectSecondAttempt,
                 final int numIncorrectTwoAttempts)
    {
        validateInput(dateTimePlayed,
                      numGamesPlayed,
                      numCorrectFirstAttempt,
                      numCorrectSecondAttempt,
                      numIncorrectTwoAttempts);

        this.dateTimePlayed          = dateTimePlayed;
        this.numGamesPlayed          = numGamesPlayed;
        this.numCorrectFirstAttempt  = numCorrectFirstAttempt;
        this.numCorrectSecondAttempt = numCorrectSecondAttempt;
        this.numIncorrectTwoAttempts = numIncorrectTwoAttempts;
    }

    /**
     * Validates the input for the Score constructor.
     *
     * @param dateTimePlayed          the date and time the game was played
     * @param numGamesPlayed          the number of games played
     * @param numCorrectFirstAttempt  the number of correct first attempts
     * @param numCorrectSecondAttempt the number of correct second attempts
     * @param numIncorrectTwoAttempts the number of incorrect two attempts
     * @throws IllegalArgumentException if any numerical score values are negative or dateTimePlayed is null
     */
    private static void validateInput(final LocalDateTime dateTimePlayed,
                                      final int numGamesPlayed,
                                      final int numCorrectFirstAttempt,
                                      final int numCorrectSecondAttempt,
                                      final int numIncorrectTwoAttempts)
    {
        if (dateTimePlayed == null)
        {
            throw new IllegalArgumentException("dateTimePlayed cannot be null.");
        }
        if (numGamesPlayed < ZERO ||
            numCorrectFirstAttempt < ZERO ||
            numCorrectSecondAttempt < ZERO ||
            numIncorrectTwoAttempts < ZERO)
        {
            throw new IllegalArgumentException("Score values cannot be negative.");
        }
    }

    /**
     * Returns the date and time the game was played, formatted as a string.
     *
     * @return the formatted date and time string
     */
    public String getFormattedDateTime()
    {
        return dateTimePlayed.format(FORMATTER);
    }

    /**
     * Calculates the total score based on the number of correct attempts.
     * This method name is EXACTLY required by the ScoreTest.
     *
     * @return the total score
     */
    public int getScore()
    {
        return (numCorrectFirstAttempt * FIRST_ATTEMPT_POINTS)
               + (numCorrectSecondAttempt * SECOND_ATTEMPT_POINTS);
    }

    /**
     * Calculates the average points per game played.
     *
     * @return the average points, or {@value DOUBLE_ZERO} if no games were played
     */
    public double getAveragePoints()
    {
        if (numGamesPlayed == ZERO)
        {
            return DOUBLE_ZERO;
        }
        return (double) getScore() / numGamesPlayed;
    }

    /**
     * Formats the score data into the exact multi-line string required for file output.
     *
     * @return the formatted string for the score file
     */
    public final String formatForFile()
    {
        final StringBuilder builder;
        builder = new StringBuilder();

        builder.append(PREFIX_DATE).append(" ").append(getFormattedDateTime()).append("\n")
               .append(PREFIX_GAMES).append(" ").append(numGamesPlayed).append("\n")
               .append(PREFIX_FIRST).append(" ").append(numCorrectFirstAttempt).append("\n")
               .append(PREFIX_SECOND).append(" ").append(numCorrectSecondAttempt).append("\n")
               .append(PREFIX_INCORRECT).append(" ").append(numIncorrectTwoAttempts).append("\n")
               .append(PREFIX_TOTAL).append(" ").append(getScore()).append(" points\n");

        return builder.toString();
    }

    /**
     * Appends the current score to the specified file.
     *
     * @param score    the Score object to append
     * @param filename the name of the file
     * @throws IOException          if an I/O error occurs
     * @throws NullPointerException if score or filename is null
     */
    public static void appendScoreToFile(final Score score,
                                         final String filename) throws IOException
    {
        if (score == null || filename == null)
        {
            throw new NullPointerException("Score and filename cannot be null.");
        }

        try (final FileWriter writer = new FileWriter(filename, true))
        {
            writer.write(score.toString());
            writer.write(System.lineSeparator());
        }
    }

    /**
     * Reads all score records from the specified file.
     * This method's logic and exception handling are dictated by ScoreTest.
     *
     * @param filename the name of the file
     * @return a List of Score objects read from the file, or an empty List if the file doesn't exist
     * @throws IOException if an I/O error occurs.
     */
    public static List<Score> readScoresFromFile(final String filename) throws IOException
    {
        final List<Score> list;
        list = new ArrayList<>();

        final File file;
        file = new File(filename);

        if (!file.exists())
        {
            return list;
        }

        try (final Scanner scanner = new Scanner(file))
        {
            while (scanner.hasNextLine())
            {
                String line;
                line = scanner.nextLine().trim();

                // Skip blank lines
                if (line.isEmpty())
                {
                    continue;
                }

                if (!line.startsWith(PREFIX_DATE))
                {
                    continue;
                }

                final String dateText;
                final LocalDateTime date;
                dateText = line.substring(PREFIX_DATE.length()).trim();

                date = LocalDateTime.parse(dateText, FORMATTER);

                final int games;
                final int first;
                final int second;
                final int incorrect;
                final int totalScore;

                games      = extractInt(scanner.nextLine());
                first      = extractInt(scanner.nextLine());
                second     = extractInt(scanner.nextLine());
                incorrect  = extractInt(scanner.nextLine());
                totalScore = extractInt(scanner.nextLine());

                list.add(new Score(date, games, first, second, incorrect));
            }
        }

        return list;
    }

    /**
     * Extracts an integer value from a string line by filtering out non-digit characters.
     *
     * @param text the line of text to process.
     * @return the extracted integer.
     */
    private static int extractInt(final String text)
    {
        final StringBuilder digits;
        digits = new StringBuilder();

        for (final char c : text.toCharArray())
        {
            if (Character.isDigit(c))
            {
                digits.append(c);
            }
        }
        return digits.isEmpty() ? ZERO : Integer.parseInt(digits.toString());
    }

    /**
     * Loads all scores from the specified file into the static ALL_SCORES list.
     * This method's simple exception handling is dictated by the original logic.
     *
     * @param filename the name of the file to load scores from
     * @throws IllegalArgumentException if scores can not be loaded from file.
     */
    public static void loadScores(final String filename)
    {
        ALL_SCORES.clear();

        try
        {
            final List<Score> read;
            read = readScoresFromFile(filename);
            ALL_SCORES.addAll(read);
        }
        catch (final Exception e)
        {
            throw new IllegalArgumentException("Could not load scores from file" + e);
        }
    }

    /**
     * Gets the previous high score based on the highest average points.
     *
     * @return the previous highest Score object, or null if no scores exist
     */
    public static Score getPreviousHighScore()
    {
        if (ALL_SCORES.isEmpty())
        {
            return null;
        }

        Score best;
        best = null;

        for (final Score s : ALL_SCORES)
        {
            if (best == null ||
                s.getAveragePoints() > best.getAveragePoints())
            {
                best = s;
            }
        }
        return best;
    }

    /**
     * Generates a congratulatory or informational message based on whether
     * the current score is a new high score.
     *
     * @param previous the previous high score, or null if this is the first score
     * @return the formatted high score message string
     */
    public String getHighScoreMessage(final Score previous)
    {
        if (previous == null)
        {
            return "\nCONGRATULATIONS! You set the first high score!\n";
        }

        final double prevAvg;
        final double thisAvg;
        prevAvg = previous.getAveragePoints();
        thisAvg = getAveragePoints();

        if (thisAvg > prevAvg)
        {
            return String.format(
                "%nCONGRATULATIONS! You are the new high score with an average of %.2f points per game; " +
                "the previous record was %.2f points per game on %s.%n",
                thisAvg, prevAvg, previous.getFormattedDateTime());
        }

        return String.format(
            "%nYou did not beat the high score of %.2f points per game from %s.%n",
            prevAvg, previous.getFormattedDateTime());
    }

    /**
     * Returns the string representation of the Score object, matching the
     * exact required format for the ScoreTest.
     *
     * @return the score data as a formatted string
     */
    @Override
    public String toString()
    {
        return formatForFile();
    }

    /**
     * Compares this Score object with the specified object for equality.
     * Checks if the other object is also a Score with identical final field values.
     *
     * @param obj the object to compare against
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(final Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (!(obj instanceof Score other))
        {
            return false;
        }

        return Objects.equals(dateTimePlayed, other.dateTimePlayed)
               && numGamesPlayed == other.numGamesPlayed
               && numCorrectFirstAttempt == other.numCorrectFirstAttempt
               && numCorrectSecondAttempt == other.numCorrectSecondAttempt
               && numIncorrectTwoAttempts == other.numIncorrectTwoAttempts;
    }

    /**
     * Generates a hash code for this Score object based on its final fields.
     *
     * @return the hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(
            dateTimePlayed,
            numGamesPlayed,
            numCorrectFirstAttempt,
            numCorrectSecondAttempt,
            numIncorrectTwoAttempts
                           );
    }
}