package containing.ParkingSpot;

import containing.Vector3f;

public class AgvSpot extends ParkingSpot 
{
    public static float length = 10;
    public static float width = 10;
    
    public AgvSpot(Vector3f position, Vector3f entryPoint) 
    {
        super(position, entryPoint);
    }
}
