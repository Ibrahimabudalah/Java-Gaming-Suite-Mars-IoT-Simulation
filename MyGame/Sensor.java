package ca.bcit.comp2522.project.myGame;

/**
 * Represents an abstract class for a Mars ESP network sensor.
 * This class provides common data (battery, name, policy) and fundamental behaviors
 * (constructor, naming, power management) inherited by all specialized sensors.
 * It implements {@code TransmitData} and enforces the {@code getPowerDrain} method
 * through an abstract method.
 *
 * @author Ibrahim Abudalah
 * @version 1.0
 */
public abstract class Sensor implements TransmitData
{
    protected static final double      MAX_BATTERY          = 100.0;
    protected static final double      MIN_BATTERY          = 0.0;
    protected static final int         INITIAL_PACKETS_SENT = 0;
    protected final        String      name;
    protected              double      battery;
    protected              int         packetsSent;
    protected              PowerPolicy policy;

    /**
     * Constructs a {@code Sensor} object, initializing it to maximum battery and normal power policy.
     *
     * @param name The unique name of the sensor.
     */
    public Sensor(final String name)
    {
        this.name        = name;
        this.battery     = MAX_BATTERY;
        this.packetsSent = INITIAL_PACKETS_SENT;
        this.policy      = PowerPolicy.NORMAL;
    }

    /**
     * Retrieves the name of the sensor.
     *
     * @return The sensor's name as a {@code String}.
     */
    public String getName()
    {
        return name;
    }

    /**
     * Retrieves the power mode of the sensor.
     *
     * @return The sensor's power mode as a {@code String}.
     */
    public PowerPolicy getPolicy()
    {
        return policy;
    }

    /**
     * Retrieves the sensor current battery percentage.
     *
     * @return The sensor's battery percentage as a {@code double}.
     */
    public double getBattery()
    {
        return battery;
    }

    /**
     * Retrieves the packet sent.
     *
     * @return The packet sent as a {@code int}.
     */
    public int getPacketsSent()
    {
        return packetsSent;
    }

    /**
     * Checks if the sensor is currently operational (battery is above the minimum).
     *
     * @return {@code true} if the battery is greater than {@code MIN_BATTERY}, {@code false} otherwise.
     */
    public boolean isOnline()
    {
        return policy != PowerPolicy.OFFLINE && battery > MIN_BATTERY;
    }


    /**
     * Recharges the sensor's battery by a given percentage, ensuring it does not exceed the maximum capacity.
     *
     * @param percent The amount of charge to add to the battery (as a percentage).
     */
    public void recharge(final double percent)
    {
        battery = Math.min(MAX_BATTERY, battery + percent);
    }

    /**
     * Defines the base power drain consumption rate for a sensor type.
     * This is an abstract method, forcing all concrete subclasses to implement
     * their own specific power consumption rate.
     *
     * @return The power drain amount as a {@code double}.
     */
    public abstract double getPowerDrain();

    /**
     * Updates the sensor's current operational power policy.
     *
     * @param newPolicy The new {@code PowerPolicy} to apply to the sensor.
     */
    public void updatePolicy(final PowerPolicy newPolicy)
    {
        this.policy = newPolicy;
    }
}