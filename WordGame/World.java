package ca.bcit.comp2522.project.wordGame;

import java.io.*;
import java.util.*;

/**
 * Loads all countries from the resource files located in the project src.
 * Each country entry, including its name, capital, and facts, is parsed and stored internally.
 * The primary role of this class is to manage the dataset and provide random countries to the game logic.
 * All countries are stored in a {@code HashMap} where the key is the country name.
 *
 * @author Ibrahim Abudalah
 * @version 1.0
 */
public final class World
{
    private static final char                 START_LETTER_A   = 'a';
    private static final char                 END_LETTER_Z     = 'z';
    private static final int                  NUM_FACTS        = 3;
    private static final int                  HEADER_PARTS     = 2;
    private static final String               RESOURCE_PREFIX  = "/resources/";
    private static final String               FILE_EXTENSION   = ".txt";
    private static final char                 SKIP_LETTER_W    = 'w';
    private static final char                 SKIP_LETTER_X    = 'x';
    private static final String               HEADER_DELIMITER = ":";
    private final        Map<String, Country> countries;
    private final        Random               random;

    /**
     * Constructs a {@code World} object.
     * Initializes the internal storage map and the random number generator,
     * then immediately triggers the loading of all country data from the A-Z files.
     *
     * @throws IllegalStateException if any resource file cannot be loaded or an I/O error occurs during file reading
     */
    public World()
    {
        this.countries = new HashMap<>();
        this.random    = new Random();

        loadAllFiles();
    }

    /**
     * Iterates through all potential resource files, from a.txt through z.txt.
     * Files corresponding to letters 'w' and 'x' are skipped as they contain no data.
     * This method ensures all available country data is loaded into the map.
     *
     * @throws IllegalStateException if a file error occurs during loading a specific file.
     */
    private void loadAllFiles()
    {
        for (char letter = START_LETTER_A; letter <= END_LETTER_Z; letter++)
        {
            if (letter == SKIP_LETTER_W || letter == SKIP_LETTER_X)
            {
                // Skip w.txt and x.txt.
                continue;
            }

            final String filename;
            // Constructs the full resource path
            filename = RESOURCE_PREFIX + letter + FILE_EXTENSION;
            loadFile(filename);
        }
    }

    /**
     * Loads a single resource file from the path.
     *
     * @param filename the resource file path to load.
     * @throws IllegalStateException if the resource is not found.
     */
    private void loadFile(final String filename)
    {
        InputStream input = World.class.getResourceAsStream(filename);

        if (input == null)
        {
            throw new IllegalStateException("File not found: " + filename);
        }

        try (Scanner scanner = new Scanner(input))
        {
            parseCountries(scanner);
        }
    }


    /**
     * Parses all country data entries found in a single resource file.
     * Data is read and expecting a header line "Country:Capital" followed by
     * exactly {@value NUM_FACTS} fact lines, potentially followed by a blank separator line.
     *
     * @param reader the {@code Scanner} for the open resource file.
     * @throws IllegalArgumentException if the file content format is incorrect.
     */
    private void parseCountries(final Scanner reader)
    {

        while (reader.hasNextLine())
        {
            String line = reader.nextLine().trim();

            if (line.isBlank())
            {
                continue;
            }

            // Processing the country & capital line
            final String header;
            header = line.trim();
            final String[] nameParts;
            nameParts = header.split(HEADER_DELIMITER);

            validateHeader(nameParts);

            final String countryName;
            countryName = nameParts[0].trim();
            final String capitalName;
            capitalName = nameParts[1].trim();

            // Processes the facts lines.
            final String[] facts = new String[NUM_FACTS];
            int i;
            for (i = 0; i < NUM_FACTS; i++)
            {
                final String factLine;
                factLine = reader.nextLine();
                validateFact(factLine);
                facts[i] = factLine.trim();
            }

            if (reader.hasNextLine())
            {
                reader.nextLine();
            }

            // Store immutable country object.
            final Country country;
            country = new Country(countryName, capitalName, facts);
            this.countries.put(countryName, country);
        }
    }

    /**
     * Validates that the split header line contains exactly {@value HEADER_PARTS} parts.
     *
     * @param parts the array of strings resulting from splitting the header line by the delimiter
     * @throws IllegalArgumentException if the array length does not equal {@value HEADER_PARTS}
     */
    private static void validateHeader(final String[] parts)
    {
        if (parts.length != HEADER_PARTS)
        {
            throw new IllegalArgumentException(
                "Header format invalid. Expected 'Country:Capital'.");
        }
    }

    /**
     * Validates that a fact line read from the resource file is non-null and non-blank.
     *
     * @param fact the fact line string to validate.
     * @throws IllegalArgumentException if the fact line is null or blank
     */
    private static void validateFact(final String fact)
    {
        if (fact == null || fact.isBlank())
        {
            throw new IllegalArgumentException("Fact line empty or blank in resource file.");
        }
    }

    /**
     * Retrieves a {@code Country} object by its name using the map.
     *
     * @param name the country's name.
     * @return the matching {@code Country} object, or {@code null} if not found
     */
    public Country getCountry(final String name)
    {
        return this.countries.get(name);
    }

    /**
     * Returns a random {@code Country} object from the entire loaded data.
     * This is achieved by generating a random index, skipping elements in the map's values stream,
     * and retrieving the element at that index.
     *
     * @return a random {@code Country}
     * @throws NoSuchElementException if the country collection is unexpectedly empty
     */
    public Country getRandomCountry()
    {
        final Collection<Country> values;
        values = this.countries.values();

        final int index;
        index = this.random.nextInt(values.size());

        // Converts the collection of country objects into a stream, skips forward
        // by the randomly chosen idx, and returns the element at that position.
        //
        // values.stream() - creates a stream of all country objects.
        // .skip(index) - moves past the first idx elements.
        // .findFirst() - retrieves the next element in the stream.
        // .orElseThrow() - throws an exception.
        return values.stream().skip(index).findFirst().orElseThrow();
    }
}