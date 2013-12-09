/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package containing;

import containing.Exceptions.NoJobException;
import containing.Platform.BargePlatform;
import containing.Platform.Platform;
import containing.Platform.TruckPlatform;
import containing.Vehicle.Truck;
import java.io.File;
import java.sql.Timestamp;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Speedy
 */
public class ControlleralgorithmsTest {
    
    
    public ControlleralgorithmsTest() {
    }
    
    @BeforeClass
    public static void setUpClass() 
    {
        
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() 
    {
        
    }
    
    @After
    public void tearDown() 
    {
        
    }

    /**
     * Test of sortInCommingContainers method, of class Controlleralgorithms.
     */
    @Test
    public void testSortInCommingContainers() {
        System.out.println("sortInCommingContainers");
        
        Settings.messageLog = new MessageLog();
        Settings.port = new Port();
        XmlHandler xml = new XmlHandler();
        List<Container> ContainersFromXML = xml.openXml(new File("C:\\Users\\Speedy\\Desktop\\xml2.xml"));
        Controlleralgorithms.sortInCommingContainers(ContainersFromXML);
        
        for (Container c : ContainersFromXML)
        {
            Controlleralgorithms.sortOutgoingContainer(c);
        }
        
        if (Controlleralgorithms.getScheduledArrivingVehicles().size() != 1)
        {
            fail("List is not 1.");
        }
        Controlleralgorithms.ClearLists();
    }

    /**
     * Test of sortOutgoingContainer method, of class Controlleralgorithms.
     */
    @Test
    public void testSortOutgoingContainer() 
    {
        System.out.println("sortOutgoingContainer");

        Settings.messageLog = new MessageLog();
        Settings.port = new Port();
        XmlHandler xml = new XmlHandler();
        List<Container> ContainersFromXML = xml.openXml(new File("C:\\Users\\Speedy\\Desktop\\xml2.xml"));
        Controlleralgorithms.sortInCommingContainers(ContainersFromXML);
        
        for (Container c : ContainersFromXML)
        {
            Controlleralgorithms.sortOutgoingContainer(c);
        }
        
        
        int Size = Controlleralgorithms.getJobQeueSorted().size();
        System.out.println("truck capacity: " + new Truck(
                ContainersFromXML.get(0).getDepartureDate(),
                ContainersFromXML.get(0).getDepartureTimeFrom(),
                new TruckPlatform(new Vector3f(0, 0, 0)),
                ContainersFromXML.get(0).getArrivalTransportCompany()
                ).getCapicity());
        System.out.println("Unsorted" + Controlleralgorithms.getJobQeueUnsorted().size());
        
        for (Job j : Controlleralgorithms.getJobQeueUnsorted())
        {
            System.out.println("---new vehicle--");
            
            for (Container c : j.getContainers())
            {
                System.out.println(c.toString());
                
            }
        }
        
        if (Size > 15 || Size < 15)
        {
            fail("Jobs not sufficient.");
        }
        
        Controlleralgorithms.ClearLists();
    }

    /**
     * Test of getNextJob method, of class Controlleralgorithms.
     */
    @Test
    public void testGetNextJob() throws Exception 
    {
        System.out.println("getNextJob");
        
        Settings.messageLog = new MessageLog();
        Settings.port = new Port();
        XmlHandler xml = new XmlHandler();
        List<Container> ContainersFromXML = xml.openXml(new File("C:\\Users\\Speedy\\Desktop\\xml2.xml"));
        Controlleralgorithms.sortInCommingContainers(ContainersFromXML);
        
        for (Container c : ContainersFromXML)
        {
            Controlleralgorithms.sortOutgoingContainer(c);
        }
        Platform platform = new BargePlatform(new Vector3f(0, 0, 0));
        
        try
        {
            Job result = Controlleralgorithms.getNextJob(platform, new Timestamp(System.currentTimeMillis()));
        }
        catch (NoJobException e)
        {
            fail(e.getMessage());
        }
        catch (Exception e)
        {
            fail("er gaat iets mis");
        }
        Controlleralgorithms.ClearLists();
    }

    /**
     * Test of checkIncomingVehicles method, of class Controlleralgorithms.
     */
    @Test
    public void testCheckIncomingVehicles() 
    {
        System.out.println("checkIncomingVehicles");
       
        Settings.messageLog = new MessageLog();
        Settings.port = new Port();
        XmlHandler xml = new XmlHandler();
        List<Container> ContainersFromXML = xml.openXml(new File("C:\\Users\\Speedy\\Desktop\\xml2.xml"));
        Controlleralgorithms.sortInCommingContainers(ContainersFromXML);
        
        for (Container c : ContainersFromXML)
        {
            Controlleralgorithms.sortOutgoingContainer(c);
        }
        
        Timestamp timeStamp = new Timestamp(Controlleralgorithms.getFirstDate(ContainersFromXML));
        
        if (Controlleralgorithms.getScheduledArrivingVehicles().size() != 1)
        {
            fail("List is not 1.");
        }
        
        Controlleralgorithms.checkIncomingVehicles(timeStamp);
        
        if (Controlleralgorithms.getScheduledArrivingVehicles().size() > 0)
        {
            fail("List is not empty.");
        }
        Controlleralgorithms.ClearLists();
    }

    /**
     * Test of getFirstDate method, of class Controlleralgorithms.
     */
    @Test
    public void testGetFirstDate() {
        System.out.println("getFirstDate");
        
        XmlHandler xml = new XmlHandler();
        long expResult = 1101855600000L;
        long result = Controlleralgorithms.getFirstDate(xml.openXml(new File("C:\\Users\\Speedy\\Desktop\\xml2.xml")));
        
        // TODO review the generated test code and remove the default call to fail.
        try
        {
            assertEquals(expResult, result);
        }
        catch (AssertionError e)
        {
            fail(result + " != " + expResult);
        }
    }
}