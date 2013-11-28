package containing;

public class Point3D {
    
    public int x;
    public int y;
    public int z;

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
    
}
