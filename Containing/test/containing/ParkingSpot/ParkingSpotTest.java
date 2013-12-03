/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package containing.ParkingSpot;

import static com.oracle.nio.BufferSecrets.instance;
import static containing.Container.TransportType.Train;
import containing.Exceptions.InvalidVehicleException;
import containing.Vector3f;
import containing.Vehicle.Barge;
import containing.Vehicle.Train;
import containing.Vehicle.Vehicle;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Robert
 */
public class ParkingSpotTest {

    public ParkingSpotTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of isEmpty method, of class ParkingSpot.
     */
    @Test
    public void testIsEmpty() {
        System.out.println("isEmpty");
        ParkingSpot spot = new AgvSpot(new Vector3f(0, 0, 0));
        boolean result = spot.isEmpty();
        assertEquals(true, result);
        // TODO review the generated test code and remove the default call to fail.
    }

    /**
     * Test of ParkVehicle method, of class ParkingSpot.
     */
    @Test
    public void testParkVehicle() throws Exception {
        System.out.println("ParkVehicle");

        ParkingSpot spot = new BargeSpot(new Vector3f(0, 0, 0));
        spot.ParkVehicle(new Barge(null, 1f, null, null));
        boolean result = spot.isEmpty();
        // TODO review the generated test code and remove the default call to fail.
        assertEquals(false, result);
    }

    /**
     * Test of getParkedVehicle method, of class ParkingSpot.
     */
    @Test
    public void testGetParkedVehicle() {
        System.out.println("getParkedVehicle");
        Vehicle train = new Train(null, 1f, null, null);

        ParkingSpot spot = new TrainSpot(new Vector3f(0, 0, 0));
        try {
            spot.ParkVehicle(train);
        } catch (InvalidVehicleException e) {
            fail("Exception occured");
        }
        assertEquals(train, spot.getParkedVehicle());
        // TODO review the generated test code and remove the default call to fail.
    }

    public class ParkingSpotImpl extends ParkingSpot {

        public ParkingSpotImpl() {
            super(null);
        }

        public void ParkVehicle(Vehicle VehicleToPark) throws InvalidVehicleException {
        }
    }

}
