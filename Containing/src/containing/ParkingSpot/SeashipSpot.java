package containing.ParkingSpot;

import containing.Vector3f;

public class SeashipSpot extends ParkingSpot 
{
    private final float LENGHT;
    private final float WIDTH;
    
    public SeashipSpot(Vector3f position, Vector3f entryPoint)
    {
        super(position, entryPoint);
        this.LENGHT = 10; //???
        this.WIDTH = 10; //???
    }
}
