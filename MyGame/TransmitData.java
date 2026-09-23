package ca.bcit.comp2522.project.myGame;

/**
 * Defines the contract for any component capable of transmitting data.
 * This is a Functional Interface because it contains exactly one
 * abstract method.
 *
 * @author Ibrahim Abudalah
 * @version 1.0
 */
@FunctionalInterface
public interface TransmitData
{

    /**
     * Executes the data transmission logic.
     * This method is called through a {@code Thread} in the {@code MissionController}
     * and must be implemented by concrete sensor classes.
     */
    void transmit();
}