package containing.ParkingSpot;

import containing.Exceptions.InvalidVehicleException;
import containing.Vector3f;
import containing.Vehicle.AGV;
import containing.Vehicle.Barge;
import containing.Vehicle.Vehicle;

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
    
    @Override
    public void ParkVehicle(Vehicle VehicleToPark) throws InvalidVehicleException{
        if(VehicleToPark instanceof Barge){
            this.ParkedVehicle = VehicleToPark;
        }
        else{
            throw new InvalidVehicleException("Vehicle invalid for this parkingspot");
        }
    }
}
