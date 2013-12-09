/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package containing.Vehicle;

import containing.Container;
import containing.Exceptions.ContainerNotFoundException;
import containing.Vector3f;
import containing.XmlHandler;
import java.io.File;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;
/**
 *
 * @author Miriam
 */
public class Unload {
    
    XmlHandler xml = new XmlHandler();
    List<Container> ContainersFromXML = xml.openXml(new File("C:\\Users\\Miriam\\Desktop\\xml2.xml")); 
    
    Container bc = ContainersFromXML.get(0);
    Container container0 = new Container(bc.getContainerId(),bc.getArrivalDate(), bc.getArrivalTimeFrom(), bc.getArrivalTimeFrom(), bc.getArrivalTransport(),bc.getOwner(), new Vector3f(1,1,1), bc.getArrivalTransportCompany(), bc.getDepartureDate(), bc.getDepartureTimeFrom(),bc.getDepartureTimeTill(), bc.getDepartureTransport());
    Container container1 = new Container(bc.getContainerId(),bc.getArrivalDate(), bc.getArrivalTimeFrom(), bc.getArrivalTimeFrom(), bc.getArrivalTransport(),bc.getOwner(), new Vector3f(1,1,1), bc.getArrivalTransportCompany(), bc.getDepartureDate(), bc.getDepartureTimeFrom(),bc.getDepartureTimeTill(), bc.getDepartureTransport());
    Barge barge = new Barge(null,0,null,null);
    
    @Test
    public void testUnload()
    {
        
        
        //unloaded vehicle
        try
        {
            barge.unload();
            fail("Should fail");
        }
        catch(Exception e)
        {
            assertEquals(ContainerNotFoundException.class, e.getClass());
            assertEquals(0, barge.getCargo().size());
            
        }
        
        //load vehicle and unload same container
        try
        {
            barge.load(container0);
           
            barge.unload();
            
            assertEquals(0, barge.getCargo().size());
            assertEquals(null,barge.getGrid()[1][1][1]);

        }
        catch(Exception e)
        {
           fail("Should not fail");
        }
        
       //load vehicle and unload same container
        try
        {
            barge.load(container0);
            barge.unload(container0);
            assertEquals(0, barge.getCargo().size());
            assertEquals(null,barge.getGrid()[1][1][1]);
        }
        catch(Exception e)
        {
            fail("Should not fail");
        }
        
        //load vehicle with container0 and unload container1 (has same position on grid)
        try
        {
            barge.load(container0);
            barge.unload(container1);
            fail("Should fail unloading container1");
          
        }
        catch(Exception e)
        {
            assertEquals(ContainerNotFoundException.class, e.getClass());
            assertEquals(1, barge.getCargo().size());
            assertEquals(container0,barge.getGrid()[1][1][1]);
        }
      
    }
    
    @Test
    public void testGetContainerHighestPriority()
    {
        
        /*
        for (Container container : ContainersFromXML ){ 
            try
            {
                barge.load(container);
            }
            catch(Exception e)
            {
                System.out.println(container.getArrivalPosition());
            }
            
        }*/
        //Date date = new Date(2004,11,12);
        try
        {
            barge.load(container0);
        }
        catch(Exception e)
        {
            System.out.println("Error @ priority");
        }
        
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(Timestamp.valueOf("0004-12-13 00:00:00.0"));
        
       
        Date date = new Date(calendar.getTime().getYear(), calendar.getTime().getMonth(), calendar.getTime().getDate());

        //this.ContainersFromXML.get(0).getDepartureDate().setYear(date.getYear());

        System.out.println(date.compareTo(this.ContainersFromXML.get(0).getDepartureDate()));
        //System.out.println("Priority: " + barge.getContainerWithHighestPriority().size());
        //System.out.println("Priority: " + this.ContainersFromXML.get(3).getArrivalDate());
    }
}
