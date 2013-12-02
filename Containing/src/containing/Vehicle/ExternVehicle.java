/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package containing.Vehicle;

import containing.Command;
import containing.CommandHandler;
import containing.Container;
import containing.Exceptions.CargoOutOfBoundsException;
import containing.Exceptions.ContainerNotFoundException;
import containing.Exceptions.VehicleOverflowException;
import containing.Platform.Platform;
import containing.Vector3f;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Miriam
 */
public abstract class ExternVehicle extends Vehicle {
    
    private Date arrivalDate;
    private float arrivalTime;
    private String company;
    protected Container[][][] grid;
    private List<Container> priorityCargo = new ArrayList<Container>();
    private int nrContainersDepth;
    private int nrContainersHeight;
    private int nrContainersWidth;
    
    public ExternVehicle(Date arrivalDate, float arrivalTime, int depthGrid,int widthGrid,int heightGrid, Platform platform, String company, Type type){
        super(depthGrid*widthGrid*heightGrid, platform, type);
        this.arrivalDate = arrivalDate;
        this.arrivalTime = arrivalTime;
        status = Status.WAITING; 
        this.grid = new Container[depthGrid][widthGrid][heightGrid];
        this.company = company;
        this.nrContainersDepth = depthGrid;
        this.nrContainersHeight = heightGrid;
        this.nrContainersWidth = widthGrid;
    }
    
   
    
    public String getCompanyName(){return this.company;}
    
    public void getContainerWithHighestPriority(){
        List<Container> priority = new ArrayList<>();
        for (Container container : this.cargo){
            Date currentDate = new Date();
            if (currentDate.getDay() >= container.getDepartureDate().getDay() && currentDate.getMonth() >= container.getDepartureDate().getMonth() && currentDate.getYear() >= container.getDepartureDate().getYear()){
                if (currentDate.getDay() == container.getDepartureDate().getDay() && currentDate.getMonth() == container.getDepartureDate().getMonth() && currentDate.getYear() == container.getDepartureDate().getYear()){
                    long diff = currentDate.getTime() - container.getDepartureDate().getTime();
                    long diffMinutes = diff / (60 * 1000) % 60;
                    long diffHours = diff / (60 * 60 * 1000) % 24;
                    diff = Math.abs(diffHours + diffMinutes/60);
                    if (diff > 2.5){priority.add(container);}
                }
                else priority.add(container); //container had al bij extern vehicle moeten zijn
            }
            
           
        }
        
    }
    ///[0,0,0] add grid container position
    @Override
    public void load(Container container) throws VehicleOverflowException,CargoOutOfBoundsException
    {
        //set isLoaded true when it wasn't already done
        if (this.cargo.isEmpty()){this.isLoaded = true;} 
        
        Vector3f coordinates = container.getArrivalPosition();
        
   
        if (grid!=null)
        {
            try
            {
            if (coordinates.x  > this.nrContainersWidth || coordinates.z > this.nrContainersHeight || (int) coordinates.x < 0 ||(int) coordinates.y < 0 || (int) coordinates.z < 0 || coordinates.y > this.nrContainersDepth )
            {
            
                throw new CargoOutOfBoundsException("CargoOutOfBoundsException");
            
            }
                
            if (grid[(int)coordinates.x][(int)coordinates.y][(int)coordinates.z]!=null){throw new VehicleOverflowException("VehicleOverflowException");}//dubbel
        
                grid[(int)coordinates.x][(int)coordinates.y][(int)coordinates.z] = container; 

                super.load(container);
                
                CommandHandler.addCommand(new Command("loadVehicle", this));
                
            }
            
            catch(Exception e)
            {
                throw e;
            }
        }
    }
    
    public Container unload() throws CargoOutOfBoundsException
    {
        try
        {
            if (cargo.isEmpty()) {throw new ContainerNotFoundException("No containers");}
            Container container = cargo.get(0);
            cargo.remove(container);
            return container;
        }
        
        catch(Exception e)
        {
            //throw e;
            return null;
        }
        
    }
    
   
    
    public Date getArrivalDate(){return arrivalDate;}
    public Float getArrivalTime(){return arrivalTime;}
    public void leave(){this.status = Status.MOVING;}
    public void docking(){
        //this.status = Status.DOCKING;
    }
    public void enter()
    {
        this.status = Status.MOVING;
        HashMap<String, Object> map = new HashMap<>();
        map.put("vehicleType", this.getVehicleType());
        map.put("cargo)", this.getGrid());
                
        //type//cargo lijst
        
        CommandHandler.addCommand(new Command("enterVehicle",map));
    }
    
    public Container[][][] getGrid(){ return this.grid; }
    
    

    //public void leave(ExternVehicle vehicle){}
}
