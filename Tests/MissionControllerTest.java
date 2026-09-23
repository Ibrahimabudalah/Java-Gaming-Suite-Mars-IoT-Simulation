package tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ca.bcit.comp2522.project.myGame.MissionController;
import ca.bcit.comp2522.project.myGame.PowerPolicy;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests deterministic MissionController behavior:
 * - sleepAll()
 * - rechargeAll()
 * - transmitAll()
 */
class MissionControllerTest
{
    private MissionController controller;

    @BeforeEach
    void setUp()
    {
        controller = new MissionController();
    }

    @Test
    void testControllerInitializesThreeSensors()
    {
        assertEquals(3, controller.getSensors().size());
    }

    @Test
    void testSleepAllSetsAllSensorsToLowPower()
    {
        controller.sleepAll();
        controller.getSensors().forEach(
            s -> assertEquals(PowerPolicy.LOW_POWER, s.getPolicy())
                                       );
    }

    @Test
    void testRechargeAllIncreasesBattery()
    {
        final double before = controller.getSensors().get(0).getBattery();
        controller.rechargeAll();
        final double after = controller.getSensors().get(0).getBattery();

        assertTrue(after >= before);
    }

    @Test
    void testTransmitAllIncrementsPacketsByOnePerSensor()
    {
        final int before = controller.getSensors().stream()
                                     .mapToInt(s -> s.getPacketsSent())
                                     .sum();

        controller.transmitAll(); // Sequential

        final int after = controller.getSensors().stream()
                                    .mapToInt(s -> s.getPacketsSent())
                                    .sum();

        assertEquals(before + controller.getSensors().size(), after);
    }
}
