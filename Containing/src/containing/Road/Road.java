package containing.Road;

import containing.Platform.Platform;
import containing.Vector3f;
import containing.Vehicle.AGV;
import java.util.ArrayList;
import java.util.List;

public class Road 
{
    Vector3f hoekpunt1 = new Vector3f(0,0,0);
    Vector3f entryPointTrainPlatform = new Vector3f(0,1,0);
    Vector3f entryPointTruckPlatform = new Vector3f(0,2,0);
    Vector3f entryPointSeashipPlatform = new Vector3f(0,3,0);
    Vector3f entryPointStoragePlatform = new Vector3f(0,4,0);
    Vector3f entryPointBargePlatform = new Vector3f(1,4,0);
    Vector3f hoekpunt2 = new Vector3f(2,4,0);
    Vector3f hoekpunt3 = new Vector3f(4,4,0);
    Vector3f hoekpunt4 = new Vector3f(4,0,0);
    
    List<Vector3f> mainroad = new ArrayList<Vector3f>(); //is op volgorde van y,x  
        
    public Road(){
        mainroad.add(hoekpunt1);
        mainroad.add(entryPointTrainPlatform);
        mainroad.add(entryPointTruckPlatform);
        mainroad.add(entryPointSeashipPlatform);
        mainroad.add(entryPointStoragePlatform);
        mainroad.add(entryPointBargePlatform);
        mainroad.add(hoekpunt2);
        mainroad.add(hoekpunt3);
        mainroad.add(hoekpunt4);

    } 
    
    public Route findShortestPath(AGV agv, Platform destination){ 
        /*
         int index = mainroad.indexOf(destination.entryPoint()); //entrypoint
         List<Vector3f> newroad = mainroad.subList(index) + mainroad.subList(0, index);
         
         * */
        return new Route();
    }
    
     
}
