package containing;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Stack;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class JobTest 
{
    private Job j;
    
    public JobTest() 
    {
        j = new Job(new Date(1101855600000L), 0.10f, Container.TransportType.Truck, "Yolo Swaggings CO");
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

    @Test
    public void testAddContainer() {
        System.out.println("addContainer");
        Container container = null;
        int ContainerCount = j.getContainers().size();
        j.addContainer(container);
        assertEquals(ContainerCount + 1, j.getContainers().size());
    }

    /**
     * Test of getDate method, of class Job.
     */
    @Test
    public void testGetDate() {
        System.out.println("getDate");
        
        long expResult = 1101855600000L;
        long result = j.getDate().getTime();
        assertEquals(expResult, result);
    }

    /**
     * Test of getDepartureTime method, of class Job.
     */
    @Test
    public void testGetDepartureTime() {
        System.out.println("getDepartureTime");
        
        float expResult = 0.10F;
        float result = j.getDepartureTime();
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of getVehicleType method, of class Job.
     */
    @Test
    public void testGetVehicleType() {
        System.out.println("getVehicleType");
        Container.TransportType expResult = Container.TransportType.Truck;
        Container.TransportType result = j.getVehicleType();
        assertEquals(expResult, result);
    }
    
    /**
     * Test of sortOutGoingJobs method, of class Job.
     */
    @Test
    public void testSortOutGoingJobs() {
        System.out.println("sortOutGoingJobs");
        List<Job> Jobs = new ArrayList<>();
        Jobs.add(new Job(new Date(1101855600000L), 0.30f, Container.TransportType.Truck, "Yolo Swaggings CO"));
        Jobs.add(new Job(new Date(1101855600000L), 0.10f, Container.TransportType.Truck, "Yolo Swaggings CO"));
        Jobs.add(new Job(new Date(1101855600000L), 0.40f, Container.TransportType.Truck, "Yolo Swaggings CO"));
        
        Stack result = Job.sortOutGoingJobs(Jobs);
        Job job = (Job)result.pop();
        if (job.getDepartureTime() != 0.10f)
        {
            System.out.println(job.getDepartureTime());
            fail("not 0.10");
            
        }
    }
}