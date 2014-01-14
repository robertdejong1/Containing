package containing;

import containing.ClientHandler.ClientType;
import containing.Platform.BargePlatform;
import containing.Platform.Platform;
import containing.Platform.SeashipPlatform;
import containing.Platform.StoragePlatform;
import containing.Platform.StorageStrip;
import containing.Platform.TrainPlatform;
import containing.Platform.TruckPlatform;
import containing.Road.Road;
import containing.Vehicle.AGV;
import containing.Vehicle.Crane;
import containing.Vehicle.ExternVehicle;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Port implements Serializable
{
    private int ID = 0;
    private List<Platform> Platforms;
    private Road Mainroad;
    private StoragePlatform storagePlatform;

    private int lastUpdate = 0;
    private HashMap<String,Double> lastStats = null;
    
    public List<Vector3f> waypointList;
    
    public Port() 
    {
        this.Platforms = new ArrayList<>();
        Settings.messageLog.AddMessage("Created Harbor Object with id: " + ID);

        Platforms.add(new BargePlatform(new Vector3f(71.7f, 5.5f, 78.0f)));
        Platforms.add(new SeashipPlatform(new Vector3f(0.0f, 5.5f, 156.0f)));
        Platforms.add(new TrainPlatform(new Vector3f(0, 5.5f, 0)));
        Platforms.add(new TruckPlatform(new Vector3f(71.7f, 5.5f, 0)));
        
        waypointList = new ArrayList<>();
        waypointList.add(new Vector3f(103f*Settings.METER,5.5f, 0.5f));
        waypointList.add(new Vector3f(103f*Settings.METER,5.5f, 1562f*Settings.METER));
        waypointList.add(new Vector3f(717f*Settings.METER,5.5f, 1562f*Settings.METER));
        waypointList.add(new Vector3f(717f*Settings.METER, 5.5f, 0.5f));
        Mainroad = new Road(waypointList);
        
        storagePlatform = new StoragePlatform(new Vector3f(110f*Settings.METER , 5.5f, 19f*Settings.METER));
        //aGVs = storagePlatform.getAllCreatedAgvs();
        Platforms.add(storagePlatform);
    }
    
    public Road getMainroad() {
        return Mainroad;
    }

    public HashMap<String, Double> getStats(){
        HashMap<String, Double> stats = new HashMap<>();
        for(Platform p : Platforms){
            
            double count = 0;
            
            List<ExternVehicle> evs = p.getEvs();
            for(ExternVehicle ev : evs){
                count += ev.getCargo().size();
            }
            List<Crane> cranes = p.getCranes();
            for(Crane crane : cranes){
               count += crane.getCargo().size();
            }
            
            if(p instanceof BargePlatform){
                stats.put("barge", count);
            }
            else if(p instanceof SeashipPlatform){
                stats.put("seaship", count);
            }
            else if(p instanceof TrainPlatform){
                stats.put("train", count);
            }
            else if(p instanceof TruckPlatform){
                stats.put("truck", count);
            }
        }

         double agvCount = 0;
         for(AGV agv : storagePlatform.getAgvs()){
             agvCount += agv.getCargo().size();
         }
        stats.put("agv", agvCount);
        
        double stripCount = 0;
        for(StorageStrip strip : storagePlatform.getStrips()){
            stripCount += strip.getContainers().size();
        }
        
        stats.put("storage", stripCount);
        return stats;
    }
    
    public void update() 
    {
        lastUpdate++;
        HashMap<String,Double> stats = getStats();
        if(lastUpdate == 30 && stats != lastStats){
            CommandHandler.addCommand(new Command("stats", stats, ClientType.APP));
            CommandHandler.addCommand(new Command("stats", stats, ClientType.WEB));
            lastUpdate = 0;
            lastStats = stats;
        }
        for (Platform P : Platforms) 
        {
            P.update();
        }
    }

    public int getID() 
    {
        return ID;
    }

    public StoragePlatform getStoragePlatform() 
    {
        return storagePlatform;
    }

    public List<Platform> getPlatforms() 
    {
        return Platforms;
    }

    @Override
    public String toString() {
        return "Port{" + "ID=" + ID + ", Platforms=" + Platforms + ", storagePlatform=" + storagePlatform + '}';
    }
}
