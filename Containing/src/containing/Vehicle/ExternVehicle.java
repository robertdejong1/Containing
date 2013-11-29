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
import containing.Exceptions.VehicleOverflowException;
import containing.Platform.Platform;
import containing.Vector3f;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Miriam
 */
public abstract class ExternVehicle extends Vehicle {
    
    private Date arrivalDate;
    private float arrivalTime;
    private String company;
    Container[][][] grid;
    private List<Container> priorityCargo = new ArrayList<Container>();
   
    public ExternVehicle(int capicity, Date arrivalDate, float arrivalTime, Container[][][] grid, Platform platform, String company, Type type){
        super(capicity, platform, type);
        this.arrivalDate = arrivalDate;
        this.arrivalTime = arrivalTime;
        status = Status.WAITING; 
        this.grid = grid;
        this.company = company;
    }
    
    public String getCompanyName(){return this.company;}
    
    public void getContainerWithHighestPriority(){
        List<Container> priority = new ArrayList<>();
        for (Container container : this.cargo){
            
            //if (container.getArrivalDate() == new Date().)
        }
        
    }
    ///[0,0,0] add grid container position
    public void load(Container container) throws VehicleOverflowException,CargoOutOfBoundsException{
        //check out of capicity, check dubbel
        
        if (this.cargo.isEmpty()){this.isLoaded = true;} 
        Vector3f coordinates = container.getArrivalPosition();
        if (grid!=null){
        if (grid[(int)coordinates.x][(int)coordinates.y][(int)coordinates.z]!=null){}//dubbel
        try{
        if(grid[(int)coordinates.x][(int)coordinates.y][(int)coordinates.z]==null){
        grid[(int)coordinates.x][(int)coordinates.y][(int)coordinates.z] = container;
        }
        super.load(container);
        CommandHandler.addCommand(new Command("loadVehicle", this));
        }
        catch(Exception e){throw e;}
        }
    }
    
    public Container unload() throws CargoOutOfBoundsException{
        if (cargo.isEmpty()) {throw new CargoOutOfBoundsException(String.format("CargoOutOfBounds"));}
        Container container = cargo.get(0);
        cargo.remove(container);
        return container;
    }
    
   
    
    public Date getArrivalDate(){return arrivalDate;}
    public Float getArrivalTime(){return arrivalTime;}
    public void leave(){this.status = Status.MOVEING;}
    public void docking(){
        //this.status = Status.DOCKING;
    }
    public void enter(){
        this.status = Status.MOVEING;
    }
    
    

    //public void leave(ExternVehicle vehicle){}
}
