/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package containing.Vehicle;

import containing.Container;
import containing.Exceptions.CargoOutOfBoundsException;
import containing.Exceptions.ContainerNotFoundException;
import containing.Exceptions.VehicleOverflowException;
import containing.Vector3f;
import containing.XmlHandler;
import java.io.File;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
/**
 *
 * @author Miriam
 */
public class Load {
     public Load() {

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
    public void testLoading() 
    {
       XmlHandler xml = new XmlHandler();
       List<Container> ContainersFromXML = xml.openXml(new File("C:\\Users\\Miriam\\Desktop\\xml1.xml"));
       Container bc = ContainersFromXML.get(0);
       Container container0 = new Container(bc.getContainerId(),bc.getArrivalDate(), bc.getArrivalTimeFrom(), bc.getArrivalTimeFrom(), bc.getArrivalTransport(),bc.getOwner(), new Vector3f(1,1,1), bc.getArrivalTransportCompany(), bc.getDepartureDate(), bc.getDepartureTimeFrom(),bc.getDepartureTimeTill(), bc.getDepartureTransport());
       Container container1 = new Container(bc.getContainerId(),bc.getArrivalDate(), bc.getArrivalTimeFrom(), bc.getArrivalTimeFrom(), bc.getArrivalTransport(),bc.getOwner(), new Vector3f(-1,-1,-1), bc.getArrivalTransportCompany(), bc.getDepartureDate(), bc.getDepartureTimeFrom(),bc.getDepartureTimeTill(), bc.getDepartureTransport());
     
       Container container2 = new Container(bc.getContainerId(),bc.getArrivalDate(), bc.getArrivalTimeFrom(), bc.getArrivalTimeFrom(), bc.getArrivalTransport(),bc.getOwner(), new Vector3f(0,0,0), bc.getArrivalTransportCompany(), bc.getDepartureDate(), bc.getDepartureTimeFrom(),bc.getDepartureTimeTill(), bc.getDepartureTransport());
       Container container3 = new Container(bc.getContainerId(),bc.getArrivalDate(), bc.getArrivalTimeFrom(), bc.getArrivalTimeFrom(), bc.getArrivalTransport(),bc.getOwner(), new Vector3f(Barge.nrContainersWidth-1, Barge.nrContainersHeight-1,Barge.nrContainersDepth-1), bc.getArrivalTransportCompany(), bc.getDepartureDate(), bc.getDepartureTimeFrom(),bc.getDepartureTimeTill(), bc.getDepartureTransport());
       Container container4 = new Container(bc.getContainerId(),bc.getArrivalDate(), bc.getArrivalTimeFrom(), bc.getArrivalTimeFrom(), bc.getArrivalTransport(),bc.getOwner(), new Vector3f(Barge.nrContainersWidth, Barge.nrContainersHeight,Barge.nrContainersDepth), bc.getArrivalTransportCompany(), bc.getDepartureDate(), bc.getDepartureTimeFrom(),bc.getDepartureTimeTill(), bc.getDepartureTransport());
       
       Barge barge = new Barge(null,0,null,null); //date/float/platform/string
       
       //load container to empty barge
       try
       {
            barge.load(container0);
            assertEquals(1, barge.getCargo().size());
            assertEquals(container0, barge.getGrid()[(int)container0.getArrivalPosition().x][(int)container0.getArrivalPosition().y][(int)container0.getArrivalPosition().z]);
       }
      
       catch(Exception e)
       {
            fail("Should not fail");
       }
       
       //load container to already taken position
       try
       {
            barge.load(container0);
            fail("Should fail");
       }
      
       catch(Exception e)
       {
           assertEquals(VehicleOverflowException.class,e.getClass());
           assertEquals(1, barge.getCargo().size());
           assertEquals(container0, barge.getGrid()[1][1][1]);
       }

       //load container met arrivalPosition (-1,-1,-1)
       try
       {
           barge.load(container1);
           fail("Should fail");
       }
       catch(Exception e)
       {
           assertEquals(CargoOutOfBoundsException.class,e.getClass());
           assertEquals(1, barge.getCargo().size());
           assertEquals(container0, barge.getGrid()[1][1][1]);
       }
       
       //load container met arrivalPosition (0,0,0)
       try
       {
           barge.load(container2);
           assertEquals(2, barge.getCargo().size());
           assertEquals(container0, barge.getGrid()[1][1][1]);
           assertEquals(container2, barge.getGrid()[0][0][0]);
       }
       catch(Exception e)
       {
           fail("Should not fail1 loading container2");
       }
       
       // load container met arrivalPosition ( Barge.nrContainersWidth-1, Barge.nrContainersHeight-1,Barge.nrContainersDepth-1 )
       try
       {   
           System.out.println(container3.getArrivalPosition());
           barge.load(container3);
           assertEquals(3, barge.getCargo().size());
           assertEquals(container0, barge.getGrid()[(int)container0.getArrivalPosition().x][(int)container0.getArrivalPosition().y][(int)container0.getArrivalPosition().z]);
           assertEquals(container2, barge.getGrid()[0][0][0]);
           assertEquals(container3, barge.getGrid()[4][2][5]);
       }
       catch(Exception e)
       {
           fail("Should not fail loading container3");
       }    
       
       try
       {
           barge.load(container4);
           fail("Should fail loading container 4");
       }
       catch(Exception e)
       {
           assertEquals(CargoOutOfBoundsException.class, e.getClass());
           assertEquals(3, barge.getCargo().size());
           assertEquals(container0, barge.getGrid()[(int)container0.getArrivalPosition().x][(int)container0.getArrivalPosition().y][(int)container0.getArrivalPosition().z]);
           assertEquals(container2, barge.getGrid()[0][0][0]);
           assertEquals(container3, barge.getGrid()[4][2][5]);
       }
    }
}
