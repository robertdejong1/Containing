package containing.ParkingSpot;

import containing.Vector3f;

public class AgvSpot extends ParkingSpot 
{
    private final float LENGTH;
    private final float WIDTH;
    
    public AgvSpot(Vector3f position, Vector3f entryPoint) 
    {
        super(position, entryPoint);
        this.LENGTH = 10; //???
        this.WIDTH = 10; //???
    }

    public float getLENGTH() {
        return LENGTH;
    }

    public float getWIDTH() {
        return WIDTH;
    }
}
