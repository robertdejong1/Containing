package containing.ParkingSpot;

import containing.Road.Route;
import containing.Vector3f;
import containing.Vehicle.Vehicle;

public class TruckSpot extends ParkingSpot 
{
    private final float LENGHT;
    private final float WIDTH;

    public TruckSpot(Vector3f position, Vector3f entryPoint) 
    {
        super(position, entryPoint);
        this.LENGHT = 10; //???
        this.WIDTH = 10; //???
    }
}
