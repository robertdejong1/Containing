/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package containing;

import java.io.Serializable;

/**
 *
 * @author Robert
 */
public class Vector3f implements Serializable {
    public float x;
    public float y;
    public float z;

    /**
     * Creates a Vector3f instance
     * @param x x value
     * @param y y value
     * @param z z value
     */
    public Vector3f(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public String toString() {
        return "Vector3f{" + "x=" + x + ", y=" + y + ", z=" + z + '}';
    }
}
