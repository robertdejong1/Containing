package containing.Vehicle;

import containing.Command;
import containing.CommandHandler;
import containing.Container;
import containing.Exceptions.CargoOutOfBoundsException;
import containing.Exceptions.VehicleOverflowException;
import containing.ParkingSpot.ParkingSpot;
import containing.Platform.Platform;
import containing.Road.Route;
import containing.Vector3f;
import java.util.ArrayList;
import java.util.List;

public abstract class Vehicle 
{ 
    
    protected boolean isLoaded = false;
    protected int capicity;
    protected List<Container> cargo;
    protected static int maxSpeedLoaded;
    protected static int maxSpeedUnloaded;
    protected int currentSpeed;
    protected enum Status{ UNLOADING, LOADING, WAITING, MOVING};
    protected enum Type{ TRUCK, AGV, BARGE, BARGECRANE, SEASHIP, SEASHIPCRANE, TRAIN, TRAINCRANE, TRUCKCRANE, STORAGECRANE};
    private Type vehicleType;
    protected Status status;
    protected Route route;
    protected Platform currentPlatform;
    protected ParkingSpot currentParkingSpot;
    protected Vector3f position;
    private int id;
    
    public Vehicle(int capicity, Platform platform, Type type){
        this.cargo = new ArrayList<Container>();   
        this.isLoaded = cargo.isEmpty() ? false : true;
        this.capicity = capicity;
        this.vehicleType = type;
        this.currentPlatform = platform;
            
    }
    
    protected void setID(int id ){this.id = id;}
    
    public int getID(){return this.id;}

    public void load(Container container) throws VehicleOverflowException, CargoOutOfBoundsException
    {
        
        if (cargo.isEmpty()) isLoaded = true;

        if (cargo.size() < capicity) 
        {
            cargo.add(container);
            
        }
        
        else 
        {
            throw new CargoOutOfBoundsException("CargoOutOfBounds");
        }
        CommandHandler.addCommand(new Command("loadVehicle", this));
    } 
    
    public Vector3f getPosition(){return this.position;}
    
    public int getCurrentSpeed(){return this.currentSpeed;}
    
    public Platform getCurrentPlatform(){return this.currentPlatform;}
    
    public void setCurrentPlatform(Platform platform){this.currentPlatform = platform;}
    
    public void setCurrentParkingSpot(ParkingSpot parkingSpot){this.currentParkingSpot = parkingSpot;}
    
    public void setPosition(Vector3f position){this.position = position;}
    
    public Status getStatus(){return this.status;}
    
    public Type getVehicleType(){return this.vehicleType;}
    
    public void followRoute(Route route){
        this.route = route;
        //currentplatform sign out
        this.status = Status.MOVING;
        this.currentSpeed = (this.isLoaded) ? Vehicle.maxSpeedLoaded : Vehicle.maxSpeedUnloaded;
        CommandHandler.addCommand(new Command("followPath", this));
    } 
    
    public void stopDriving(){
        this.status = Status.WAITING;
        this.currentSpeed = 0;
    }
    
    public abstract int getMaxSpeedLoaded();
    public abstract int getMaxSpeedUnloaded();
    public List<Container> getCargo(){return this.cargo;}
    
    public int getCapicity(){return this.capicity;}
    
    public void update(){
      
        if (this.status == Status.MOVING){this.route.follow(this);}
        
    }
 
   
    
    
    
    

}
