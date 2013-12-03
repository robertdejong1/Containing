/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package containing.Platform;

import containing.Container;
import containing.Dimension2f;
import containing.ParkingSpot.AgvSpot;
import containing.Point3D;
import containing.Vector3f;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Minardus
 */
public class StorageStripTest {
    
    public StorageStripTest() {
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
     * Test of createJob method, of class StorageStrip.
     */
    @Test
    public void testCreateJob() {
        System.out.println("createJob");
        int stripId = 0;
        Container c = null;
        StorageStrip instance = null;
        instance.createJob(stripId, c);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getJob method, of class StorageStrip.
     */
    @Test
    public void testGetJob() {
        System.out.println("getJob");
        StorageStrip instance = null;
        StorageJob expResult = null;
        StorageJob result = instance.getJob();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of hasContainer method, of class StorageStrip.
     */
    @Test
    public void testHasContainer() {
        System.out.println("hasContainer");
        Container container = null;
        StorageStrip instance = null;
        boolean expResult = false;
        boolean result = instance.hasContainer(container);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of loadContainer method, of class StorageStrip.
     */
    @Test
    public void testLoadContainer() {
        System.out.println("loadContainer");
        StorageJob job = null;
        StorageStrip instance = null;
        instance.loadContainer(job);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of unloadContainer method, of class StorageStrip.
     */
    @Test
    public void testUnloadContainer() {
        System.out.println("unloadContainer");
        Point3D position = null;
        StorageStrip instance = null;
        Container expResult = null;
        Container result = instance.unloadContainer(position);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}
