package tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ca.bcit.comp2522.project.myGame.TempSensor;
import ca.bcit.comp2522.project.myGame.HumiditySensor;
import ca.bcit.comp2522.project.myGame.CamSensor;
import ca.bcit.comp2522.project.myGame.PowerPolicy;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests all sensor subclasses:
 * - Sensor
 * - TempSensor
 * - HumiditySensor
 * - CamSensor
 * <p>
 * These tests validate deterministic behaviors:
 * - isOnline()
 * - battery changes
 * - packetsSent changes
 * - power policy changes
 */
class SensorTest
{
    private TempSensor     tempSensor;
    private HumiditySensor humiditySensor;
    private CamSensor      camSensor;

    @BeforeEach
    void setUp()
    {
        tempSensor     = new TempSensor("T1");
        humiditySensor = new HumiditySensor("H1");
        camSensor      = new CamSensor("C1");
    }

    @Test
    void testSensorsStartOnline()
    {
        assertTrue(tempSensor.isOnline());
        assertTrue(humiditySensor.isOnline());
        assertTrue(camSensor.isOnline());
    }

    @Test
    void testRechargeIncreasesBattery()
    {
        tempSensor.recharge(-20);
        final double before = tempSensor.getBattery();

        tempSensor.recharge(10);

        final double after = tempSensor.getBattery();

        assertEquals(before + 10, after);

    }

    @Test
    void testUpdatePolicyChangesPowerState()
    {
        humiditySensor.updatePolicy(PowerPolicy.LOW_POWER);
        assertEquals(PowerPolicy.LOW_POWER, humiditySensor.getPolicy());
    }

    @Test
    void testSensorGoesOfflineWithOfflinePolicy()
    {
        camSensor.updatePolicy(PowerPolicy.OFFLINE);

        assertFalse(camSensor.isOnline());
    }

    @Test
    void testTempSensorTransmitIncrementsPackets()
    {
        final int before = tempSensor.getPacketsSent();

        tempSensor.transmit();

        final int after = tempSensor.getPacketsSent();

        assertEquals(before + 1, after);
    }

    @Test
    void testHumiditySensorTransmitIncrementsPackets()
    {
        final int before = humiditySensor.getPacketsSent();

        humiditySensor.transmit();

        final int after = humiditySensor.getPacketsSent();

        assertEquals(before + 1, after);
    }

    @Test
    void testCamSensorTransmitIncrementsPackets()
    {
        final int before = camSensor.getPacketsSent();

        camSensor.transmit();

        final int after = camSensor.getPacketsSent();

        assertEquals(before + 1, after);
    }
}
