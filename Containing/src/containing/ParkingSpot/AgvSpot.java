package containing.ParkingSpot;

import containing.Vector3f;
import containing.Vehicle.AGV;

public class AgvSpot extends ParkingSpot 
{
    public static float length = AGV.length;
    public static float width = AGV.width;
    
    public AgvSpot(Vector3f position) 
    {
        super(position);
        this.entryPoint = position;
        this.entryPoint.x += width / 2;
    }
}
