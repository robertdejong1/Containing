package containing.ParkingSpot;

import containing.Vector3f;
import containing.Vehicle.Train;

public class TrainSpot extends ParkingSpot 
{
    public static float length = Train.length;
    public static float width = Train.width;
    
    public TrainSpot(Vector3f position) 
    {
        super(position);
        this.entryPoint = position;
        this.entryPoint.x += width / 2;
    }
}
