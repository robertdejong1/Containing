/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package containing.Road;

import containing.Platform.Platform;
import containing.Vector3f;
import containing.Vehicle.Vehicle;
import java.util.Arrays;
import java.util.Collections;
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
public class RoadTest {
    List<Vector3f> road1 = Arrays.asList(new Vector3f(0f,0f,0f),new Vector3f(3f,0f,0f), new Vector3f(2f,0f,0f), new Vector3f(5f,0f,0f),new Vector3f(1f,0f,0f));
    Road road;
    
    public RoadTest() {
        road = new Road(road1);
        
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
    public void testSortRoad()
    {
        //Road road = new Road(road1);
        
        Collections.sort(road1);
        
        for(Vector3f v : road1)
        {
            System.out.println(v);
        }
       
        assertEquals(0,(int)road1.get(0).x);
        assertEquals(1,(int)road1.get(1).x);
        assertEquals(2,(int)road1.get(2).x);
        assertEquals(3,(int)road1.get(3).x);
        assertEquals(5,(int)road1.get(4).x);
        
    }
    
    @Test
    public void testPathLength()
    {
       Collections.sort(road1);
       float expected = Road.getPathLength(road1);
       
       assertEquals(5,(int)expected);
       
    }

    /**
     * Test of getPathLength method, of class Road.
     */
 
}