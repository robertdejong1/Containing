package containing.ParkingSpot;

import containing.Exceptions.InvalidVehicleException;
import containing.Vector3f;
import containing.Vehicle.AGV;
import containing.Vehicle.Train;
import containing.Vehicle.Vehicle;

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
    
    @Override
    public void ParkVehicle(Vehicle VehicleToPark) throws InvalidVehicleException{
        if(VehicleToPark instanceof Train){
            this.ParkedVehicle = VehicleToPark;
        }
        else{
            throw new InvalidVehicleException("Vehicle invalid for this parkingspot");
        }
    }
}
