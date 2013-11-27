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
    Vector3f entryPointTrainPlatform = new Vector3f(0,0,3.5f);
    Vector3f entryPointTruckPlatform = new Vector3f(4,0,3.5f);
    Vector3f entryPointSeashipPlatform = new Vector3f(0.5f,0,0);
    Vector3f entryPointStoragePlatformLeft = new Vector3f(0.5f,0,0);
    Vector3f entryPointStoragePlatformRight = new Vector3f(3.5f,0,0);
    Vector3f entryPointBargePlatform = new Vector3f(2,0,2);
    Vector3f hoekpunt2 = new Vector3f(0,0,4);
    Vector3f hoekpunt3 = new Vector3f(4,0,0);
    Vector3f hoekpunt4 = new Vector3f(4,4,4);
    
    List<Vector3f> mainroad = new ArrayList<Vector3f>(); //is op volgorde van y,x  
        
    public Road(){
        mainroad.add(hoekpunt1);
        mainroad.add(entryPointTrainPlatform);
        mainroad.add(entryPointTruckPlatform);
        mainroad.add(entryPointSeashipPlatform);
        mainroad.add(entryPointStoragePlatformLeft);
        mainroad.add(entryPointStoragePlatformRight);
        mainroad.add(entryPointBargePlatform);
        mainroad.add(hoekpunt2);
        mainroad.add(hoekpunt3);
        mainroad.add(hoekpunt4);

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
    public Route getShortestPath(AGV agv, Platform destination){ 
        int index = mainroad.indexOf(agv.getCurrentPlatform().entryPoint());
        List<Vector3f> weg1 = mainroad.subList(0, index);
        List<Vector3f> weg2 = mainroad.subList(index, mainroad.size());
        List<Vector3f> weg = new ArrayList<Vector3f>(weg2);
        weg.addAll(weg1);
        float length_weg1 = getPathLength(weg);
        List<Vector3f> weg_notreversed = new ArrayList<Vector3f>(weg);
        Collections.reverse(weg.subList(1, weg.size()));
        float length_weg2 = getPathLength(weg);
        if (length_weg1 < length_weg2){return new Route(weg_notreversed, length_weg1);}
        else return new Route(weg, length_weg2);
    }
    

    
     
}
