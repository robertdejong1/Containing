/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package containing.Vehicle;

import containing.Container;
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
  
    Container[][][] grid;
    private List<Container> priorityCargo = new ArrayList<Container>();
    
    public ExternVehicle(int capicity, Date arrivalDate, float arrivalTime, Container[][][] grid){
        super(capicity);
        this.arrivalDate = arrivalDate;
        this.arrivalTime = arrivalTime;
        status = Status.WAITING; 
        this.grid = grid;
    }
    
    public void getContainerWithHighestPriority(){
        /*
        for (Container container : this.cargo){
            if (container.getArrivalDate() == new Date().)
        }*/
        
    }
    ///[0,0,0] add grid container position
    public void load(Container container){
        //check out of capicity, check dubbel
        
        if (this.cargo.isEmpty()){this.isLoaded = true;} 
        Vector3f coordinates = container.getArrivalPosition();
        if (grid!=null){
        if (grid[(int)coordinates.x][(int)coordinates.y][(int)coordinates.z]!=null){}//dubbel
        if(grid[(int)coordinates.x][(int)coordinates.y][(int)coordinates.z]==null){
        grid[(int)coordinates.x][(int)coordinates.y][(int)coordinates.z] = container;
        }
        super.load(container);
        }
    }
    
    public Container unload(){
        if (cargo.isEmpty()) return null;
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
