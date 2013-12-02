package containing.Platform;

import containing.Container;
import containing.Dimension2f;
import containing.Vector3f;
import containing.Vehicle.AGV;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class PlatformTest {
    
    public PlatformTest() {
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
     * Test of requestNextContainer method, of class Platform.
     */
    @Test
    public void testRequestNextContainer() {
        System.out.println("requestNextContainer");
        Platform instance = null;
        instance.requestNextContainer();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of requestNextJob method, of class Platform.
     */
    @Test
    public void testRequestNextJob() {
        System.out.println("requestNextJob");
        Platform instance = null;
        instance.requestNextJob();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of addAgvToQueue method, of class Platform.
     */
    @Test
    public void testAddAgvToQueue() {
        System.out.println("addAgvToQueue");
        AGV agv = null;
        Platform instance = null;
        instance.addAgvToQueue(agv);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getFreeAgv method, of class Platform.
     */
    @Test
    public void testGetFreeAgv() {
        System.out.println("getFreeAgv");
        Platform instance = null;
        AGV expResult = null;
        AGV result = instance.getFreeAgv();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of createAgvSpots method, of class Platform.
     */
    @Test
    public void testCreateAgvSpots() {
        System.out.println("createAgvSpots");
        Vector3f baseposition = null;
        Platform instance = null;
        instance.createAgvSpots(baseposition);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getAgvSpotAmount method, of class Platform.
     */
    @Test
    public void testGetAgvSpotAmount() {
        System.out.println("getAgvSpotAmount");
        Platform instance = null;
        int expResult = 0;
        int result = instance.getAgvSpotAmount();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of hasFreeParkingSpot method, of class Platform.
     */
    @Test
    public void testHasFreeParkingSpot() {
        System.out.println("hasFreeParkingSpot");
        Platform instance = null;
        boolean expResult = false;
        boolean result = instance.hasFreeParkingSpot();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getPosition method, of class Platform.
     */
    @Test
    public void testGetPosition() {
        System.out.println("getPosition");
        Platform instance = null;
        Vector3f expResult = null;
        Vector3f result = instance.getPosition();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getDimension method, of class Platform.
     */
    @Test
    public void testGetDimension() {
        System.out.println("getDimension");
        Platform instance = null;
        Dimension2f expResult = null;
        Dimension2f result = instance.getDimension();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setDimension method, of class Platform.
     */
    @Test
    public void testSetDimension() {
        System.out.println("setDimension");
        Dimension2f dimension = null;
        Platform instance = null;
        instance.setDimension(dimension);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getAxis method, of class Platform.
     */
    @Test
    public void testGetAxis() {
        System.out.println("getAxis");
        Platform instance = null;
        Platform.DynamicAxis expResult = null;
        Platform.DynamicAxis result = instance.getAxis();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setAxis method, of class Platform.
     */
    @Test
    public void testSetAxis() {
        System.out.println("setAxis");
        Platform.DynamicAxis axis = null;
        Platform instance = null;
        instance.setAxis(axis);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getEntrypoint method, of class Platform.
     */
    @Test
    public void testGetEntrypoint() {
        System.out.println("getEntrypoint");
        Platform instance = null;
        Vector3f expResult = null;
        Vector3f result = instance.getEntrypoint();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setEntrypoint method, of class Platform.
     */
    @Test
    public void testSetEntrypoint() {
        System.out.println("setEntrypoint");
        Vector3f entrypoint = null;
        Platform instance = null;
        instance.setEntrypoint(entrypoint);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getExitpoint method, of class Platform.
     */
    @Test
    public void testGetExitpoint() {
        System.out.println("getExitpoint");
        Platform instance = null;
        Vector3f expResult = null;
        Vector3f result = instance.getExitpoint();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setExitpoint method, of class Platform.
     */
    @Test
    public void testSetExitpoint() {
        System.out.println("setExitpoint");
        Vector3f exitpoint = null;
        Platform instance = null;
        instance.setExitpoint(exitpoint);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getTransportType method, of class Platform.
     */
    @Test
    public void testGetTransportType() {
        System.out.println("getTransportType");
        Platform instance = null;
        Container.TransportType expResult = null;
        Container.TransportType result = instance.getTransportType();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setTransportType method, of class Platform.
     */
    @Test
    public void testSetTransportType() {
        System.out.println("setTransportType");
        Container.TransportType transportType = null;
        Platform instance = null;
        instance.setTransportType(transportType);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setMaxAgvQueue method, of class Platform.
     */
    @Test
    public void testSetMaxAgvQueue() {
        System.out.println("setMaxAgvQueue");
        int max = 0;
        Platform instance = null;
        instance.setMaxAgvQueue(max);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getId method, of class Platform.
     */
    @Test
    public void testGetId() {
        System.out.println("getId");
        Platform instance = null;
        int expResult = 0;
        int result = instance.getId();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of createCranes method, of class Platform.
     */
    @Test
    public void testCreateCranes() {
        System.out.println("createCranes");
        Platform instance = null;
        instance.createCranes();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of createExtVehicleSpots method, of class Platform.
     */
    @Test
    public void testCreateExtVehicleSpots() {
        System.out.println("createExtVehicleSpots");
        Platform instance = null;
        instance.createExtVehicleSpots();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of update method, of class Platform.
     */
    @Test
    public void testUpdate() {
        System.out.println("update");
        Platform instance = null;
        instance.update();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of toString method, of class Platform.
     */
    @Test
    public void testToString() {
        System.out.println("toString");
        Platform instance = null;
        String expResult = "";
        String result = instance.toString();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of log method, of class Platform.
     */
    @Test
    public void testLog() {
        System.out.println("log");
        String msg = "";
        Platform instance = null;
        instance.log(msg);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    public class PlatformImpl extends Platform {

        public PlatformImpl() {
            super(null);
        }

        public void createCranes() {
        }

        public void createExtVehicleSpots() {
        }

        public void update() {
        }
    }
    
}
