package containing.ParkingSpot;

import containing.Road.Route;
import containing.Vector3f;
import containing.Vehicle.Vehicle;

public abstract class ParkingSpot 
{
    public static float length;
    public static float width;
    private static int idCounter = 0;
    
    protected int id;
    protected Vector3f position;
    protected Vehicle ParkedVehicle;
    //waypoint ingang en uitgang?
    
    protected ParkingSpot(Vector3f position) 
    {
        this.id = ParkingSpot.requestNewParkingSpotID();
        this.position = position;
        this.ParkedVehicle = null;
    }
    
    public int GetParkingSpotID()
    {
        return this.id;
    }
    
    public boolean isEmpty()
    {
        if (this.ParkedVehicle == null)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    
    public abstract void ParkVehicle(Vehicle VehicleToPark);
    public abstract void UnparkVehicle(Route RouteToFollow);
    
    private static int requestNewParkingSpotID()
    {
        idCounter++;
        return idCounter;
    }
    
}
