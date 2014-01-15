package containing.Platform;

import containing.Container;
import containing.Container.TransportType;
import containing.Dimension2f;
import containing.ParkingSpot.TruckSpot;
import static containing.Platform.Platform.Phase.LOAD;
import static containing.Platform.Platform.Phase.MOVE;
import static containing.Platform.Platform.Phase.SENDTOSTORAGE;
import static containing.Platform.Platform.Phase.UNLOAD;
import containing.Point3D;
import containing.Settings;
import containing.Vector3f;
import containing.Vehicle.AGV;
import containing.Vehicle.Crane;
import containing.Vehicle.ExternVehicle;
import containing.Vehicle.StorageCrane;
import containing.Vehicle.Truck;
import containing.Vehicle.TruckCrane;
import containing.Vehicle.Vehicle;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * TruckPlatform.java
 * The dynamic axis for cranes is: Z
 * @author Minardus
 */
public class TruckPlatform extends Platform {
    
    private final float WIDTH          = 100f*Settings.METER;  // ???
    private final float LENGTH         = 821.5f*Settings.METER;  // ???
    private final int MAX_VEHICLES     = 20;    // ???
    public final int CRANES           = 20;
    
    private final float AGV_OFFSET     = 0f;
    private final float CRANE_OFFSET   = 80f;   // ???
    private final float VEHICLE_OFFSET = 0f;
    private Vehicle.Status[] VehicleStatus = new Vehicle.Status[CRANES];
    
    public TruckPlatform(Vector3f position)
    {
        super(position, Platform.Positie.RECHTS);
        setDimension(new Dimension2f(WIDTH, LENGTH));
        setAxis(Platform.DynamicAxis.Z);
        setEntrypoint(new Vector3f(80.0f, 5.5f, 78.5f));
        setExitpoint(new Vector3f(80.0f, 5.5f, 0));
        setRoad();
        setTransportType(TransportType.Truck);
        setMaxAgvQueue(CRANES);
        createAgvSpots(new Vector3f(CRANE_OFFSET - TruckCrane.length - AGV_OFFSET, 0, 0));
        createExtVehicleSpots();
        createCranes();
        log("Created TruckPlatform object: " + toString());
    }

    @Override
    protected final void createCranes() 
    {
        float space = LENGTH / (float)CRANES;
        float offset = getPosition().z;
       // float offset = (space / 2f) - ( TruckCrane.width / 2f) + getPosition().z;
        for(int i = 0; i < CRANES; i++) 
        {
            Vector3f cranePosition = new Vector3f(CRANE_OFFSET, 5.5f, i * TruckCrane.width + offset + TruckCrane.width / 2f);
            cranes.add(new TruckCrane(cranePosition, this));
        }
    }
    
    @Override
    protected final void createExtVehicleSpots() 
    {
        float space = LENGTH / (float)MAX_VEHICLES;
        float offset = getPosition().z;
        
        for(int i = 0; i < MAX_VEHICLES; i++)
        {
            Vector3f spotPosition = new Vector3f(CRANE_OFFSET, 5.5f, i * TruckCrane.width + offset + TruckCrane.width / 2f);
            extVehicleSpots.add(new TruckSpot(spotPosition));
        }
    }
    
    @Override
    public void update()
    {
        super.update();
        
        /* if platform is free, request next job */
        if(state.equals(State.FREE))
            requestNextJob();
        
        if(jobs.size() > 0)
            state = State.LOAD;
        else if(hasExtVehicle())
            state = State.UNLOAD;
        else
            state = State.FREE;
        
        /* UNLOAD EXTERNAL VEHICLE */
        if(state.equals(State.UNLOAD))
        {
            unload();
        }
        
        /* LOAD EXTERNAL VEHICLE */
        if(state.equals(State.LOAD))
        {
            load(this);
        }
    }
    
    /**
     * Unload function which takes care of unloading a extern vehicle
     */
    @Override
    public void unload() 
    {
        super.unload();
        
        
        try {
            
                for (int i =0; i < extVehicleSpots.size(); i++)
                {
                    if (extVehicleSpots.get(i).getParkedVehicle() != null && VehicleStatus[i] == Vehicle.Status.INIT )
                    {   
                        VehicleStatus[i] = Vehicle.Status.UNLOADING;
                    
                        TruckCrane truckcrane = (TruckCrane)cranes.get(i);
                        Truck truck = (Truck)extVehicleSpots.get(i).getParkedVehicle();
                        AGV agv = (AGV)agvSpots.get(i).getParkedVehicle();
                        
                        // send new AGV's
                        sendAgvs(1, agvQueuePositions);
                        
                        truckcrane.setIsAvailable(true);
                    
                        if (agv != null)
                        {
                            Container cargo = truck.getCargo().get(0);
                            truckcrane.unload(agv);
                        }
                    }
                }
            } 
            catch (Exception ex) 
            {
                Logger.getLogger(StorageStrip.class.getName()).log(Level.SEVERE, null, ex);
            }

        
        /*
        //train
        if(!extVehicles.isEmpty()) 
        {
           
                
                
                
                
                
                // loop through cranes
                int currentCrane = 0;
                for(Crane c : cranes)
                {
                    int allowedCranes = (currentVehicle+1 * cranesPerVehicle - cranesPerVehicle);
                    int rows = ev.getGridWidth();
                    int rowsPerCrane = rows / cranesPerVehicle;
                    // fix isAvailable statuses?
                    if(c.getStatus() != Vehicle.Status.LOADING && c.getStatus() != Vehicle.Status.UNLOADING && c.getStatus() != Vehicle.Status.MOVING)
                            c.setIsAvailable(true);
                    // process phase of cranes
                    if(currentCrane >= allowedCranes && currentCrane < allowedCranes + cranesPerVehicle && currentCrane*rowsPerCrane < ev.getColumns().size()) {
                        int column = getColumn(currentCrane, rowsPerCrane, ev);
                        Container container = getContainer(column, ev);
                        Phase phase = unload_getPhase(currentCrane, column, ev);
                        if(phase != null)
                        {
                            switch(phase)
                            {
                                case MOVE:
                                    //System.out.println("MOVE");
                                    unload_phaseMove(currentCrane, column, ev);
                                    break;
                                case LOAD:
                                    //System.out.println("LOAD");
                                    unload_phaseLoad(currentCrane, container, ev);
                                    break;
                                case UNLOAD:
                                    //System.out.println("UNLOAD");
                                    unload_phaseUnload(currentCrane);
                                    break;
                                case SENDTOSTORAGE:
                                    //System.out.println("SENDTOSTORAGE");
                                    unload_phaseSendToStorage(currentCrane);
                                    break;
                            }
                        }
                    }
                    currentCrane++;
                }
            }
        }
        else 
        {
            state = State.FREE;
        }
        if(agvQueue.isEmpty())
            time = 0;
            * */
    }
    
}