/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package containing.Road;

import containing.Vector3f;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author Miriam
 */
public class Road {
    
    @Test
    public void testSortRoad()
    {
        List<Vector3f> road1 = Arrays.asList(new Vector3f(0,0,0),new Vector3f(3,0,0), new Vector3f(2,0,0), new Vector3f(5,0,0),new Vector3f(1,0,0));
        Collections.sort(road1);
        assertEquals(road1,Arrays.asList(new Vector3f(0,0,0),new Vector3f(1,0,0), new Vector3f(2,0,0), new Vector3f(3,0,0),new Vector3f(5,0,0)));
        
        
    }
}
