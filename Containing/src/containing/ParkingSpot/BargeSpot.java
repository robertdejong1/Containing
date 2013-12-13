package containing.ParkingSpot;

import containing.Exceptions.InvalidVehicleException;
import containing.Vector3f;
import containing.Vehicle.Barge;
import containing.Vehicle.Vehicle;

/**
 * 
 * @author Robert
 */
public class BargeSpot extends ParkingSpot 
{
    public static float length = Barge.length;
    public static float width = Barge.width;

     /**
     * Creates an BargeSpot instance
     * @param position The position of this parkingspot
     */
    public BargeSpot(Vector3f position) 
    {
        super(position);
        this.entryPoint = position;
        this.entryPoint.x += width / 2;
    }
    
     /**
     * Parks a vehicle in this parkingspot
     * @param VehicleToPark Vehicle to park in this parkingspot
     * @throws InvalidVehicleException 
     */
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
