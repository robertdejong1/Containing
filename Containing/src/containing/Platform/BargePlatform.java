package containing.Platform;

import containing.Container;
import containing.Container.TransportType;
import containing.Dimension2f;
import containing.Exceptions.AgvNotAvailable;
import containing.ParkingSpot.BargeSpot;
import containing.Settings;
import containing.Vector3f;
import containing.Vehicle.AGV;
import containing.Vehicle.BargeCrane;
import containing.Vehicle.Crane;
import containing.Vehicle.ExternVehicle;
import containing.Vehicle.Vehicle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * BargePlatform.java
 * The dynamic axis for cranes is: Z
 * @author Minardus
 */
public class BargePlatform extends Platform {
    
    private final float WIDTH          = 100f*Settings.METER;  // ???
    private final float LENGTH         = 821.5f*Settings.METER;  // ???
    private final int MAX_VEHICLES     = 2;
    public final int CRANES           = 8;
    
    private final float AGV_OFFSET     = 0f;
    private final float CRANE_OFFSET   = 81.0f;  // ???
    private final float VEHICLE_OFFSET = 0f;
    
    private ArrayList<Vector3f> agvQueuePositions;
    
    private final int[] sendAwayEvTiming;
    
    public BargePlatform(Vector3f position)
    {
        super(position, Positie.RECHTS);
        setDimension(new Dimension2f(WIDTH, LENGTH));
        setAxis(DynamicAxis.Z);
        setEntrypoint(new Vector3f(81.0f,5.5f, 155.5f));
        setExitpoint(new Vector3f(81.0f, 5.5f, 78.8f));
        setRoad();
        setTransportType(TransportType.Barge);
        setMaxAgvQueue(CRANES);
        createAgvSpots(new Vector3f(CRANE_OFFSET - BargeCrane.length - AGV_OFFSET, 0, 0));
        createExtVehicleSpots();
        createCranes(); 
        createAgvQueuePositions();
        sendAwayEvTiming = new int[MAX_VEHICLES];
        for(int i = 0; i < sendAwayEvTiming.length; i++)
            sendAwayEvTiming[i] = -1;
        
                /* 'initialize' craneAgvs */
        for(int i = 0; i < cranes.size(); i++) {
            craneAgvs.add(null);
        }
        
        List waypoints = new ArrayList();
        waypoints.add(new Vector3f(7.9f, 5.5f, 15.6f));
        waypoints.add(new Vector3f(7.9f, 5.5f, 7.8f));
        setCraneRoad(waypoints);
        
        log("Created BargePlatform object: " + toString());
    }
    
       private void createAgvQueuePositions() 
    {
        agvQueuePositions = new ArrayList<>();
        Vector3f base = new Vector3f(760f*Settings.METER, 5.5f, 1450f*Settings.METER);
        for(int i = 0; i < maxAgvQueue; i++) 
        {
            agvQueuePositions.add(new Vector3f(base.x, base.y, base.z + (AGV.length * Settings.METER) * i + 0.5f));
        }
        Collections.reverse(agvQueuePositions);
    }
    
    @Override
    protected final void createCranes() 
    {
        float space = LENGTH / (float)CRANES;
        float offset = (space / 2f) - ( BargeCrane.width / 2f) + super.getPosition().z;
        for(int i = 0; i < CRANES; i++) 
        {
            Vector3f cranePosition = new Vector3f(CRANE_OFFSET, 5.5f, space*i + offset);
            cranes.add(new BargeCrane(cranePosition, this));
        }
    }

    @Override
    protected final void createExtVehicleSpots() 
    {
        float space = LENGTH / (float)MAX_VEHICLES;
        float offset = (space / 2) - (BargeSpot.length / 2);
        for(int i = 0; i < MAX_VEHICLES; i++)
        {
            Vector3f spotPosition = new Vector3f(WIDTH + VEHICLE_OFFSET + getPosition().x, 4.5f,space*i + offset + getPosition().z);
            extVehicleSpots.add(new BargeSpot(spotPosition));
        }
    }
    
    @Override
    public void unload() 
    {
        super.unload();
        
        if(!extVehicles.isEmpty()) 
        {
            int currentVehicle = 0;
            int cranesPerVehicle = CRANES / extVehicles.size();
            
            Iterator<ExternVehicle> it = extVehicles.iterator();
            while(it.hasNext())
            {
                ExternVehicle ev = it.next();
                
                // if cargo is unloaded, send vehicle away
                if(ev.getCargo().isEmpty() && sendAwayEvTiming[currentVehicle] == -1) {
                    sendAwayEvTiming[currentVehicle] = 120;
                }
                if(sendAwayEvTiming[currentVehicle] != -1)
                {
                    if(sendAwayEvTiming[currentVehicle] > 0)
                    {
                        System.out.println("nog seconden: " + sendAwayEvTiming[currentVehicle]);
                        sendAwayEvTiming[currentVehicle] = sendAwayEvTiming[currentVehicle] - 1;
                    }
                    else
                    {
                        try { 
                            it.remove();
                            sendAwayEvTiming[currentVehicle] = -1;
                            ev.followRoute(road.getPathExternVehicleExit(extVehicleSpots.get(currentVehicle), new Vector3f(ev.getPosition().x, 5.5f, ev.getPosition().z + 1000f)));
                        } catch (AgvNotAvailable ex) {
                            Logger.getLogger(TrainPlatform.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
                
                // send new AGV's
                sendAgvs(ev.getCargo().size(), agvQueuePositions);
                
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
    }
    
    @Override
    public void update()
    {
        super.update();
        time++;
        
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
            if(time >= 10) {
                //System.out.println("unloading...");
                unload();
            }
        }
        
        /* LOAD EXTERNAL VEHICLE */
        if(state.equals(State.LOAD))
        {
            load(this);
        }
        
    }
    
}