package ca.bcit.comp2522.project.myGame;

/**
 * The concrete representation of the MyGame component in the COMP term project.
 * <p>
 * This class acts as the wrapper and entry point for the ESP Mission Control game.
 * A text-based IoT management game set on Mars. The player assumes the role of a systems engineer
 * tasked with maintaining a network of ESP-based IoT sensors that monitor environmental conditions
 * such as temperature, humidity, and camera feeds over a 24-hour period.
 * <p>
 * The game challenges players to balance limited solar power, manage sensor health, and respond to
 * unpredictable planetary events like dust storms, solar boosts, and radiation spikes. Each sensor
 * consumes battery power when transmitting data and can be switched into low-power or offline modes
 * to conserve energy. Strategic use of recharge cycles and power policies determines mission success.
 * <p>
 * Gameplay revolves around real time decision-making:
 * <ul>
 *      <li>Transmitting data drains battery but increases mission success points.</li>
 *      <li>Entering low-power mode slows battery drain but delays progress.</li>
 *      <li>Recharging replenishes energy using limited solar input, affected by random weather events.</li>
 * </ul>
 * <p>
 * The simulation ends after 24 hours (win condition) or if all sensors run out of power or go offline
 * (loss condition). Results, including operational hours, packets transmitted, and active sensor count,
 * are logged to a log file. The game demonstrates the use of OOP principles, interfaces,
 * inheritance, polymorphism, and file I/O.
 * <p>
 * The {@code MyGame} class itself delegates all gameplay logic to {@link MissionController}, serving
 * purely as a high-level interface invoked by the project’s {@code Main} class when the player selects
 * the ESP Mission Control from the main menu.
 *
 * @author Ibrahim Abudalah
 * @version 1.0
 */
public final class MyGame
{
    private final MissionController controller;

    /**
     * Constructs a {@code MyGame} instance, initializing the {@code MissionController}.
     */
    public MyGame()
    {
        this.controller = new MissionController();
    }

    /**
     * Starts the ESP Mission Control component.
     * This method is called from the main menu, delegating the actual gameplay
     * to the internal controller instance.
     */
    public void play()
    {
        controller.startMission();
    }
}