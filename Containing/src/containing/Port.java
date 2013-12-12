package containing;

import containing.Platform.BargePlatform;
import containing.Platform.Platform;
import containing.Platform.SeashipPlatform;
import containing.Platform.StoragePlatform;
import containing.Platform.TrainPlatform;
import containing.Platform.TruckPlatform;
import containing.Road.Road;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Port implements Serializable
{
    private int ID = 0;
    private List<Platform> Platforms;
    private Road Mainroad;

    public Road getMainroad() {
        return Mainroad;
    }
    //private List<AGV> aGVs;
    private StoragePlatform storagePlatform;
    

    public Port() 
    {
        this.Platforms = new ArrayList<>();
        Settings.messageLog.AddMessage("Created Harbor Object with id: " + ID);

        Platforms.add(new BargePlatform(new Vector3f(3, 0, 0)));
        Platforms.add(new SeashipPlatform(new Vector3f(0, 0, 0)));
        Platforms.add(new TrainPlatform(new Vector3f(0, 5.5f, 0)));
        Platforms.add(new TruckPlatform(new Vector3f(0, 0, 0)));
        
        List<Vector3f> waypointList = new ArrayList<>();
        waypointList.add(new Vector3f(103f*Settings.METER,5.5f,0));
        waypointList.add(new Vector3f(103f*Settings.METER,5.5f, 1562f*Settings.METER));
        waypointList.add(new Vector3f(717f*Settings.METER,5.5f, 1562f*Settings.METER));
        waypointList.add(new Vector3f(717f*Settings.METER, 5.5f, 0));
        Mainroad = new Road(waypointList);
        
        storagePlatform = new StoragePlatform(new Vector3f(110f*Settings.METER , 5.5f, 19f*Settings.METER));
        //aGVs = storagePlatform.getAllCreatedAgvs();
        Platforms.add(storagePlatform);
        
        
    }

    public void update() 
    {
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
