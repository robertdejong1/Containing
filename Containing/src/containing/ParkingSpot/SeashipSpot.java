package containing.ParkingSpot;

import containing.Vector3f;
import containing.Vehicle.Seaship;

public class SeashipSpot extends ParkingSpot 
{
    public static float length = Seaship.length;
    public static float width = Seaship.width;
    
    public SeashipSpot(Vector3f position)
    {
        super(position);
        this.entryPoint = position;
        this.entryPoint.x += width / 2;
    }
}
