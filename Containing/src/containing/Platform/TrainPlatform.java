package containing.Platform;

import containing.Container;
import containing.Container.TransportType;
import containing.Dimension2f;
import containing.Exceptions.AgvNotAvailable;
import containing.Exceptions.CargoOutOfBoundsException;
import containing.Exceptions.ContainerNotFoundException;
import containing.Exceptions.NoFreeAgvException;
import containing.Exceptions.VehicleOverflowException;
import containing.ParkingSpot.AgvSpot;
import containing.ParkingSpot.TrainSpot;
import containing.Road.Road;
import containing.Settings;
import containing.Vector3f;
import containing.Vehicle.AGV;
import containing.Vehicle.Crane;
import containing.Vehicle.ExternVehicle;
import containing.Vehicle.TrainCrane;
import containing.Vehicle.Vehicle.Status;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * TrainPlatform.java
 * The dynamic axis for cranes is: Z
 * @author Minardus
 */
public class TrainPlatform extends Platform {
    
    private enum Phase { MOVE, LOAD, UNLOAD, SENDTOSTORAGE }

    private final float WIDTH          = 103f*Settings.METER;  // ???
    private final float LENGTH         = 1523f*Settings.METER; // ???
    public final int MAX_VEHICLES      = 3;
    public final int CRANES            = 4;
    
    private final float AGV_OFFSET     = 0f;
    private final float CRANE_OFFSET   = 1.5f;   // ???
    private final float VEHICLE_OFFSET = 1.4f;
    
    private List<Vector3f> agvQueuePositions;
    
    private final Road craneRoad;
    
    private boolean unloadOnce = false;
    
    private final int[] sendAwayEvTiming;
    
    public TrainPlatform(Vector3f position)
    {
        super(position, Platform.Positie.LINKS);
        setDimension(new Dimension2f(WIDTH, LENGTH));
        setAxis(DynamicAxis.Z);
        setEntrypoint(new Vector3f(10.3f - (AGV.width*Settings.METER) / 2f, getPosition().y, getPosition().z + 1.5f));
        //setEntrycorner(new Vector3f(3.7f - (AGV.width*Settings.METER) / 2f, getPosition().y, getPosition().z + 1.5f));
        setExitpoint(new Vector3f(10.3f - (AGV.width*Settings.METER) / 2f, getPosition().y, getPosition().z + LENGTH));
        //setExitcorner(new Vector3f(3.7f - (AGV.width*Settings.METER) / 2f, getPosition().y, getPosition().z + LENGTH));
        setRoad();
        setTransportType(TransportType.Train);
        setMaxAgvQueue(CRANES * 3);
        //createAgvSpots(new Vector3f(CRANE_OFFSET + TrainCrane.length + AGV_OFFSET, 0, 0));
        createExtVehicleSpots();
        createCranes();
        createAgvSpots();
        createAgvQueuePositions();
        for(int i = 0; i < cranes.size(); i++) {
            craneAgvs.add(null);
        }
        List waypoints = new ArrayList();
        waypoints.add(new Vector3f(2.6f, 5.5f, 0.5f));
        waypoints.add(new Vector3f(2.6f, 5.5f, 152.3f));
        craneRoad = new Road(waypoints);
        sendAwayEvTiming = new int[MAX_VEHICLES];
        for(int i = 0; i < sendAwayEvTiming.length; i++)
            sendAwayEvTiming[i] = -1;
        log("Created TrainPlatform object: " + toString());
    }
    
    protected final void createAgvSpots() {
        float space = LENGTH / (float)CRANES;
        float offset = (space / 2f) - ( TrainCrane.width*Settings.METER / 2f);
        for(int i = 0; i < CRANES; i++) 
        {
            Vector3f cranePosition = new Vector3f(getPosition().x + CRANE_OFFSET, getPosition().y, getPosition().z + (space*i + offset));
            float x = cranePosition.x;
            float y = cranePosition.y;
            float z = cranePosition.z;
            agvSpots.add(new AgvSpot(new Vector3f(x + TrainCrane.length*Settings.METER, y, z)));
        }
    }

    @Override
    protected final void createCranes() 
    {
        float space = LENGTH / (float)CRANES;
        float offset = (space / 2f) - ( TrainCrane.width*Settings.METER / 2f);
        for(int i = 0; i < CRANES; i++) 
        {
            Vector3f cranePosition = new Vector3f(getPosition().x + CRANE_OFFSET, getPosition().y, getPosition().z + (space*i + offset));
            cranes.add(new TrainCrane(cranePosition, this));
        }
        Collections.reverse(cranes);
    }
    
    @Override
    protected final void createExtVehicleSpots() 
    {
        float space = LENGTH / (float)MAX_VEHICLES;
        //float offset = (space / 2f) - (TrainSpot.length*Settings.METER / 2f);
        for(int i = 0; i < MAX_VEHICLES; i++)
        {
            Vector3f spotPosition = new Vector3f(getPosition().x + VEHICLE_OFFSET, getPosition().y, (getPosition().z + LENGTH) - space*i);
            extVehicleSpots.add(new TrainSpot(spotPosition));
        }
    }
    
    private void createAgvQueuePositions() 
    {
        agvQueuePositions = new ArrayList<>();
        Vector3f base = new Vector3f(3.5f, 5.5f, 2f);
        for(int i = 0; i < maxAgvQueue; i++) 
        {
            agvQueuePositions.add(new Vector3f(base.x, base.y, base.z + (AGV.length * Settings.METER) * i + 0.5f));
        }
        Collections.reverse(agvQueuePositions);
    }
    
    private void sendAgvs(int cargoSize, List<Vector3f> positions) {
        if(agvQueue.isEmpty() || agvQueue.size() < maxAgvQueue) {
            for(int i = agvQueue.size(); i < (cargoSize < maxAgvQueue ? cargoSize : maxAgvQueue); i++) {
                if(time >= 30) {
                    try {
                        AgvSpot agvSpot = Settings.port.getStoragePlatform().requestFreeAgv(getTransportType(), agvQueue);
                        AGV agv = (AGV)agvSpot.getParkedVehicle();
                        try {
                            agv.followRoute(road.getPathAllInVector(agv, agvSpot, positions.get(i), this, Settings.port.getMainroad()));
                        } catch (AgvNotAvailable ex) {
                            Logger.getLogger(TrainPlatform.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        addAgvToQueue(agv);
                    } catch(NoFreeAgvException e) {
                        System.out.println("No Free AGV available ;(");
                    }
                    time = 0;
                    break;
                }
            }
        }
    }
    
    private int getColumn(int currentCrane, int rowsPerCrane, ExternVehicle ev) 
    {
        List<Integer> pColumns = ev.getPriorityColumns();
        List<Boolean> columns = ev.getColumns();
        int startIndex = currentCrane * rowsPerCrane;
        for(int i = startIndex; i < startIndex + rowsPerCrane; i++)
        {
            if(pColumns.contains(i) && !columns.get(i))
                return i;
            else if(!columns.get(i))
                return i;
        }
        return -1;
    }
    
    private Container getContainer(int column, ExternVehicle ev) {
        List<Integer> unloadOrder = ev.getUnloadOrderY(column);
        if(unloadOrder != null)
        {
            //System.out.println("unloadOrder == " +  unloadOrder.size());
            Collections.reverse(unloadOrder);
            int rowUnloaded = 0;
            for(Integer row : unloadOrder) 
            {
                for(Container container : ev.getGrid()[column][row]) 
                {
                    if(container != null) 
                    {
                        return container;
                    } 
                    else 
                    {
                        rowUnloaded++;
                        if(rowUnloaded == unloadOrder.size()) 
                        {
                            ev.getColumns().set(column, true);
                            ev.updateColumn(column, true);
                        }
                    }
                }
            }
        }
        return null;
    }
    
    private Phase unload_getPhase(int currentCrane, int moveToColumn, ExternVehicle ev) 
    {
        Crane c = cranes.get(currentCrane);
        AGV craneAgv = craneAgvs.get(currentCrane);
        if(!busyCranes.contains(c) && craneAgv == null && moveToColumn != -1 && c.getStatus() == Status.WAITING) 
        {
            return Phase.MOVE;
        } 
        else if(busyCranes.contains(c) && c.getStatus() == Status.WAITING && craneAgv == null && getContainer(moveToColumn, ev) != null) 
        {
            return Phase.LOAD;
        }
        else if(busyCranes.contains(c) && craneAgv != null && craneAgv.getStatus() != Status.MOVING && c.getStatus() == Status.UNLOADING && !c.getCargo().isEmpty() && !agvSpots.get(currentCrane).isEmpty()) 
        {
            return Phase.UNLOAD;
        }
        else if(busyCranes.contains(c) && craneAgv != null && craneAgv.getStatus() != Status.MOVING && c.getStatus() == Status.WAITING && c.getCargo().isEmpty()) 
        {
            return Phase.SENDTOSTORAGE;
        }
        return null;
    }
    
    private void unload_phaseMove(int currentCrane, int column, ExternVehicle ev) 
    {
        Crane c = cranes.get(currentCrane);
        if(ev.getGrid()[column][0][0] != null) {
            try {
                c.followRoute(craneRoad.moveToContainer(ev, column, c));
                busyCranes.add(c);
            } catch (AgvNotAvailable ex) {
                Logger.getLogger(TrainPlatform.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private void unload_phaseLoad(int currentCrane, Container container, ExternVehicle ev) 
    {
        Crane c = cranes.get(currentCrane);
        // adjust parkingspot
        Vector3f cp = c.getPosition();
        agvSpots.set(currentCrane, new AgvSpot(new Vector3f(cp.x + TrainCrane.length*Settings.METER, cp.y, cp.z)));
        // send AGV from queue
        AGV agv = agvQueue.peek();
        if(agv.getStatus() != Status.MOVING) {
            try {
                agv = agvQueue.poll();
                System.out.println("AGV == " + agv.getStatus());
                agv.followRoute(road.getPathToParkingsSpot(agv, agvSpots.get(currentCrane)));
                System.out.println("AGV == " + agv.getStatus());
                System.out.println("breakie breakie");
                //while(true) {}
                craneAgvs.set(currentCrane, agv);
                if(c.getStatus() == Status.WAITING) {
                    try {
                        c.load(container, ev);
                    } catch (CargoOutOfBoundsException ex) {
                        Logger.getLogger(TrainPlatform.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (Exception ex) {
                        Logger.getLogger(TrainPlatform.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            } catch (AgvNotAvailable ex) {
                Logger.getLogger(TrainPlatform.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private void unload_phaseUnload(int currentCrane) 
    {
        Crane c = cranes.get(currentCrane);
        AGV craneAgv = craneAgvs.get(currentCrane);
        System.out.println("(UNLOAD) AGV STATUS == " + craneAgv.getStatus());
        if(!unloadOnce) {
            try {
                System.out.println("crane cargo == " + c.getCargo().size());
                c.unload(craneAgv);
                unloadOnce = true;
            } catch (VehicleOverflowException | ContainerNotFoundException | CargoOutOfBoundsException ex) {
                Logger.getLogger(TrainPlatform.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private void unload_phaseSendToStorage(int currentCrane) 
    {
        try {
            Crane c = cranes.get(currentCrane);
            AGV craneAgv = craneAgvs.get(currentCrane);
            busyCranes.remove(c);
            unloadOnce = false;
            craneAgvs.set(currentCrane, null);
            agvSpots.get(currentCrane).UnparkVehicle();
            // check where container needs to go
            //TransportType tt = craneAgv.getCargo().get(0).getDepartureTransport();
            int stripNr = Settings.port.getStoragePlatform().getNearbyStrip(getTransportType());
            AgvSpot agvSpot = null;
            for(int i = stripNr; i < Settings.port.getStoragePlatform().getStripAmount(); i++)
            {
                if(Settings.port.getStoragePlatform().getStrip(i).getStorageState() != StorageStrip.StorageState.FULL)
                {
                    agvSpot = Settings.port.getStoragePlatform().getStrip(i).getFreeAgvSpotLoad();
                }
                if(agvSpot != null)
                    break;
            }
            System.out.println("WOW WTF GEBEURT JIR");
            craneAgv.followRoute(road.getPathAllIn(craneAgv, agvSpots.get(currentCrane), agvSpot, Settings.port.getStoragePlatform().getLeft(), Settings.port.getMainroad()));
        } catch (AgvNotAvailable ex) {
            Logger.getLogger(TrainPlatform.class.getName()).log(Level.SEVERE, null, ex);
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
                    if(c.getStatus() != Status.LOADING && c.getStatus() != Status.UNLOADING && c.getStatus() != Status.MOVING)
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
        
        if(hasExtVehicle()) {
            state = State.UNLOAD;
        } else if(jobs.size() > 0) {
            state = State.LOAD;
        } else {
            state = State.FREE;
        }
        
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