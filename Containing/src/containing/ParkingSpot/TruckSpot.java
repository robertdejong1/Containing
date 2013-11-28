package containing.ParkingSpot;

import containing.Vector3f;
import containing.Vehicle.Truck;

public class TruckSpot extends ParkingSpot 
{
    public static float length = Truck.length;
    public static float width = Truck.width;

    public TruckSpot(Vector3f position) 
    {
        super(position);
        this.entryPoint = position;
        this.entryPoint.x += width / 2;
    }
}
