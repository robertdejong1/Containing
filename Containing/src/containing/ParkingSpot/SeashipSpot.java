package containing.ParkingSpot;

import containing.Exceptions.InvalidVehicleException;
import containing.Vector3f;
import containing.Vehicle.Seaship;
import containing.Vehicle.Vehicle;

public class SeashipSpot extends ParkingSpot 
{
    public static float length = Seaship.length;
    public static float width = Seaship.width;
    
    /**
     * Creates a SeashipSpot instance
     * @param position The position of the parkingspot
     */
    public SeashipSpot(Vector3f position)
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
        if(VehicleToPark instanceof Seaship){
            this.ParkedVehicle = VehicleToPark;
        }
        else{
            throw new InvalidVehicleException("Vehicle invalid for this parkingspot");
        }
    }
}
