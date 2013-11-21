/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package containing;

/**
 *
 * @author Robert
 */
public class Vector3f {
    public int x;
    public int y;
    public int z;

    public Vector3f(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    @Override
    public String toString(){
        return "Vector:[" + x +", " + y +", " +z +"]";   
    }
    
}
