package containing.ParkingSpot;

import containing.ErrorLog;
import containing.Road.Route;
import containing.Vector3f;
import containing.Vehicle.AGV;
import containing.Vehicle.Vehicle;

public class AgvSpot extends ParkingSpot 
{
    public AgvSpot(Vector3f position) 
    {
        super(position);
    }

    @Override
    public void ParkVehicle(Vehicle VehicleToPark) 
    {
        if (VehicleToPark instanceof AGV)
        {
            
        }
        else
        {
            try 
            {
                throw new Exception("Invalid vehicle type");
            } 
            catch (Exception ex) 
            {
                ErrorLog.logMsg("Invalid vehicle type", ex);
            }
        }
    }

    @Override
    public void UnparkVehicle(Route RouteToFollow) 
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
