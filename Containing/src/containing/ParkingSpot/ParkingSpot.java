package containing.ParkingSpot;

import containing.Command;
import containing.CommandHandler;
import containing.Exceptions.InvalidVehicleException;
import containing.Road.Route;
import containing.Vector3f;
import containing.Vehicle.Vehicle;
import java.io.Serializable;
import java.util.HashMap;

public abstract class ParkingSpot implements Serializable
{
    private static int idCounter = 0; //Statische counter die functioneert als auto_increment veld
    protected int id; //id van de parkingspot
    protected Vector3f position; //Positie van de parkingspot
    protected Vehicle ParkedVehicle; //Voortuig die is geparkeerd. Blijft null als de spot leeg is.
    protected Vector3f entryPoint; //In- en uitgang van de parkingspot
    
    
    protected ParkingSpot(Vector3f position) 
    {
        this.id = ParkingSpot.requestNewParkingSpotID();
        this.position = position;
    }
    
    public boolean isEmpty()
    {
        return this.ParkedVehicle == null;
    }
    
    public void UnparkVehicle(Route RouteToFollow) 
    {
        HashMap<String, Object> map = new HashMap<>();
        map.put("parkingSpot", this);
        map.put("route", RouteToFollow);
        CommandHandler.addCommand(new Command("unparkVehicle", map));
        this.ParkedVehicle = null;
    }
    
    public abstract void ParkVehicle(Vehicle VehicleToPark) throws InvalidVehicleException;
    
    private static int requestNewParkingSpotID()
    {
        idCounter++;
        return idCounter;
    }

    public int getId() {
        return id;
    }

    public Vector3f getPosition() {
        return position;
    }

    public Vehicle getParkedVehicle() {
        return ParkedVehicle;
    }

    public Vector3f getEntryPoint() {
        return entryPoint;
    }

    @Override
    public String toString() {
        return "ParkingSpot{" + "id=" + id + ", position=" + position + ", ParkedVehicle=" + ParkedVehicle + ", entryPoint=" + entryPoint + '}';
    }
}
