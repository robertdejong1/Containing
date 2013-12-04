package containing.Road;

import containing.Platform.Platform;
import containing.Vector3f;
import containing.Vehicle.AGV;
import containing.Vehicle.Vehicle;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Road implements Serializable 
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
    
    //List<Vector3f> outsidetrack = new ArrayList<>();
    
    //List<Vector3f> insidetrack = new ArrayList<>();
    
    //List<Vector3f> singletrack = new ArrayList<>();
    
    List<Vector3f> track = new ArrayList<>();
        
    public Road(List<Vector3f> roadPoints){ //vaste punten voor main road = 4, voor platform road = 2
        for (Vector3f v : roadPoints){
            track.add(v);
        }
        Collections.sort(track); //links onder links boven rechts boven rechtsonder
        //for (Vector3f v : entryPoints){this.createCorrespondingWaypoint(v);}
    } 
    

    private Vector3f createCorrespondingWaypoint(Vector3f point){
        if (track.size() == 4){
            if (point.x < track.get(0).x){ return new Vector3f(track.get(0).x, point.y, point.z);}
            if (point.x > track.get(2).x) {return new Vector3f(track.get(2).x, point.y, point.z); }
            if (point.z < track.get(1).z){return new Vector3f(point.x, point.y, track.get(1).z);}
            return new Vector3f(point.x,point.y, track.get(0).z);
        }
        return new Vector3f(track.get(0).x, point.y, point.z);
    }
    
     public static float getPathLength(List<Vector3f> weg){
         
        if (weg.size() > 1){
            if (weg.get(0).x != weg.get(1).x){ return Math.abs(weg.get(1).x - weg.get(0).x) + getPathLength(weg.subList(1, weg.size()));}
            return Math.abs(weg.get(1).z - weg.get(0).z) + getPathLength(weg.subList(1, weg.size()));
        }
        else return 0;
    }
    
  
    public Route getPath(Vehicle vehicle, Platform destination){ 
        Route shortestPath = calculateShortestPath(vehicle, this.createCorrespondingWaypoint(destination.getExitpoint()));
        shortestPath.setDestinationPlatform(destination);
        shortestPath.setDestinationParkingSpot(null);
        return shortestPath;
    }
    
    public Route getPath(Vehicle vehicle, Vector3f position){
        List<Vector3f> track = new ArrayList<Vector3f>(this.track);
        track.add(this.createCorrespondingWaypoint(vehicle.getPosition()));
        track.add(this.createCorrespondingWaypoint(position));
        Collections.sort(track);
        track = this.setPathCorrectOrder(track, createCorrespondingWaypoint(vehicle.getPosition()), this.createCorrespondingWaypoint(position));
        float length = this.getPathLength(track);
        return (new Route(track,length));
      
    }
    
     /*
     * Calculates distance from current platform of agv to desination platform.
     * There are two possible roads for agv, the function selects the road with minimum distance (=shortest).
     * The shortest way is converted into a Route.
     */
    public Route calculateShortestPath(Vehicle vehicle, Vector3f destination){ //only for mainroad
        List<Vector3f> outsidetrack = new ArrayList<Vector3f>(this.track);

        outsidetrack.add(destination);
        outsidetrack.add(this.createCorrespondingWaypoint(vehicle.getCurrentPlatform().getExitpoint()));
        Collections.sort(outsidetrack);
        List<Vector3f> insidetrack = new ArrayList<>(outsidetrack);
        Collections.reverse(outsidetrack);
        
        insidetrack = this.setPathCorrectOrder(insidetrack, this.createCorrespondingWaypoint(vehicle.getCurrentPlatform().getExitpoint()), this.createCorrespondingWaypoint(destination));
        outsidetrack = this.setPathCorrectOrder(outsidetrack, this.createCorrespondingWaypoint(vehicle.getCurrentPlatform().getExitpoint()), this.createCorrespondingWaypoint(destination));
        
        float length_insidetrack = getPathLength(insidetrack);
        float length_outsidetrack = getPathLength(outsidetrack);
        
        if (length_insidetrack < length_outsidetrack){return new Route(insidetrack, length_insidetrack);}
        else return new Route(outsidetrack, length_outsidetrack);
    }
    
    public static List<Vector3f> setPathCorrectOrder(List<Vector3f> path, Vector3f source, Vector3f destination){
        int indexSource = path.indexOf(source);
        int indexDestination =  path.indexOf(destination);
        List<Vector3f> weg1 = path.subList(indexSource, path.size());
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
