package containing.ParkingSpot;

import containing.Exceptions.InvalidVehicleException;
import containing.Vector3f;
import containing.Vehicle.Truck;
import containing.Vehicle.Vehicle;

/**
 * 
 * @author Robert
 */
public class TruckSpot extends ParkingSpot 
{
    public static float length = Truck.length;
    public static float width = Truck.width;

    /**
     * Creates a TruckSpot instance
     * @param position The position of this parkingspot
     */
    public TruckSpot(Vector3f position) 
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
        if(VehicleToPark instanceof Truck){
            this.ParkedVehicle = VehicleToPark;
        }
        else{
            throw new InvalidVehicleException("Vehicle invalid for this parkingspot");
        }
    }
}
