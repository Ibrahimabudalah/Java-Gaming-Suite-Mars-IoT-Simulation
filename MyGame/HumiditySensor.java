package ca.bcit.comp2522.project.myGame;

/**
 * Represents a specialized Humidity Sensor.
 * This concrete class extends {@code Sensor} and implements the abstract {@code transmit()}
 * and {@code getPowerDrain()} methods with humidity specific logic and constants.
 *
 * @author Ibrahim Abudalah
 * @version 1.0
 */
public class HumiditySensor extends Sensor
{
    private static final double HUMIDITY_DRAIN_PER_HOUR = 5.0;
    private static final int    MAX_HUMIDITY_PERCENT    = 100;

    /**
     * Constructs a {@code HumiditySensor} with the given name.
     *
     * @param name The name of the humidity sensor.
     */
    public HumiditySensor(final String name)
    {
        super(name);
    }

    /**
     * Implements the {@code TransmitData.transmit()} contract.
     * Sends a humidity reading, increments the packet count, and reduces the battery
     * by the calculated power drain (base drain * policy multiplier).
     */
    @Override
    public void transmit()
    {
        if (!isOnline())
        {
            return;
        }

        // Logic to send data and update mutable state.
        packetsSent++;
        battery -= getPowerDrain() * policy.multiplier;

        System.out.printf("Thread: %s sent humidity packet [%.1f%%]\n",
                          name, Math.random() * MAX_HUMIDITY_PERCENT);
    }

    /**
     * Implements the {@code Sensor.getPowerDrain()} contract.
     *
     * @return The constant power drain rate for this type of sensor.
     */
    @Override
    public double getPowerDrain()
    {
        return HUMIDITY_DRAIN_PER_HOUR;
    }
}