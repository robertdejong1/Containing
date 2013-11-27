/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package containing.Road;

import containing.Vector3f;
import containing.Vehicle.Vehicle;
import java.util.List;

/**
 *
 * @author Robert
 */
public class Route {
    private List<Vector3f> weg;
    float distance;
    public Route(List<Vector3f> weg, float distanceInMeters){
        this.weg = weg;
        this.distance = distanceInMeters;
    }
    
    public int CalculateTime(Vehicle vehicle){
        float drived_distance = 0;
        int count_sec = 0;
        while (drived_distance < distance){
            drived_distance =  drived_distance + vehicle.getCurrentSpeed()*1000/3600;
            count_sec++;
        }
        return count_sec;
    }
    
    
}
