package containing.Road;

import containing.ParkingSpot.ParkingSpot;
import containing.Platform.Platform;
import containing.Vector3f;
import containing.Vehicle.AGV;
import containing.Vehicle.Vehicle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Road 
{
    float roadWidth = AGV.width + AGV.width * 0.25f;
    //testvariabelen, simulatie is bepalend 
    Vector3f vertex_outsidetrack_leftbottom = new Vector3f(0,0,0); 
    Vector3f vertex_outsidetrack_rightbottom = new Vector3f(0,0,100);
    Vector3f vertex_outsidetrack_lefttop = new Vector3f(100,0,0);
    Vector3f vertex_outsidetrack_righttop = new Vector3f(100,0,100);
    
    Vector3f vertex_insidetrack_leftbottom = new Vector3f(vertex_outsidetrack_leftbottom.x + roadWidth,0,vertex_outsidetrack_leftbottom.z + roadWidth); 
    Vector3f vertex_insidetrack_rightbottom = new Vector3f(vertex_outsidetrack_rightbottom.x - roadWidth,0,vertex_outsidetrack_rightbottom.z + roadWidth);
    Vector3f vertex_insidetrack_lefttop = new Vector3f(vertex_outsidetrack_lefttop.x + roadWidth,0,vertex_outsidetrack_lefttop.z - roadWidth);
    Vector3f vertex_insidetrack_righttop = new Vector3f(vertex_outsidetrack_righttop.x - roadWidth,0,vertex_outsidetrack_righttop.z - roadWidth);
    
    List<Vector3f> outsidetrack = 
            Arrays.asList(
            vertex_outsidetrack_leftbottom, 
            vertex_outsidetrack_rightbottom, 
            vertex_outsidetrack_lefttop, 
            vertex_outsidetrack_righttop
            );
    
    List<Vector3f> insidetrack = 
            Arrays.asList(
            vertex_insidetrack_leftbottom, 
            vertex_insidetrack_rightbottom, 
            vertex_insidetrack_lefttop, 
            vertex_insidetrack_righttop
            );
        
    public Road(List<Vector3f> entryPoints){
        for (Vector3f v : entryPoints){this.createCorrespondingWaypoint(v);}
        

        
    } 
    
    
    
    private void createCorrespondingWaypoint(Vector3f entryPoint){
        //left of mainroad (trainplatform)
        if (entryPoint.x < this.vertex_insidetrack_leftbottom.x){ //links van weg
            outsidetrack.add(new Vector3f(entryPoint.x+roadWidth/2,0,entryPoint.z));//niet zeker van roadWidth
            insidetrack.add(new Vector3f(entryPoint.x+roadWidth*1.5f,0,entryPoint.z));
            return;
        }
        
        //right of mainroad (barge,truck platform)
        if (entryPoint.x > this.vertex_outsidetrack_righttop.x){
            outsidetrack.add(new Vector3f(entryPoint.x-roadWidth/2,0,entryPoint.z));
            insidetrack.add(new Vector3f(entryPoint.x-roadWidth*1.5f,0,entryPoint.z));
            return;
        }
        
        if (entryPoint.z > this.vertex_insidetrack_lefttop.z){
            outsidetrack.add(new Vector3f(entryPoint.z-roadWidth/2,0,entryPoint.z));
            insidetrack.add(new Vector3f(entryPoint.z-roadWidth*1.5f,0,entryPoint.z));
            return;
        }
        
        if (entryPoint.z < this.vertex_insidetrack_leftbottom.z){
            outsidetrack.add(new Vector3f(entryPoint.z+roadWidth/2,0,entryPoint.z));
            insidetrack.add(new Vector3f(entryPoint.z+roadWidth*1.5f,0,entryPoint.z));
            return;
        }
        
        
        //left side storage platform
        if (Math.abs(entryPoint.x - this.vertex_insidetrack_lefttop.x) < Math.abs(entryPoint.x - this.vertex_insidetrack_righttop.x)){
            outsidetrack.add(new Vector3f(entryPoint.x-roadWidth*1.5f,0,entryPoint.x));
            insidetrack.add(new Vector3f(entryPoint.x-roadWidth/2,0,entryPoint.x));
            return;
        }
        
        //else right side
        outsidetrack.add(new Vector3f(entryPoint.x+roadWidth*1.5f,0,entryPoint.x));
        insidetrack.add(new Vector3f(entryPoint.x+roadWidth/2,0,entryPoint.x));

    }
    
     public float getPathLength(List<Vector3f> weg){
        if (weg.size() > 1){
            if (weg.get(0).x != weg.get(1).x){return Math.abs(weg.get(1).x - weg.get(0).x) + getPathLength(weg.subList(1, weg.size()));}
            return Math.abs(weg.get(1).z - weg.get(0).z) + getPathLength(weg.subList(1, weg.size()));
        }
        else return 0;
    }
    
  
    public Route getPath(Vehicle vehicle, Platform destination){ 
        Route shortestPath = calculateShortestPath(vehicle, destination.entryPoint());
        shortestPath.setDestinationPlatform(destination);
        shortestPath.setDestinationParkingSpot(null);
        return shortestPath;
    }
    
    public Route getPath(Vehicle vehicle, ParkingSpot parkingSpot){ 
        Route shortestPath = calculateShortestPath(vehicle, new Vector3f(0,0,0)); //parkingSpot moet position hebben
        shortestPath.setDestinationPlatform(vehicle.getCurrentPlatform());
        shortestPath.setDestinationParkingSpot(parkingSpot);
        return shortestPath;
    }
    
        /*
     * Calculates distance from current platform of agv to desination platform.
     * There are two possible roads for agv, the function selects the road with minimum distance (=shortest).
     * The shortest way is converted into a Route.
     */
    private Route calculateShortestPath(Vehicle vehicle, Vector3f destination){
        this.createCorrespondingWaypoint(vehicle.getCurrentPlatform().exitPoint());

        List<Vector3f> outsidetrack = new ArrayList<Vector3f>(this.outsidetrack);
        List<Vector3f> insidetrack = new ArrayList<Vector3f>(this.insidetrack);
   
        Collections.sort(outsidetrack);
        Collections.sort(insidetrack);
        Collections.reverse(insidetrack);
        
        insidetrack = this.setPathCorrectOrder(insidetrack, vehicle.getCurrentPlatform().exitPoint(), destination);
        outsidetrack = this.setPathCorrectOrder(outsidetrack, vehicle.getCurrentPlatform().exitPoint(), destination);
        
        float length_insidetrack = getPathLength(insidetrack);
        float length_outsidetrack = getPathLength(outsidetrack);
        
        if (length_insidetrack < length_outsidetrack){return new Route(insidetrack, length_insidetrack);}
        else return new Route(outsidetrack, length_outsidetrack);
    }
    
    private List<Vector3f> setPathCorrectOrder(List<Vector3f> path, Vector3f source, Vector3f destination){
        
        
        int indexSource = path.indexOf(source);
        int indexDestination =  path.indexOf(destination);
        List<Vector3f> weg1 = path.subList(indexSource, outsidetrack.size());
        List<Vector3f> weg2 = path.subList(0, indexSource);
        List<Vector3f> weg = new ArrayList<Vector3f>(weg1);
        weg.addAll(weg2);
        try{
        weg = weg.subList(0, indexDestination+1);}
        catch(Exception e){
            //destination is laatste waypoint
        }
        return weg;
    }
    

    
     
}
