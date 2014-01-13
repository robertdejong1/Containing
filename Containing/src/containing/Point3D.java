package containing;

import java.awt.Point;
import java.io.Serializable;

public class Point3D implements Serializable {
    
    public int x;
    public int y;
    public int z;

    /**
     * Creates a Point3D instance
     * @param x x value
     * @param y y value
     * @param z z value
     */
    public Point3D(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    @Override
    public String toString()
    {
        return String.format("Point3D:[%d,%d,%d]", x, y, z);  
    }
    
    @Override
    public boolean equals(Object o) {
        if(o instanceof Point3D) {
            Point3D p = (Point3D)o;
            return (p.x == x) && (p.y == y) && (p.z == z);
        }
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + this.x;
        hash = 53 * hash + this.y;
        hash = 53 * hash + this.z;
        return hash;
    }
    
}
