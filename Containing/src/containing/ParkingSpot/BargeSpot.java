package containing.ParkingSpot;

import containing.Vector3f;
import containing.Vehicle.Barge;

public class BargeSpot extends ParkingSpot 
{
    public static float length = Barge.length;
    public static float width = Barge.width;

    public BargeSpot(Vector3f position) 
    {
        super(position);
        this.entryPoint = position;
        this.entryPoint.x += width / 2;
    }
}
