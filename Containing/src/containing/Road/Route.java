/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package containing.Road;

import containing.ErrorLog;
import containing.ParkingSpot.ParkingSpot;
import containing.Platform.Platform;
import containing.Settings;
import containing.Vector3f;
import containing.Vehicle.Vehicle;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Robert
 */
public class Route implements Serializable {
    private List<Vector3f> weg = new ArrayList<Vector3f>();
    float distance;
    Platform destinationPlatform;
    ParkingSpot destinationParkingSpot;
    
    public Route(List<Vector3f> weg, float distanceInMeters){
        
        for (Vector3f v : weg){
            this.weg.add(v);
        }
        this.distance = distanceInMeters;
        System.out.println("------------");
        for (Vector3f v : weg){
            System.out.println("V: " + v);
        }
        
        System.out.println("Distance way: " + distance);
        System.out.println("------------");
    }
    
    public void setDestinationPlatform(Platform destination){this.destinationPlatform = destination;}
    
    public void setDestinationParkingSpot(ParkingSpot destination){this.destinationParkingSpot = destination;}
    
    public void follow(Vehicle vehicle){
        //if destinationParkingSpot == null ga vanaf exitpoint naar midden weg en start volg route
        
        distance = distance - (float)((float)vehicle.getCurrentSpeed()*1000f/3600f)/100f;
        
        System.out.println("Distance to drive: " + distance);
        if (distance <= 0){
            
            Settings.messageLog.AddMessage("Reached end of path");
            vehicle.setPosition(weg.get(weg.lastIndexOf(weg)));
            vehicle.stopDriving();
            if (destinationPlatform == null && destinationParkingSpot == null){}
            else if (destinationParkingSpot == null){
                //motionpath from waypoint to platform
                //platform sign in
                vehicle.setCurrentPlatform(destinationPlatform);
                
            } //meldt aan voor platform nieuwe route
            else {
                
                //motionpath from waypoint to parkingspot
                if (destinationPlatform == null)
                {
                    try
                    {
                        this.destinationParkingSpot.ParkVehicle(vehicle);
                        vehicle.setPosition(this.destinationParkingSpot.getPosition());
                        Settings.messageLog.AddMessage("Park vehicle");
                    }
                    catch(Exception e)
                    {
                        
                        ErrorLog.logMsg(e.getMessage());
                    }
                    
                }
                
                //hier kraan geparkeerd ... ga unload
             //park vehicle

        }
        
    }
    }

    public List<Vector3f> getWeg() {
        return weg;
    }

    public float getDistance() {
        return distance;
    }

    public Platform getDestinationPlatform() {
        return destinationPlatform;
    }

    public ParkingSpot getDestinationParkingSpot() {
        return destinationParkingSpot;
    }
    
   
    
    
    
}
