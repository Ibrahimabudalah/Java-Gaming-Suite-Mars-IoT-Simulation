package ca.bcit.comp2522.project.myGame;

/**
 * Represents a specialized Camera Sensor.
 * This concrete class extends {@code Sensor} and implements the abstract {@code transmit()}
 * and {@code getPowerDrain()} methods with camera specific logic and constants.
 *
 * @author Ibrahim Abudalah
 * @version 1.0
 */
public class CamSensor extends Sensor
{
    private static final double CAMERA_DRAIN_PER_HOUR = 10.0;

    /**
     * Constructs a {@code CamSensor} with the given name.
     *
     * @param name The name of the camera sensor.
     */
    public CamSensor(final String name)
    {
        super(name);
    }

    /**
     * Implements the {@code TransmitData.transmit()} contract.
     * Sends a camera frame status, increments the packet count, and reduces the battery
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

        System.out.printf("Thread: %s sent camera frame packet [OK]\n", name);
    }

    /**
     * Implements the {@code Sensor.getPowerDrain()} contract.
     *
     * @return The constant power drain rate for this type of sensor.
     */
    @Override
    public double getPowerDrain()
    {
        return CAMERA_DRAIN_PER_HOUR;
    }
}