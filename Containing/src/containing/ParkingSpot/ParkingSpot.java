package containing.ParkingSpot;

import containing.Command;
import containing.CommandHandler;
import containing.Exceptions.InvalidVehicleException;
import containing.Vector3f;
import containing.Vehicle.Vehicle;
import java.io.Serializable;
import java.util.HashMap;

/**
 * 
 * @author Robert
 */
public abstract class ParkingSpot implements Serializable
{
    private static int idCounter = 0; //Statische counter die functioneert als auto_increment veld
    protected int id; //id van de parkingspot
    protected Vector3f position; //Positie van de parkingspot
    protected Vehicle ParkedVehicle; //Voortuig die is geparkeerd. Blijft null als de spot leeg is.
    protected Vector3f entryPoint; //In- en uitgang van de parkingspot
    
    /**
     * Creates a ParkingSpot instance
     * @param position The position of this parkingspot
     */
    protected ParkingSpot(Vector3f position) 
    {
        this.id = ParkingSpot.requestNewParkingSpotID();
        this.position = position;
    }
    
    /**
     * Checks wether this parkingspot is empty
     * @return true if empty, false otherwise
     */
    public boolean isEmpty()
    {
        return this.ParkedVehicle == null;
    }
    
    /**
     * Unparks a parked vehicle from this parkingspot
     */
    public void UnparkVehicle() 
    {
        HashMap<String, Object> map = new HashMap<>();
        map.put("parkingSpot", this);
        CommandHandler.addCommand(new Command("unparkVehicle", map));
        this.ParkedVehicle = null;
    }
    
    /**
     * Parks a vehicle in this parkingspot
     * @param VehicleToPark Vehicle to park in this parkingspot
     * @throws InvalidVehicleException 
     */
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
