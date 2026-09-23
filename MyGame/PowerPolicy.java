package ca.bcit.comp2522.project.myGame;

/**
 * Represents different operational states (power policies) for a sensor.
 * This enum is used to define discrete states and associate a constant
 * power multiplier with each state.
 *
 * @author Ibrahim Abudalah
 * @version 1.0
 */
public enum PowerPolicy
{
    /**
     * Normal operation state with normal power consumption.
     */
    NORMAL(1.0),
    /**
     * Reduced power consumption state.
     */
    LOW_POWER(0.5),
    /**
     * Sensor is offline and should consume no power.
     */
    OFFLINE(0.0);

    /**
     * The multiplier used to calculate effective power drain in the {@code transmit} method.
     */
    public final double multiplier;

    /**
     * Initializes a {@code PowerPolicy} with a power consumption multiplier.
     *
     * @param multiplier The factor by which the base power drain is multiplied.
     */
    PowerPolicy(final double multiplier)
    {
        this.multiplier = multiplier;
    }
}