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
public class Vector3f implements Comparable, Serializable {
    public float x;
    public float y;
    public float z;

    public Vector3f(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public String toString() {
        return "Vector3f{" + "x=" + x + ", y=" + y + ", z=" + z + '}';
    }

    @Override
    public int compareTo(Object o) { 
        Vector3f v2 = (Vector3f) o;
        if (x < v2.x){return -1;}
        if (x > v2.x) {return 1;}
        if (z < v2.z) {return -1;}
        if (z > v2.z) return 1;
        return 0;
    }
}
