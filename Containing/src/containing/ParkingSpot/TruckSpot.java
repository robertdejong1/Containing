package containing.ParkingSpot;

import containing.Vector3f;

public class TruckSpot extends ParkingSpot 
{
    public static float length = 10;
    public static float width = 10;

    public TruckSpot(Vector3f position, Vector3f entryPoint) 
    {
        super(position, entryPoint);
    }
}
