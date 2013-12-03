/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package containing;


import containing.Exceptions.CargoOutOfBoundsException;
import containing.Exceptions.VehicleOverflowException;
import containing.Vehicle.Barge;
import java.io.File;
import java.util.List;
import static junit.framework.Assert.fail;
import junit.framework.TestCase;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Miriam
 */
public class VehicleTest extends TestCase {
    
    public VehicleTest() {
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
    public void testUnloading()
    {
        XmlHandler xml = new XmlHandler();
        List<Container> ContainersFromXML = xml.openXml(new File("C:\\Users\\Miriam\\Desktop\\xml1.xml"));    
        Container bc = ContainersFromXML.get(0);
        Container container0 = new Container(bc.getContainerId(),bc.getArrivalDate(), bc.getArrivalTimeFrom(), bc.getArrivalTimeFrom(), bc.getArrivalTransport(),bc.getOwner(), new Vector3f(1,1,1), bc.getArrivalTransportCompany(), bc.getDepartureDate(), bc.getDepartureTimeFrom(),bc.getDepartureTimeTill(), bc.getDepartureTransport());
        Barge barge = new Barge(null,0,null,null);
        
        // unload empty barge
        try
        {
            barge.unload();
        }
        catch(Exception e)
        {
            
        }
    }
    /**
     * TestLoadingExternVehicle
     */
    @Test
    public void testLoading() 
    {
       XmlHandler xml = new XmlHandler();
       List<Container> ContainersFromXML = xml.openXml(new File("C:\\Users\\Miriam\\Desktop\\xml1.xml"));
       Container bc = ContainersFromXML.get(0);
       Container container0 = new Container(bc.getContainerId(),bc.getArrivalDate(), bc.getArrivalTimeFrom(), bc.getArrivalTimeFrom(), bc.getArrivalTransport(),bc.getOwner(), new Vector3f(1,1,1), bc.getArrivalTransportCompany(), bc.getDepartureDate(), bc.getDepartureTimeFrom(),bc.getDepartureTimeTill(), bc.getDepartureTransport());
       Container container1 = new Container(bc.getContainerId(),bc.getArrivalDate(), bc.getArrivalTimeFrom(), bc.getArrivalTimeFrom(), bc.getArrivalTransport(),bc.getOwner(), new Vector3f(-1,-1,-1), bc.getArrivalTransportCompany(), bc.getDepartureDate(), bc.getDepartureTimeFrom(),bc.getDepartureTimeTill(), bc.getDepartureTransport());
       Container container2 = new Container(bc.getContainerId(),bc.getArrivalDate(), bc.getArrivalTimeFrom(), bc.getArrivalTimeFrom(), bc.getArrivalTransport(),bc.getOwner(), new Vector3f(10,10,10), bc.getArrivalTransportCompany(), bc.getDepartureDate(), bc.getDepartureTimeFrom(),bc.getDepartureTimeTill(), bc.getDepartureTransport());
       Container container3 = new Container(bc.getContainerId(),bc.getArrivalDate(), bc.getArrivalTimeFrom(), bc.getArrivalTimeFrom(), bc.getArrivalTransport(),bc.getOwner(), new Vector3f(0,0,0), bc.getArrivalTransportCompany(), bc.getDepartureDate(), bc.getDepartureTimeFrom(),bc.getDepartureTimeTill(), bc.getDepartureTransport());
       Container container4 = new Container(bc.getContainerId(),bc.getArrivalDate(), bc.getArrivalTimeFrom(), bc.getArrivalTimeFrom(), bc.getArrivalTransport(),bc.getOwner(), new Vector3f(Barge.nrContainersDepth-1, Barge.nrContainersWidth-1, Barge.nrContainersHeight-1), bc.getArrivalTransportCompany(), bc.getDepartureDate(), bc.getDepartureTimeFrom(),bc.getDepartureTimeTill(), bc.getDepartureTransport());
       Container container5 = new Container(bc.getContainerId(),bc.getArrivalDate(), bc.getArrivalTimeFrom(), bc.getArrivalTimeFrom(), bc.getArrivalTransport(),bc.getOwner(), new Vector3f(Barge.nrContainersDepth, Barge.nrContainersWidth, Barge.nrContainersHeight), bc.getArrivalTransportCompany(), bc.getDepartureDate(), bc.getDepartureTimeFrom(),bc.getDepartureTimeTill(), bc.getDepartureTransport());
       
       Barge barge = new Barge(null,0,null,null); //date/float/platform/string
       
       try
       {
            barge.load(container0);
       }
      
       catch(Exception e)
       {
            System.out.println("exception");
       }
       
       if (barge.getCargo().size() != 1) { fail("List cargo not 1"); }
       
       if (barge.getGrid()[(int)container0.getArrivalPosition().x][(int)container0.getArrivalPosition().y][(int)container0.getArrivalPosition().z] != container0)
       {
           fail("Container not loaded to correct position");
       }
       
       //load container to already taken position
       try
       {
            barge.load(container0);
       }
      
       catch(Exception e)
       {
           System.out.println(e.getClass());
           if (e.getClass() != new VehicleOverflowException("").getClass())
           {
               fail("error");
           }
       }
       
       if (barge.getCargo().size() != 1) {fail("List cargo not 1");}

       if (barge.getGrid()[(int)container0.getArrivalPosition().x][(int)container0.getArrivalPosition().y][(int)container0.getArrivalPosition().z] != container0)
       {
           fail("Container not loaded to correct position");
       }
       
       
       try
       {
           barge.load(container1);
       }
       catch(Exception e)
       {
           System.out.println(e.getClass());
       }
       
       try
       {
           barge.load(container2);
       }
       catch(Exception e)
       {
           System.out.println(e.getClass());
       }
       
       try
       {
           barge.load(container3);
       }
       catch(Exception e)
       {
           fail("Should not fail");
       }
       
       try
       {
           barge.load(container4);
       }
       catch(Exception e)
       {
           assertEquals(CargoOutOfBoundsException.class, e.getClass());
       }    
       
       try
       {
           barge.load(container5);
           fail("Should fail");
       }
       catch(Exception e)
       {
           
       }    
       
       
       
    }
    
    
    

 

   
}