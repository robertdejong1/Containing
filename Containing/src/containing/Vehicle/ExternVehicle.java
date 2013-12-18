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
import containing.Settings;
import containing.Vector3f;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Miriam
 */
public abstract class ExternVehicle extends Vehicle {
    //x=width y=height z=depth
    private Date arrivalDate;
    private float arrivalTime;
    private String company;
    
    protected Container[][][] grid;
    private List<Integer> priorityColumns = new ArrayList<>();
    private List<Container> priorityCargo = new ArrayList<Container>();
    private HashMap<Integer, List<Integer>> unloadOrderY = new HashMap();
    private List<Boolean> unloadedColumn = new ArrayList<>();
    private int nrContainersDepth;
    private int nrContainersHeight;
    private int nrContainersWidth;
    
    public ExternVehicle(Date arrivalDate, float arrivalTime, int depthGrid,int widthGrid,int heightGrid, Platform platform, String company, Type type)
    {
        
        super(depthGrid*widthGrid*heightGrid, platform, type);
        this.arrivalDate = arrivalDate;
        this.arrivalTime = arrivalTime;


        this.grid = new Container[widthGrid][heightGrid][depthGrid];
        
        this.company = company;
        this.nrContainersDepth = depthGrid;
        this.nrContainersHeight = heightGrid;
        this.nrContainersWidth = widthGrid;
        
        //only relevant for unloading externvehicles 
        List<Integer> unloadOrderYDefault = new ArrayList<>();
        for (int i = 0; i < depthGrid; i++)
        {
            unloadOrderYDefault.add(i);
        }
        
        for (int i = 0; i < widthGrid; i++)
        {
            unloadOrderY.put(i, unloadOrderYDefault);
            unloadedColumn.add(false);     
        }
        
        this.status = Status.INIT;
        
        
        getContainerWithHighestPriority();
    }

    

    
    private List<Container> getContainerWithHighestPriority()
    {
        
        List<Container> priority = new ArrayList<>();
        
        for (Container container : this.cargo)
        {
            
            Date currentDate = new Date(Settings.CurrentTime.getYear(), Settings.CurrentTime.getMonth(), Settings.CurrentTime.getDate());//van controller current date?? + time
            currentDate.setHours(Settings.CurrentTime.getHours());
            currentDate.setMinutes(Settings.CurrentTime.getMinutes());
            
            Date containerDate = container.getDepartureDate();
            containerDate.setHours((int)container.getDepartureTimeTill());
            containerDate.setMinutes(((int)container.getDepartureTimeTill() % 1) * 100);

            long diff = containerDate.getTime() - currentDate.getTime();
            long diffHours = diff / (60 * 60 * 1000);

            if (diffHours < 5) //5 uur grens?
            {
                priority.add(container);
            }        
           
        }
        
        //evt sorteer priority list
        
        //update unloadOrder met prioriteitskolommen
        for (Container container : priority)
        {
           
            priorityColumns.add(0, (int)container.getArrivalPosition().x);
            
            List<Integer> orderY = unloadOrderY.get((int)container.getArrivalPosition().x);
            orderY.remove((int)container.getArrivalPosition().y);
            orderY.add(0,(int)container.getArrivalPosition().y);
            unloadOrderY.put((int)container.getArrivalPosition().x, orderY);
        }
        
        return priority;
        
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
                if (coordinates.x  > this.nrContainersWidth || coordinates.z > this.nrContainersDepth || (int) coordinates.x < 0 ||(int) coordinates.y < 0 || (int) coordinates.z < 0 || coordinates.y > this.nrContainersHeight )
                {
                    //Settings.messageLog.AddMessage("Cargo: " + container.getArrivalPosition());
                    throw new CargoOutOfBoundsException("CargoOutOfBoundsException");
            
                }
                
                if (grid[(int)coordinates.x][(int)coordinates.y][(int)coordinates.z]!=null)
                {
                    Settings.messageLog.AddMessage("Vehicle: " + this.getID() +  " has already container on position: " + coordinates);
                    throw new VehicleOverflowException("VehicleOverflowException");
                }//dubbel
        
                    grid[(int)coordinates.x][(int)coordinates.y][(int)coordinates.z] = container; 

                    super.load(container);
                    if (this.getStatus() != Status.INIT)
                    {
                        /*
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("id", this.getID());
                        map.put("vehicleType", this.getVehicleType());
                        map.put("container", container);
                    
                        CommandHandler.addCommand(new Command("loadVehicle",map));*/
                    }
                   
                
                //if status not waiting
                //CommandHandler.addCommand(new Command("loadVehicle", this));
                
                }
            
                catch(Exception e)
                {
                    throw e;
                }
            }
        
       //status waiting ..... anders load op vrije plaats
    }
    
    public Container unload() throws ContainerNotFoundException
    {
            try
            {
                if (cargo.isEmpty()) { throw new ContainerNotFoundException("No containers"); }
     
                Container container = cargo.get(0);
            
                cargo.remove(container);
            
                this.getGrid()[(int)container.getArrivalPosition().x][(int)container.getArrivalPosition().y][(int)container.getArrivalPosition().z] = null;
            
             

                 
                return container;
            }
            
            catch(ContainerNotFoundException e)
            {
                throw e;
            }
        
      
        
    }
    
    
    
 
    
     public Container unload(Container container) throws ContainerNotFoundException
    {

        if (cargo.isEmpty()) { throw new ContainerNotFoundException("No containers"); }

        if(cargo.remove(container) == false)
        { 
            throw new ContainerNotFoundException("ContainerNotFound");
        }
        
       this.getGrid()[(int)container.getArrivalPosition().x][(int)container.getArrivalPosition().y][(int)container.getArrivalPosition().z] = null;

        return container;

    }
    
    public String getCompanyName(){ return this.company; }
    
    public Date getArrivalDate(){ return arrivalDate; }
    
    public Float getArrivalTime(){ return arrivalTime; }
    
    public List<Integer> getUnloadOrderY(Integer row){ return this.unloadOrderY.get(row); }
    
    public List<Integer> getPriorityColumns(){ return this.priorityColumns; }
    
    public List<Boolean> getColumns(){ return this.unloadedColumn; }
    
    public Integer getGridWidth(){ return this.nrContainersWidth; }
    
    public void updateColumn(int i, boolean bool) 
    {
       this.unloadedColumn.set(i, bool);
    }
    
    public void leave()
    {
        this.status= Status.MOVING;
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", this.getID());
        map.put("vehicleType", this.getVehicleType());
    
        CommandHandler.addCommand(new Command("leaveExternVehicle",map));
    }
    
    public void docking(){
        //this.status = Status.DOCKING;
    }
    
    public void enter()
    {
        Settings.messageLog.AddMessage("Enter vehicle: " + this.getID());
       
       
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", this.getID());
        map.put("vehicleType", this.getVehicleType());
        map.put("cargo", this.getGrid()); 
        map.put("numberContainers", this.getCargo().size());
        
        
        CommandHandler.addCommand(new Command("enterExternVehicle",map));
        Settings.messageLog.AddMessage("AddCommand enterExternVehicle");
        this.currentPlatform.registerExternVehicle(this);
        Settings.messageLog.AddMessage("Register Vehicle @ Platform");
        
    }
    
    public Container[][][] getGrid(){ return this.grid; }
    
    

    //public void leave(ExternVehicle vehicle){}
}
