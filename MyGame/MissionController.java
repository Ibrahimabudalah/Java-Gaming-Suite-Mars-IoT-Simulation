package ca.bcit.comp2522.project.myGame;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.InputMismatchException;

/**
 * Manages the mission simulation logic, user interaction, and overall network state.
 * This class orchestrates the game loop, user choices, random events, and results logging.
 *
 * @author Ibrahim Abudalah
 * @version 1.0
 */
public class MissionController
{
    private static final int          TOTAL_MISSION_HOURS    = 24;
    private static final int          PREVIOUS_HOUR_OFFSET   = 1;
    private static final double       SOLAR_RECHARGE_AMOUNT  = 15.0;
    private static final double       SOLAR_BOOST_MULTIPLIER = 2.0;
    private static final int          ACTION_TRANSMIT        = 1;
    private static final int          ACTION_SLEEP           = 2;
    private static final int          ACTION_RECHARGE        = 3;
    private static final int          RANDOM_ROLL_RANGE      = 100;
    private static final int          DUST_STORM_CHANCE      = 15;
    private static final int          SOLAR_BOOST_CHANCE     = 30;
    private static final int          RADIATION_SPIKE_CHANCE = 35;
    private static final String       SENSOR_TEMP_NAME       = "TempSensor";
    private static final String       SENSOR_HUMIDITY_NAME   = "HumiditySensor";
    private static final String       SENSOR_CAM_NAME        = "CamSensor";
    private static final String       LOG_FILE_NAME          = "mars_network.txt";
    private final        List<Sensor> sensors;
    private final        Random       random;

    /**
     * Constructs a {@code MissionController}, initializing the sensor network
     * with one of each specialized sensor type.
     */
    public MissionController()
    {
        this.sensors = new ArrayList<>();
        this.random  = new Random();

        sensors.add(new TempSensor(SENSOR_TEMP_NAME));
        sensors.add(new HumiditySensor(SENSOR_HUMIDITY_NAME));
        sensors.add(new CamSensor(SENSOR_CAM_NAME));
    }

    /**
     * Starts the main mission simulation loop (the core game logic).
     * The loop runs for the predefined number of hours or until all sensors go offline.
     */
    public void startMission()
    {
        System.out.println("ESP Mission Control");

        final Scanner scanner;
        scanner = new Scanner(System.in);

        int hour;
        for (hour = 1; hour <= TOTAL_MISSION_HOURS; hour++)
        {
            System.out.printf("\nHour %d of %d\n", hour, TOTAL_MISSION_HOURS);
            showStatus();

            System.out.println("[" + ACTION_TRANSMIT + "] Transmit data  [" +
                               ACTION_SLEEP + "] Sleep sensors  [" +
                               ACTION_RECHARGE + "] Recharge");
            System.out.print("Choose action: ");

            final int choice;

            try
            {
                choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice)
                {
                    case ACTION_TRANSMIT -> transmitAll();
                    case ACTION_SLEEP -> sleepAll();
                    case ACTION_RECHARGE -> rechargeAll();
                    default -> System.out.println("Invalid option.");
                }
            }
            catch (final InputMismatchException e)
            {
                System.out.println("Invalid input! Please enter a number (" +
                                   ACTION_TRANSMIT +
                                   ", " +
                                   ACTION_SLEEP +
                                   ", or " +
                                   ACTION_RECHARGE +
                                   ").");
                scanner.nextLine();
            }

            randomEvent();

            // Check mission failure
            if (sensors.stream().noneMatch(Sensor::isOnline))
            {
                System.out.println("All sensors offline! Mission failed.");
                logResults(hour - PREVIOUS_HOUR_OFFSET);
                return;
            }
        }

        // If loop completes successfully
        logResults(TOTAL_MISSION_HOURS);
        System.out.println("Mission completed successfully!\n");
    }

    /**
     * Initiates concurrent data transmission for all sensors.
     * Each sensor's {@code transmit()} is run.
     */
    public void transmitAll()
    {
        for (final Sensor s : sensors)
        {
            s.transmit();
        }
    }

    /**
     * Updates all sensors power policy to {@code LOW_POWER}.
     */
    public void sleepAll()
    {
        sensors.forEach(s -> s.updatePolicy(PowerPolicy.LOW_POWER));
        System.out.println("All sensors set to low power mode.");
    }

    /**
     * Applies the constant solar recharge amount to all sensors.
     */
    public void rechargeAll()
    {
        sensors.forEach(s -> s.recharge(SOLAR_RECHARGE_AMOUNT));
        System.out.println("Solar recharge applied to all sensors.");
    }

    /**
     * Simulates a random external event affecting the mission, such as
     * solar boost or radiation spike.
     */
    private void randomEvent()
    {
        final int roll;
        roll = random.nextInt(RANDOM_ROLL_RANGE);

        if (roll < DUST_STORM_CHANCE)
        {
            System.out.println("Dust storm — no recharge next hour.");
        }
        else if (roll < SOLAR_BOOST_CHANCE)
        {
            System.out.println("Solar boost — recharge efficiency doubled.");
            sensors.forEach(s -> s.recharge(SOLAR_RECHARGE_AMOUNT * SOLAR_BOOST_MULTIPLIER));
        }
        else if (roll < RADIATION_SPIKE_CHANCE)
        {

            // Select a random victim sensor.
            final Sensor victim;
            victim = sensors.get(random.nextInt(sensors.size()));

            victim.updatePolicy(PowerPolicy.OFFLINE);
            System.out.printf("Radiation spike: %s is offline!\n", victim.getName());
        }
    }

    /**
     * Displays the current status (battery level and mode) for all sensors.
     */
    private void showStatus()
    {
        sensors.forEach(s -> System.out.printf("%-15s Battery: %5.1f%% Mode: %s\n",
                                               s.getName(),
                                               s.battery,
                                               s.policy));
    }

    /**
     * Logs the final mission results to the data file.
     *
     * @param hoursOperated The total number of hours the mission ran successfully.
     */
    private void logResults(final int hoursOperated)
    {
        try (PrintWriter out = new PrintWriter(new FileOutputStream(LOG_FILE_NAME, true)))
        {
            final String timestamp;
            timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            final long online;
            online = sensors.stream()
                            .filter(Sensor::isOnline)
                            .count();

            final int totalPackets;
            totalPackets = sensors.stream()
                                  .mapToInt(s -> s.packetsSent)
                                  .sum();

            out.println("Mission Summary — " + timestamp);
            out.println("Hours Operated: " + hoursOperated);
            out.println("Sensors online: " + online + "/" + sensors.size());
            out.println("Packets sent: " + totalPackets);
            out.println("----------------------------");
        }
        catch (IOException e)
        {
            System.err.println("Error saving mission log.");
        }
    }

    /**
     * Gets all the sensors.
     *
     * @return A list of all the sensors.
     */
    public List<Sensor> getSensors()
    {
        return List.copyOf(sensors);
    }

}