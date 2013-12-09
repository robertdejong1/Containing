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
import containing.Vehicle.Crane;
import containing.Vehicle.Vehicle;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Robert
 */
public class Route implements Serializable {
    private List<Vector3f> weg;
    float distance;
    Platform destinationPlatform;
    ParkingSpot destinationParkingSpot;
    
    public Route(List<Vector3f> weg, float distanceInMeters){
        
        this.weg = weg;
        this.distance = distanceInMeters;
     
        
    }
    
    public void setDestinationPlatform(Platform destination){this.destinationPlatform = destination;}
    
    public void setDestinationParkingSpot(ParkingSpot destination){this.destinationParkingSpot = destination;}
    
    public void follow(Vehicle vehicle){
        //if destinationParkingSpot == null ga vanaf exitpoint naar midden weg en start volg route
        Settings.messageLog.AddMessage("Vehicle: " + vehicle.getID() + "follows route");
        Settings.messageLog.AddMessage("Current speed: " + vehicle.getCurrentSpeed());
        Settings.messageLog.AddMessage("Distance to drive: " + distance);
        
        distance = distance - (vehicle.getCurrentSpeed()*1000/3600)/100;
        if (distance <= 0){
            Settings.messageLog.AddMessage("Reached end of path");
            Settings.messageLog.AddMessage("DestinationPlatform: " + destinationPlatform);
            Settings.messageLog.AddMessage("DestinationParkingSpot: " + destinationParkingSpot);
            vehicle.stopDriving();
            if (destinationParkingSpot == null){
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
                        Settings.messageLog.AddMessage("Park vehicle");
                    }
                    catch(Exception e)
                    {
                        
                        ErrorLog.logMsg(e.getMessage());
                    }
                    
                }
                
                //hier kraan geparkeerd ... ga unload
            } //park vehicle

        }
        
    }
    

    
}
