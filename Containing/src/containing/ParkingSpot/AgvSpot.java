package containing.ParkingSpot;

import containing.Exceptions.InvalidVehicleException;
import containing.Vector3f;
import containing.Vehicle.AGV;
import containing.Vehicle.Vehicle;

/**
 * 
 * @author Robert
 */
public class AgvSpot extends ParkingSpot 
{
    public static float length = AGV.length;
    public static float width = AGV.width;
    
    /**
     * Creates an AgvSpot instance
     * @param position The position of this parkingspot
     */
    public AgvSpot(Vector3f position) 
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
        if(VehicleToPark instanceof AGV){
            this.ParkedVehicle = VehicleToPark;
        }
        else{
            throw new InvalidVehicleException("Vehicle " +VehicleToPark.getVehicleType() +" invalid for this parkingspot");
        }
    }
}
