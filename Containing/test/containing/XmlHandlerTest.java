/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package containing;

import containing.Exceptions.ParseErrorException;
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
 * @author Robert
 */
public class XmlHandlerTest {

    public XmlHandlerTest() {
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
     * Test of openXml method, of class XmlHandler.
     */
    @Test
    public void testOpenXml() {
        System.out.println("openXml");

        XmlHandler instance = new XmlHandler();

        try {
            List<Container> containers = instance.openXml(new File("C:\\Users\\Speedy\\Desktop\\Github\\xml7.xml"));
            int amountContainers = containers.size();
            //51669 id
            System.out.println(amountContainers);

            assertEquals(amountContainers, 51670);
        } catch (ParseErrorException e) {
            fail("Exception caught" + e.getMessage());
        }
    }
}
