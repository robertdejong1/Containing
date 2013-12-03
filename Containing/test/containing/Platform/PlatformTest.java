package containing.Platform;

import containing.Container;
import containing.ControlleralgorithmsTest;
import containing.Dimension2f;
import containing.Exceptions.NoFreeAgvException;
import containing.MessageLog;
import containing.Port;
import containing.Settings;
import containing.Vector3f;
import containing.Vehicle.AGV;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class PlatformTest {
    
    Platform instance = null;
    
    public PlatformTest() {
        Settings.messageLog = new MessageLog();
        Settings.port = new Port();
        instance = new BargePlatform(new Vector3f(0,0,0));
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

        instance.requestNextContainer();

        fail("The test case is a prototype.");
    }

    /**
     * Test of requestNextJob method, of class Platform.
     */
    @Test
    public void testRequestNextJob() {
        System.out.println("requestNextJob");
        
        for(int i = 0; i < 99999; i++)
        {
            instance.requestNextJob();
        }
    }

    /**
     * Test of addAgvToQueue method, of class Platform.
     */
    @Test
    public void testAddAgvToQueue() {
        System.out.println("addAgvToQueue");
        for(int i = 0; i < 99999; i++)
        {
            AGV agv = new AGV(instance, new Vector3f(0,0,0));
            instance.addAgvToQueue(agv);
            if(instance.agvQueue.size() > 8)
            {
                fail("There is no more road for AGV's, should not add more than 8");
            }
        }
        assertEquals(8, instance.agvQueue.size());
    }

    /**
     * Test of getFreeAgv method, of class Platform.
     */
    @Test
    public void testGetFreeAgv() {
        System.out.println("getFreeAgv");
        
        if(Settings.port.getStoragePlatform().agvs.isEmpty())
            fail("AGV's arent created");

        int expResult = Settings.port.getStoragePlatform().agvs.get(99).getID();
        int result = 0;
        try
        {
            result = instance.requestFreeAgv().getID();
        } catch(NoFreeAgvException e) { 
            System.out.println(e.getMessage());
            fail("kon geen vrije AGV vinden");
        }
        System.out.println("exp: " + expResult);
        System.out.println("res: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of createAgvSpots method, of class Platform.
     */
    @Test
    public void testCreateAgvSpots() {
        System.out.println("createAgvSpots");
        Vector3f baseposition = new Vector3f(0,0,0);

        instance.createAgvSpots(baseposition);

        fail("The test case is a prototype.");
    }

    /**
     * Test of getAgvSpotAmount method, of class Platform.
     */
    @Test
    public void testGetAgvSpotAmount() {
        System.out.println("getAgvSpotAmount");
        int expResult = 0;
        int result = instance.getAgvSpotAmount();
        assertEquals(expResult, result);

        fail("The test case is a prototype.");
    }

    /**
     * Test of hasFreeParkingSpot method, of class Platform.
     */
    @Test
    public void testHasFreeParkingSpot() {
        System.out.println("hasFreeParkingSpot");

        boolean expResult = false;
        boolean result = instance.hasFreeParkingSpot();
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
