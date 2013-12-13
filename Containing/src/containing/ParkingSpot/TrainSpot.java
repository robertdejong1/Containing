package containing.ParkingSpot;

import containing.Exceptions.InvalidVehicleException;
import containing.Vector3f;
import containing.Vehicle.Train;
import containing.Vehicle.Vehicle;

/**
 * 
 * @author Robert
 */
public class TrainSpot extends ParkingSpot 
{
    public static float length = Train.length;
    public static float width = Train.width;
    
    /**
     * Creates a TrainSpot instance
     * @param position The position of this parkingspot
     */
    public TrainSpot(Vector3f position) 
    {
        super(position);
        this.entryPoint = position;
    }
    
    /**
     * Parks a vehicle in this parkingspot
     * @param VehicleToPark Vehicle to park in this parkingspot
     * @throws InvalidVehicleException 
     */
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
