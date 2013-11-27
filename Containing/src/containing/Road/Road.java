package containing.Road;

import containing.Platform.Platform;
import containing.Vector3f;
import containing.Vehicle.AGV;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Road 
{
    //testvariabelen, simulatie is bepalend
    Vector3f hoekpunt1 = new Vector3f(0,0,0); 
    Vector3f hoekpunt2 = new Vector3f(0,0,4);
    Vector3f hoekpunt3 = new Vector3f(4,0,0);
    Vector3f hoekpunt4 = new Vector3f(4,4,4);
    
    List<Vector3f> mainroad = new ArrayList<Vector3f>(); 
        
    public Road(Vector3f entryPointTruckPlatform, Vector3f entryPointTrainPlatform, Vector3f entryPointSeashipPlatform, Vector3f entryPointBargePlatform, Vector3f entryPointStoragePlatform ){
        mainroad.add(hoekpunt1);
        mainroad.add(hoekpunt2);
        mainroad.add(hoekpunt3);
        mainroad.add(hoekpunt4);
        mainroad.add(entryPointTrainPlatform);
        mainroad.add(entryPointTruckPlatform);
        mainroad.add(entryPointSeashipPlatform);
        mainroad.add(entryPointStoragePlatform);
        mainroad.add(entryPointBargePlatform);
        
        Collections.sort(mainroad);
        
    } 
    
     public float getPathLength(List<Vector3f> weg){
        if (weg.size() > 1){
            if (weg.get(0).x != weg.get(1).x){return Math.abs(weg.get(1).x - weg.get(0).x) + getPathLength(weg.subList(1, weg.size()));}
            return Math.abs(weg.get(1).z - weg.get(0).z) + getPathLength(weg.subList(1, weg.size()));
        }
        else return 0;
    }
    
    /*
     * Calculate distance from current platform of agv to desination platform.
     * There are two possible roads for agv, the function selects the road with minimum distance (=shortest).
     * The shortest way is converted into a Route.
     */
    public Route getShortestPath(AGV agv, Vector3f destinationPlatformEntryPoint){ 
        int indexSource = mainroad.indexOf(agv.getCurrentPlatform().entryPoint());
        int indexDestination =  mainroad.indexOf(destinationPlatformEntryPoint);
        List<Vector3f> weg1 = mainroad.subList(indexSource, mainroad.size());
        List<Vector3f> weg2 = mainroad.subList(0, indexSource);
        List<Vector3f> weg = new ArrayList<Vector3f>(weg1);
        weg.addAll(weg2);
        try{
        weg = weg.subList(0, indexDestination+1);}
        catch(Exception e){
            //destination is laatste waypoint
        }
        float length_weg1 = getPathLength(weg);
        List<Vector3f> weg_notreversed = new ArrayList<Vector3f>(weg);
        Collections.reverse(weg.subList(1, weg.size()));
        float length_weg2 = getPathLength(weg);
        if (length_weg1 < length_weg2){return new Route(weg_notreversed, length_weg1);}
        else return new Route(weg, length_weg2);
    }
    

    
     
}
