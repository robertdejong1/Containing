package containing.ParkingSpot;

import containing.Road.Route;
import containing.Vector3f;
import containing.Vehicle.Vehicle;

public class BargeSpot extends ParkingSpot 
{

    public BargeSpot(Vector3f position) 
    {
        super(position);
    }

    @Override
    public void ParkVehicle(Vehicle VehicleToPark) 
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void UnparkVehicle(Route RouteToFollow) 
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
