package containing.Road;

import containing.ParkingSpot.ParkingSpot;
import containing.Platform.Platform;
import containing.Platform.StoragePlatform;
import containing.Settings;
import containing.Vector3f;
import containing.Vehicle.AGV;
import containing.Vehicle.ExternVehicle;
import containing.Vehicle.Vehicle;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Road implements Serializable 
{
    float roadWidth = AGV.width + AGV.width * 0.25f;
    
    List<Vector3f> track = new ArrayList<>();
        
    public Road(List<Vector3f> roadPoints){ //vaste punten voor main road = 4, voor platform road = 2
        for (Vector3f v : roadPoints){
            track.add(v);
        }
        //Collections.sort(track); //links onder links boven rechts boven rechtsonder
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
       return new Route(this.track,getPathLength(track) );
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
        System.out.println("hahaah hier heeft vehicle boi : " + vehicle.getCurrentPlatform().toString());
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
       
         track = this.setPathCorrectOrder(track, vehicle.getCurrentPlatform(), destination);
         vehicle.setCurrentPlatform(destination);
         vehicle.setPosition(destination.getEntrypoint());
         Route route = new Route(track, getPathLength(track));
         route.destinationParkingSpot = null;
         return route;
         
         
     }
     
     //van ingang platform naar parkeerplaats
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
     * Calculates distance from current platform of agv to desination platform.
     * There are two possible roads for agv, the function selects the road with minimum distance (=shortest).
     * The shortest way is converted into a Route.
     */
    
 /*
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
    }*/
    
    public List<Vector3f> setPathCorrectOrder(List<Vector3f> path, Platform source, Platform destination){
        //path size altijd 4
        List<Vector3f> correctPath = path;
        correctPath.add(path.get(0));
        correctPath.add(path.get(1));
        
        boolean right = true;
        //bepalen aan hand van platform of rechts of links: nu altijd rechtsom
        if (source instanceof StoragePlatform)
        {
            right = false;
        }
        //if (typeOrInterface.isInstance(someObject);)
        
        //rechtsom
        if (true)
        {
            for (Vector3f waypoint : track)   
            {
                System.out.println("source.getExitpoint() " + source.getExitpoint());
              if ((waypoint.x >= source.getExitpoint().x && waypoint.z >= source.getExitpoint().z))
              {
                  
                correctPath.add(waypoint);
              }  
             
            }
            
            for(Vector3f waypoint : track)
            {
              if (((int) waypoint.x >= (int) destination.getEntrypoint().x && waypoint.z >=  destination.getEntrypoint().z))
              {
                 correctPath.add(waypoint);
              }
            }
            
    
        }
        
        //linksom <= 
        
        else
        {
            for (Vector3f waypoint : track)   
            {
              if ((waypoint.x <= source.getExitpoint().x && waypoint.z <= source.getExitpoint().z))
              {
                  
                correctPath.add(waypoint);
              }  
             
            }
            
            for(Vector3f waypoint : track)
            {
              if ((waypoint.x <= destination.getEntrypoint().x && waypoint.z <= destination.getEntrypoint().z))
              {
                 correctPath.add(waypoint);
              }
            }
            
        }
        
        correctPath.add(path.get(2));
        correctPath.add(path.get(3));

        return correctPath;
    }
    
    
    

    
     
}
