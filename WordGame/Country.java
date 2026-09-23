package ca.bcit.comp2522.project.wordGame;

import java.util.Objects;

/**
 * Represents a single, immutable country data entry used by the Word Game.
 * Each instance stores a country's name, its capital name, and
 * a fixed number of facts. Being immutable, all instance fields are {@code final}
 * and set only upon construction.
 *
 * @author Ibrahim Abudalah
 * @version 1.0
 */
public final class Country
{
    private static final int      NUM_FACTS = 3;
    private final        String   name;
    private final        String   capitalCityName;
    private final        String[] facts;

    /**
     * Constructs a new {@code Country} object.
     * This constructor performs validation on all input parameters to ensure
     * non-null/non-blank values and verifies that the facts array is of the
     * required fixed length ({@value #NUM_FACTS}).
     *
     * @param name            the country's name, must be non-null and non-blank.
     * @param capitalCityName the capital city name, must be non-null and non-blank.
     * @param facts           an array of exactly three non-blank fact strings.
     * @throws IllegalArgumentException if any parameter is invalid.
     */
    public Country(final String name,
                   final String capitalCityName,
                   final String[] facts)
    {

        validateName(name);
        validateCapital(capitalCityName);
        validateFacts(facts);

        this.name            = name;
        this.capitalCityName = capitalCityName;
        this.facts           = facts.clone();
    }

    /**
     * Validates the country name.
     * Checks that the string reference is not null and that its content is not blank.
     *
     * @param name the name string to validate.
     * @throws IllegalArgumentException if the country name is null or blank.
     */
    private static void validateName(final String name)
    {
        if (name == null || name.isBlank())
        {
            throw new IllegalArgumentException("Country name cannot be null or blank.");
        }
    }

    /**
     * Validates the capital city name.
     * Checks that the string reference is not null and that its content is not blank.
     *
     * @param capital the capital name string to validate.
     * @throws IllegalArgumentException if the capital city name is null or blank.
     */
    private static void validateCapital(final String capital)
    {
        if (capital == null || capital.isBlank())
        {
            throw new IllegalArgumentException("Capital city name cannot be null or blank.");
        }
    }

    /**
     * Validates the facts array structure and contents.
     * Ensures the array reference is not null, the length exactly matches
     * {@value NUM_FACTS}, and every fact string within the array is non-null and non-blank.
     *
     * @param facts the array of strings to validate.
     * @throws IllegalArgumentException if the facts array is null, does not contain exactly {@value NUM_FACTS}
     *                                  elements, or contains any null or blank strings
     */
    private static void validateFacts(final String[] facts)
    {
        if (facts == null)
        {
            throw new IllegalArgumentException("Facts array cannot be null.");
        }
        if (facts.length != NUM_FACTS)
        {
            throw new IllegalArgumentException("Facts array must contain exactly "
                                               + NUM_FACTS + " facts.");
        }
        for (final String fact : facts)
        {
            if (fact == null || fact.isBlank())
            {
                throw new IllegalArgumentException("Facts cannot contain null or blank values.");
            }
        }
    }

    /**
     * Returns the country's name.
     *
     * @return the immutable name of the country
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * Returns the capital's name.
     *
     * @return the immutable capital name of the country
     */
    public String getCapitalCityName()
    {
        return this.capitalCityName;
    }

    /**
     * Returns the facts array.
     * This cloning process ensures that external code cannot modify the contents
     * of the {@code Country} object's array, to preserve its immutability.
     *
     * @return a new {@code String} array containing the facts.
     */
    public String[] getFacts()
    {
        return this.facts.clone();
    }

    /**
     * Overrides the {@code Object} class's {@code equals} method.
     * Two {@code Country} objects are considered equal if and only if they have the
     * identical {@code name} and identical {@code capitalCityName}.
     *
     * @param obj the other object to compare for equality
     * @return {@code true} if the objects are equal based on name and capital, {@code false} otherwise
     */
    @Override
    public boolean equals(final Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (!(obj instanceof Country))
        {
            return false;
        }

        final Country other = (Country) obj;

        return this.name.equals(other.name)
               && this.capitalCityName.equals(other.capitalCityName);
    }

    /**
     * Overrides the {@code Object} class's {@code hashCode} method.
     * Calculates the hash code based on the country's {@code name} and
     * {@code capitalCityName}, fulfilling the contract that equal objects must
     * return equal hash codes.
     *
     * @return the hash code value for this {@code Country} object
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(this.name, this.capitalCityName);
    }

    /**
     * Overrides the {@code Object} class's {@code toString} method.
     * returns a representation of the country, "CountryName (CapitalCityName)".
     *
     * @return a formatted string version of the country
     */
    @Override
    public String toString()
    {
        return this.name + " (" + this.capitalCityName + ")";
    }
}