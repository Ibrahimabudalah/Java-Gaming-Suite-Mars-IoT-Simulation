package ca.bcit.comp2522.project.myGame;

/**
 * Represents a specialized Temperature Sensor.
 * This concrete class extends {@code Sensor} and implements the abstract {@code transmit()}
 * and {@code getPowerDrain()} methods with temperature specific logic and constants.
 *
 * @author Ibrahim Abudalah
 * @version 1.0
 */
public class TempSensor extends Sensor
{
    private static final double TEMP_DRAIN_PER_HOUR = 7.5;
    private static final int    MAX_TEMP_CELSIUS    = 40;

    /**
     * Constructs a {@code TempSensor} with the given name.
     *
     * @param name The name of the temperature sensor.
     */
    public TempSensor(final String name)
    {
        super(name);
    }

    /**
     * Implements the {@code TransmitData.transmit()} contract.
     * Sends a temperature reading, increments the packet count, and reduces the battery
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

        System.out.printf("Thread: %s sent temperature packet [%.1f°C]\n",
                          name, Math.random() * MAX_TEMP_CELSIUS);
    }

    /**
     * Implements the {@code Sensor.getPowerDrain()} contract.
     *
     * @return The constant power drain rate for this type of sensor.
     */
    @Override
    public double getPowerDrain()
    {
        return TEMP_DRAIN_PER_HOUR;
    }
}