package containing.Road;

import containing.ParkingSpot.ParkingSpot;
import containing.Platform.Platform;
import containing.Settings;
import containing.Vector3f;
import containing.Vehicle.AGV;
import containing.Vehicle.ExternVehicle;
import containing.Vehicle.Vehicle;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
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
    
    public Route getPath()
    {
       new Route(this.track,getPathLength(track) );
    }
    
    
     public static float getPathLength(List<Vector3f> weg){
         
        if (weg.size() > 1){
            if (weg.get(0).x != weg.get(1).x){ return Math.abs(weg.get(1).x - weg.get(0).x) + getPathLength(weg.subList(1, weg.size()));}
            return Math.abs(weg.get(1).z - weg.get(0).z) + getPathLength(weg.subList(1, weg.size()));
        }   
        else return 0;
    }
    
     
    //van parkeerplaats op platform naar einde platform
    public Route getPath(Vehicle vehicle, ParkingSpot source, Vector3f exitwayPlatform)
    {
        List<Vector3f> track = new ArrayList<Vector3f>();
        track.add(vehicle.getPosition());
        track.add(this.createCorrespondingWaypoint(vehicle.getPosition()));
        track.add(this.createCorrespondingWaypoint(exitwayPlatform));
        track.add(exitwayPlatform);
        source.UnparkVehicle(); //moet straks bij followroute
        vehicle.setPosition(exitwayPlatform);
        return new Route(track, getPathLength(track));
    }
    
    //van uitgang platform naar ingang andere platform
     public Route getPath(Vehicle vehicle, Vector3f sourcePlatformExitPoint,Platform destination)
     {
         List<Vector3f> track = this.track;
         track.add(sourcePlatformExitPoint);
         track.add(this.createCorrespondingWaypoint(sourcePlatformExitPoint));
         track.add(this.createCorrespondingWaypoint(destination.getEntrypoint()));
         track.add(destination.getEntrypoint());
         Collections.sort(track);
         track = this.setPathCorrectOrder(track, sourcePlatformExitPoint, destination.getExitpoint());
         vehicle.setCurrentPlatform(destination);
         vehicle.setPosition(destination.getEntrypoint());
         return new Route(track, getPathLength(track));
         
     }
     
     //v an ingang platform naar parkeerplaats
      public Route getPath(Vehicle vehicle, ParkingSpot ps)
      {
          List<Vector3f> track = this.track;
          track.add(vehicle.getPosition());
          track.add(this.createCorrespondingWaypoint(vehicle.getPosition()));
          track.add(this.createCorrespondingWaypoint(this.createCorrespondingWaypoint(ps.getPosition())));
          track.add(ps.getPosition());
          try
          {
          ps.ParkVehicle(vehicle);
          vehicle.setPosition(ps.getPosition());
          }
          catch(Exception e){
              Settings.messageLog.AddMessage(e.getMessage());
          }
          
          return new Route(track, getPathLength(track));
          
          
          
          
          
      }
      
    public Route getPath(ExternVehicle ev, ParkingSpot ps )
    {
        List<Vector3f> track = new ArrayList<Vector3f>();
        track.add(ev.getPosition());
        track.add(ps.getPosition());
        float length = this.getPathLength(track);
        Route route = new Route(track, length);
        route.destinationPlatform = null;
        route.destinationParkingSpot = ps;
        return route;
    }
     
    
     /*
    
    //public Route getPath(Vehicle vehicle, VehicleSpot)
    public Route getPath(Vehicle vehicle, Platform destination, boolean outin){ 
        //\\
        
        
        Route shortestPath = null;
        if (outin)//ingang
        {
            Vector3f entrypoint = this.createCorrespondingWaypoint(destination.getEntrypoint());
            shortestPath = calculateShortestPath(vehicle, entrypoint);
      
        }
        else
        {
            Vector3f exitpoint = this.createCorrespondingWaypoint(destination.getExitpoint());
            shortestPath = calculateShortestPath(vehicle.getPosition(), exitpoint);
        }
        
        shortestPath.setDestinationPlatform(destination);
        shortestPath.setDestinationParkingSpot(null);
        
        
        return shortestPath; 
    }
    
    //70 130 152
    
  
    
    public Route getPath(Vehicle vehicle, ParkingSpot ps){
        Vector3f source = this.createCorrespondingWaypoint(vehicle.getPosition());
        Vector3f destination = this.createCorrespondingWaypoint(ps.getPosition());
        List<Vector3f> track = new ArrayList<Vector3f>(this.track);
        track.add(source);
        track.add(destination);
        Collections.sort(track);
        track = this.setPathCorrectOrder(track, source, destination);
        float length = this.getPathLength(track);
  
        Route route = new Route(track,length);
        route.destinationPlatform = null;
        route.destinationParkingSpot = ps;
        return route;
      
    }
    
     /*
     * Calculates distance from current platform of agv to desination platform.
     * There are two possible roads for agv, the function selects the road with minimum distance (=shortest).
     * The shortest way is converted into a Route.
     */
    
    public Route calculateShortestPath(Vector3f vehicleposition, Vector3f destination){ //only for mainroad
        List<Vector3f> outsidetrack = new ArrayList<Vector3f>(this.track);

        outsidetrack.add(destination);
        Vector3f positionvehicle = vehicleposition;
        outsidetrack.add(positionvehicle);
        Collections.sort(outsidetrack);
        List<Vector3f> insidetrack = new ArrayList<>(outsidetrack);
        Collections.reverse(outsidetrack);
        
        insidetrack = this.setPathCorrectOrder(insidetrack, positionvehicle, destination);
        outsidetrack = this.setPathCorrectOrder(outsidetrack,positionvehicle, destination);
        
        float length_insidetrack = getPathLength(insidetrack);
        float length_outsidetrack = getPathLength(outsidetrack);
        
        if (length_insidetrack < length_outsidetrack){return new Route(insidetrack, length_insidetrack);}
        else return new Route(outsidetrack, length_outsidetrack);
    }
    public Route calculateShortestPath(Vehicle vehicle, Vector3f destination){ //only for mainroad
        List<Vector3f> outsidetrack = new ArrayList<Vector3f>(this.track);

        outsidetrack.add(destination);
        Vector3f positionvehicle = this.createCorrespondingWaypoint(vehicle.getCurrentPlatform().getExitpoint());
        outsidetrack.add(positionvehicle);
        Collections.sort(outsidetrack);
        List<Vector3f> insidetrack = new ArrayList<>(outsidetrack);
        Collections.reverse(outsidetrack);
        
        insidetrack = this.setPathCorrectOrder(insidetrack, positionvehicle, destination);
        outsidetrack = this.setPathCorrectOrder(outsidetrack,positionvehicle, destination);
        
        float length_insidetrack = getPathLength(insidetrack);
        float length_outsidetrack = getPathLength(outsidetrack);
        
        if (length_insidetrack < length_outsidetrack){return new Route(insidetrack, length_insidetrack);}
        else return new Route(outsidetrack, length_outsidetrack);
    }
    
    public List<Vector3f> setPathCorrectOrder(List<Vector3f> path, Vector3f source, Vector3f destination){

        System.out.println("Source: " + source);
        System.out.println("Destination: " + destination);
        int indexSource = path.indexOf(source);
        int indexDestination =  path.indexOf(destination);
        System.out.println("indexSource: " + indexSource);
        System.out.println("pathSize: " + path.size());
     
        
        List<Vector3f> weg1 = new ArrayList(path.subList(indexSource, path.size()));
    
        
       
        List<Vector3f> weg2 = new ArrayList(path.subList(0, indexSource));
        List<Vector3f> weg = new ArrayList<Vector3f>(weg1);
        weg.addAll(weg2);
        try{
        weg = new ArrayList(weg.subList(0, indexDestination+1));}
        catch(Exception e){
            //destination is laatste waypoint
        }
        return weg;
    }
    
    
    

    
     
}
