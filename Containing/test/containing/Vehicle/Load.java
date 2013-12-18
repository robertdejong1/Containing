/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package containing.Vehicle;

import containing.Container;
import containing.Exceptions.CargoOutOfBoundsException;
import containing.Exceptions.VehicleOverflowException;

import containing.Vector3f;
import containing.XmlHandler;
import java.io.File;
import java.util.ArrayList;
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
    List<Container> containers = new ArrayList<>();
    XmlHandler xml = new XmlHandler();
    public Load()
    {
       try
       {
        List<Container> ContainersFromXML = xml.openXml(new File("C:\\Users\\Miriam\\Desktop\\xml1.xml"));
        Container bc = ContainersFromXML.get(0);
        Container container0 = new Container(bc.getContainerId(),bc.getArrivalDate(), bc.getArrivalTimeFrom(), bc.getArrivalTimeFrom(), bc.getArrivalTransport(),bc.getOwner(), new Vector3f(1,1,1), bc.getArrivalTransportCompany(), bc.getDepartureDate(), bc.getDepartureTimeFrom(),bc.getDepartureTimeTill(), bc.getDepartureTransport());
        Container container1 = new Container(bc.getContainerId(),bc.getArrivalDate(), bc.getArrivalTimeFrom(), bc.getArrivalTimeFrom(), bc.getArrivalTransport(),bc.getOwner(), new Vector3f(-1,-1,-1), bc.getArrivalTransportCompany(), bc.getDepartureDate(), bc.getDepartureTimeFrom(),bc.getDepartureTimeTill(), bc.getDepartureTransport());

        Container container2 = new Container(bc.getContainerId(),bc.getArrivalDate(), bc.getArrivalTimeFrom(), bc.getArrivalTimeFrom(), bc.getArrivalTransport(),bc.getOwner(), new Vector3f(0,0,0), bc.getArrivalTransportCompany(), bc.getDepartureDate(), bc.getDepartureTimeFrom(),bc.getDepartureTimeTill(), bc.getDepartureTransport());
        Container container3 = new Container(bc.getContainerId(),bc.getArrivalDate(), bc.getArrivalTimeFrom(), bc.getArrivalTimeFrom(), bc.getArrivalTransport(),bc.getOwner(), new Vector3f(1,0,0), bc.getArrivalTransportCompany(), bc.getDepartureDate(), bc.getDepartureTimeFrom(),bc.getDepartureTimeTill(), bc.getDepartureTransport());
        Container container4 = new Container(bc.getContainerId(),bc.getArrivalDate(), bc.getArrivalTimeFrom(), bc.getArrivalTimeFrom(), bc.getArrivalTransport(),bc.getOwner(), new Vector3f(1,1,0), bc.getArrivalTransportCompany(), bc.getDepartureDate(), bc.getDepartureTimeFrom(),bc.getDepartureTimeTill(), bc.getDepartureTransport());
        
        containers.add(container0);
        containers.add(container1);
        containers.add(container2);
        containers.add(container3);
        containers.add(container4);
       }
       catch(Exception e)
       {
           
       }
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
    public void testLoadRow()
    {
       
        
  
     
       
       Barge barge = new Barge(null,0,null,null); //date/float/platform/string
       
       BargeCrane bg = new BargeCrane(new Vector3f(0f,0f,0f), null);
       
       try{
           
            barge.load(containers.get(4));
            barge.load(containers.get(2));
            barge.load(containers.get(3));

            barge.load(containers.get(0));
       }
       catch(Exception e){System.out.println("Barge: " + e.getMessage());}
       try
       {
        bg.load(barge, 1);
       }
       catch(Exception e){System.out.println("Barge: " + e.getMessage()); }
       
    }
    
    
    @Test
    public void testLoading() 
    {

       
       Barge barge = new Barge(null,0,null,null); //date/float/platform/string
       
       //load container to empty barge
       try
       {
            barge.load(containers.get(0));
            assertEquals(1, barge.getCargo().size());
            assertEquals(containers.get(0), barge.getGrid()[(int)containers.get(0).getArrivalPosition().x][(int)containers.get(0).getArrivalPosition().y][(int)containers.get(0).getArrivalPosition().z]);
       }
      
       catch(Exception e)
       {
            fail("Should not fail");
       }
       
       //load container to already taken position
       try
       {
            barge.load(containers.get(0));
            fail("Should fail");
       }
      
       catch(Exception e)
       {
           assertEquals(VehicleOverflowException.class,e.getClass());
           assertEquals(1, barge.getCargo().size());
           assertEquals(containers.get(0), barge.getGrid()[1][1][1]);
       }

       //load container met arrivalPosition (-1,-1,-1)
       try
       {
           barge.load(containers.get(1));
           fail("Should fail");
       }
       catch(Exception e)
       {
           assertEquals(CargoOutOfBoundsException.class,e.getClass());
           assertEquals(1, barge.getCargo().size());
           assertEquals(containers.get(0), barge.getGrid()[1][1][1]);
       }
       
       //load container met arrivalPosition (0,0,0)
       try
       {
           barge.load(containers.get(2));
           assertEquals(2, barge.getCargo().size());
           assertEquals(containers.get(0), barge.getGrid()[1][1][1]);
           assertEquals(containers.get(2), barge.getGrid()[0][0][0]);
       }
       catch(Exception e)
       {
           fail("Should not fail1 loading container2");
       }
       
       // load container met arrivalPosition ( Barge.nrContainersWidth-1, Barge.nrContainersHeight-1,Barge.nrContainersDepth-1 )
       try
       {   
           System.out.println(containers.get(3).getArrivalPosition());
           barge.load(containers.get(3));
           assertEquals(3, barge.getCargo().size());
           assertEquals(containers.get(0), barge.getGrid()[(int)containers.get(0).getArrivalPosition().x][(int)containers.get(0).getArrivalPosition().y][(int)containers.get(0).getArrivalPosition().z]);
           assertEquals(containers.get(2), barge.getGrid()[0][0][0]);
           assertEquals(containers.get(3), barge.getGrid()[4][2][5]);
       }
       catch(Exception e)
       {
           fail("Should not fail loading container3");
       }    
       
       try
       {
           barge.load(containers.get(4));
           fail("Should fail loading container 4");
       }
       catch(Exception e)
       {
           assertEquals(CargoOutOfBoundsException.class, e.getClass());
           assertEquals(3, barge.getCargo().size());
           assertEquals(containers.get(0), barge.getGrid()[(int)containers.get(0).getArrivalPosition().x][(int)containers.get(0).getArrivalPosition().y][(int)containers.get(0).getArrivalPosition().z]);
           assertEquals(containers.get(2), barge.getGrid()[0][0][0]);
           assertEquals(containers.get(3), barge.getGrid()[4][2][5]);
       }
    }
    
    
}
