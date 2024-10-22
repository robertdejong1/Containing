package containing.Vehicle;

import containing.Command;
import containing.CommandHandler;
import containing.Container;
import containing.Exceptions.AgvNotAvailable;
import containing.Exceptions.CargoOutOfBoundsException;
import containing.Exceptions.VehicleOverflowException;
import containing.ParkingSpot.ParkingSpot;
import containing.Platform.Platform;
import containing.Road.Route;
import containing.Settings;
import containing.Vector3f;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class Vehicle implements Serializable 
{ 
    private static int counter = 0;
    public int routecounter = 0;
    protected boolean isLoaded = false;
    protected int capicity;
    protected List<Container> cargo;
    protected int maxSpeedLoaded;
    protected int maxSpeedUnloaded;
    protected int currentSpeed;
    public enum Status{ UNLOADING, LOADING, WAITING, MOVING, INIT };
    protected enum Type{ TRUCK, AGV, BARGE, BARGECRANE, SEASHIP, SEASHIPCRANE, TRAIN, TRAINCRANE, TRUCKCRANE, STORAGECRANE};
    private Type vehicleType;
    protected Status status;
    protected Route route;
    protected Platform currentPlatform;
    protected ParkingSpot currentParkingSpot;
    protected Vector3f position = new Vector3f(1,0,0);
    private int id;

    
    
    public Vehicle(int capicity, Platform platform, Type type){
        this.cargo = new ArrayList<Container>();   
        this.isLoaded = cargo.isEmpty() ? false : true;
        this.capicity = capicity;
        this.vehicleType = type;
        this.currentPlatform = platform;
        this.status = Status.WAITING;
        id = counter;
        counter++;
            
    }
    
    public List<Container> getCargo()
    {
        return this.cargo;
    }
    private void setID(int id ){this.id = id;}
    
    public int getID(){return this.id;}

    public void load(Container container) throws VehicleOverflowException, CargoOutOfBoundsException
    {
        
        if(cargo == null)
            cargo = new ArrayList<>();
        if (cargo.isEmpty()) isLoaded = true;
        
        if (cargo.size() < capicity) 
        {
            cargo.add(container);
            if (this.getStatus() != Status.INIT)
            {
                /*
                HashMap<String, Object> map = new HashMap<>();
                map.put("id", this.getID());
                map.put("vehicleType", this.getVehicleType());
                map.put("container", container);

                CommandHandler.addCommand(new Command("loadVehicle",map));*/
            }
            
            
        }
        
        else 
        {
            Settings.messageLog.AddMessage("VehicleOverflowInVehicle");
            throw new VehicleOverflowException("VehicleOverFlowException");
        }
        
        //CommandHandler.addCommand(new Command("loadVehicle", this));
    } 
    
    public Vector3f getPosition(){return this.position;}
    
    public int getCurrentSpeed(){return this.currentSpeed;}
    
    public Platform getCurrentPlatform(){return this.currentPlatform;}
    
    public void setCurrentPlatform(Platform platform){this.currentPlatform = platform;}
    
    public void setCurrentParkingSpot(ParkingSpot parkingSpot){this.currentParkingSpot = parkingSpot;}
    
    public void setPosition(Vector3f position){this.position = position;}
    
    public Status getStatus(){return this.status;}
    
    public Type getVehicleType(){return this.vehicleType;}
    
    public synchronized void followRoute(Route route) throws AgvNotAvailable{
        if (this.status == Status.MOVING)
        {
           throw new AgvNotAvailable(this.getVehicleType() + " " + this.getID() + " is already following route");
        }
        this.route = route;
        this.status = Status.MOVING;
        //currentplatform sign out
        if (this.getID() == 103){
            System.out.println("AGV103 route: " + this.routecounter);
            System.out.println("----------------------");
            for (Vector3f v : this.route.getWeg()){
                System.out.println("V: " + v);
            }
            System.out.println("----------------------");
        }
        this.currentSpeed = (this.isLoaded) ? this.maxSpeedLoaded : this.maxSpeedUnloaded;
        float duration = (route.getDistance()*10)/(float)((float)this.getCurrentSpeed()*1000f/3600f);
        HashMap<String, Object> map = new HashMap<>();
            
        map.put("id", this.getID());
        map.put("vehicleType", this.getVehicleType());
        map.put("motionPath", route); 
        //map.put("speed", currentSpeed);
        map.put("duration", duration);
            
        CommandHandler.addCommand(new Command("followPath", map));
        
        Settings.messageLog.AddMessage("Vehicle: " + this.getID() + "start following path" );
        Settings.messageLog.AddMessage("VehicleStatus: " + this.status);
        Settings.messageLog.AddMessage("Route: " + this.route);
     
        
        
    } 
    
    public void stopDriving(){
        Settings.messageLog.AddMessage("Stop driving");
        this.routecounter++;
        this.status = Status.WAITING;
        this.currentSpeed = 0;
        this.route = null;
    }
    
    public abstract int getMaxSpeedLoaded();
    public abstract int getMaxSpeedUnloaded();

    
    public int getCapicity(){return this.capicity;}
    
    public void update(){
        
        if (this.getStatus() == Status.MOVING){
            
            this.route.follow(this);
        }
        
    }
 
   
    
    
    
    

}
