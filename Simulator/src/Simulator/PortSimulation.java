package Simulator;

import com.jme3.app.SimpleApplication;
import com.jme3.cinematic.MotionPath;
import com.jme3.cinematic.events.MotionEvent;
import com.jme3.collision.CollisionResults;
import com.jme3.input.ChaseCamera;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.util.SkyFactory;
import containing.Command;
import containing.Settings;
import java.util.HashMap;
import java.util.List;

/**
 * test
 *
 * @author normenhansen
 */
public class PortSimulation extends SimpleApplication {

    AGV[] agv = new AGV[100];
    //Container[] container = new Container[19];
    FreeCrane[] freeCranes = new FreeCrane[4];
    StorageCrane[] storageCranes = new StorageCrane[61];
    RailCrane[] railCrane;
    Port port;
    //private StorageCrane storageCrane = new StorageCrane(assetManager, rootNode);
    //Truck[] truck = new Truck[20];
    
    Train train; 
    
    ChaseCamera chaseCam;

    public static void main(String[] args) {
        PortSimulation app = new PortSimulation();
        app.start();

        //Runnable networkHandler = new NetworkHandler("141.252.236.70", 1337);
        //Runnable networkHandler = new NetworkHandler("localhost", 1337);
        //Thread t = new Thread(networkHandler);
        //t.start();
    }

    @Override
    public void simpleInitApp() {
        train = new Train(assetManager, rootNode);
        flyCam.setEnabled(true);
        flyCam.setMoveSpeed(50f);       
        chaseCam = new ChaseCamera(cam, inputManager);
        
        cam.setLocation(new Vector3f(0f, 140f, 0));
        cam.lookAt(Vector3f.ZERO, Vector3f.UNIT_X);
        rootNode.attachChild(SkyFactory.createSky(assetManager, "Textures/Sky/Bright/BrightSky.dds", false));

        for (int i = 0; i < 4; i++) {
            freeCranes[i] = new FreeCrane(assetManager, rootNode);
            freeCranes[i].scale(0.5f);
            freeCranes[i].rotate(0, 180 * FastMath.DEG_TO_RAD, 0);
        }
        port = new Port(assetManager, rootNode, viewPort);
        //port.scale(10f);
        //port.place();

        //for (int i = 0; i < 20; i++) {
            //truck[i] = new Truck(assetManager, rootNode);
            //truck[i].rotate(0, -90 * FastMath.DEG_TO_RAD, 0);
            //truck[i].scale(0.15f);
            //truck[i].place(40.32f, 5f, i);
        //}
        
        for (int i = 0; i < 61; i++)
        {
            storageCranes[0] = new StorageCrane(assetManager, rootNode);
            storageCranes[0].place(14, 5.5f, 3.15f+(i*2.5f));
        }

        DirectionalLight sun = new DirectionalLight();
        Vector3f lightDir = new Vector3f(-0.37352666f, -0.50444174f, -0.7784704f);
        sun.setDirection(lightDir);
        sun.setColor(ColorRGBA.White.clone().multLocal(2));
        rootNode.addLight(sun);

        DirectionalLight sun2 = new DirectionalLight();
        Vector3f lightDir2 = new Vector3f(0.37352666f, 0.50444174f, 0.7784704f);
        sun2.setDirection(lightDir2);
        sun2.setColor(ColorRGBA.White.clone().multLocal(2));
        rootNode.addLight(sun2);
        
        inputManager.addMapping("mousedown", new MouseButtonTrigger(MouseInput.BUTTON_MIDDLE));
        inputManager.addListener(analogListener,"mousedown");
    }
    
    private AnalogListener analogListener = new AnalogListener() {
        public void onAnalog(String name, float intensity, float tpf) 
        {
            if (name.equals("mousedown"))
            {
                CollisionResults results = new CollisionResults();
                Ray ray = new Ray(cam.getLocation(), cam.getDirection());
                
                rootNode.collideWith(ray, results);
                if (results.size() > 0) 
                {
                    //Geometry closest = results.getClosestCollision().getGeometry();
                    //if (!closest.getName().equals("port"))
                    //{
                        //chaseCam.setSpatial(closest);
                    //}
                    agv[0] = new AGV(assetManager, rootNode, 1);
                    agv[0].rotate(0, 90*FastMath.DEG_TO_RAD, 0);
                    agv[0].place(103f*Settings.METER,5.5f,0);
                    
                    MotionPath path = new MotionPath();
                    path.addWayPoint(new Vector3f(103f*Settings.METER,5.5f,0));
                    path.addWayPoint(new Vector3f(103f*Settings.METER,5.5f, 1562f*Settings.METER));
                    path.addWayPoint(new Vector3f(717f*Settings.METER,5.5f, 1562f*Settings.METER));
                    path.addWayPoint(new Vector3f(717f*Settings.METER, 5.5f, 0));
                    path.setCurveTension(0.0f);
                    
                    MotionEvent motev = new MotionEvent(agv[0].model, path, 10);
                    motev.setSpeed(1f);
                    motev.setDirectionType(MotionEvent.Direction.Path);
                    motev.play();
                }
            }
        }
    };
    


    @Override
    public void simpleUpdate(float tpf) { 
        //freeCranes[0].move(tpf/2, 0, 0);
        //train.move(0,0,tpf*2);
        //train.model.rotate(tpf, tpf, tpf)

        //for (int i = 0; i < 19; i++)
        //{
        //    container[i].move(0, 0, tpf*2);
        //}
        //railCrane.update(tpf);
        
        if(!CommandHandler.newStackedCommandsAvailable()){
            return;
        }
        Command cmd = CommandHandler.getStackedCommand();
        if(cmd == null){
            return;
        }
        
        if (cmd.getCommand().equals("INIT")) 
        {
            System.out.println(cmd.getCommand());
            
            containing.Port _port = (containing.Port)cmd.getObject();
            
            containing.Platform.TrainPlatform trainPlatform = ((containing.Platform.TrainPlatform)_port.getPlatforms().get(2));
            int aantal_traincranes = trainPlatform.CRANES;
            railCrane = new RailCrane[aantal_traincranes];
            
            for (int i = 0; i < aantal_traincranes; i++)
            {
                containing.Vehicle.TrainCrane crane = (containing.Vehicle.TrainCrane) trainPlatform.getCranes().get(i);
                railCrane[i] = new RailCrane(assetManager, rootNode);
                railCrane[i].place(crane.getPosition().x, crane.getPosition().y, crane.getPosition().z);
            }
            
            for (int i = 0; i < 100; i++)
            {
                containing.Vehicle.AGV _agv = _port.getStoragePlatform().getAgvs().get(i);
                agv[i] = new AGV(assetManager, rootNode, _agv.getID());
                agv[i].rotate(0, 90*FastMath.DEG_TO_RAD, 0);
                agv[i].place(_agv.getPosition().x, _agv.getPosition().y, _agv.getPosition().z);
            }
            
        } else if (cmd.getCommand().equals("enterExternVehicle")) 
        {
            System.out.println(cmd.getCommand());
            
            HashMap<String, Object> map = (HashMap<String, Object>) cmd.getObject();
            int id = Integer.parseInt(map.get("id").toString());
            System.out.println("" + id);

            Type type = Type.valueOf(map.get("vehicleType").toString());
            System.out.println("" + type.toString());
            Object[][][] containers = (Object[][][]) map.get("cargo");
            
            switch (type) {
                case TRAIN:
                    for (int i = 0; i < containers.length; i++) {
                        if (containers[i][0][0] != null) {
                            containing.Container c = (containing.Container) containers[i][0][0];
                            Container cont = new Container(assetManager, rootNode, ColorRGBA.randomColor());
                            cont.setData(c.getContainerId(), c.getArrivalDate(), c.getArrivalTimeFrom(), c.getArrivalTimeTill(), c.getArrivalTransport(), c.getArrivalTransportCompany(), c.getArrivalPosition(), c.getOwner(), c.getDepartureDate(), c.getDepartureTimeFrom(), c.getDepartureTimeTill(), c.getDepartureTransport());
                            train.addWagon(cont);
                            //System.out.println(cont);
                            
                        }
                    }
                    break;
                    
                case BARGE:
                    Barge barge = new Barge(assetManager, rootNode);
                    for (int i = 0; i < containing.Vehicle.Barge.nrContainersWidth; i++) {
                        for (int j = 0; j < containing.Vehicle.Barge.nrContainersHeight; j++) {
                            for (int k = 0; k < containing.Vehicle.Barge.nrContainersDepth; k++) {
                                if (containers[i][j][k] != null) {
                                    containing.Container c = (containing.Container) containers[i][j][k];
                                    Container cont = new Container(assetManager, rootNode, ColorRGBA.randomColor());
                                    cont.setData(c.getContainerId(), c.getArrivalDate(), c.getArrivalTimeFrom(), c.getArrivalTimeTill(), c.getArrivalTransport(), c.getArrivalTransportCompany(), c.getArrivalPosition(), c.getOwner(), c.getDepartureDate(), c.getDepartureTimeFrom(), c.getDepartureTimeTill(), c.getDepartureTransport());
                                    barge.addContainer(cont);
                                    //System.out.println(cont);
                                }
                            }
                        }
                    }
                    
                    barge.place(44, 4.5f, 70);
                    break;
                    
                case SEASHIP:
                    Freighter freighter = new Freighter(assetManager, rootNode);
                    for (int i = 0; i < containing.Vehicle.Seaship.nrContainersWidth; i++) {
                        for (int j = 0; j < containing.Vehicle.Seaship.nrContainersHeight; j++) {
                            for (int k = 0; k < containing.Vehicle.Seaship.nrContainersDepth; k++) {
                                if (containers[i][j][k] != null) {
                                    containing.Container c = (containing.Container) containers[i][j][k];
                                    Container cont = new Container(assetManager, rootNode, ColorRGBA.randomColor());
                                    cont.setData(c.getContainerId(), c.getArrivalDate(), c.getArrivalTimeFrom(), c.getArrivalTimeTill(), c.getArrivalTransport(), c.getArrivalTransportCompany(), c.getArrivalPosition(), c.getOwner(), c.getDepartureDate(), c.getDepartureTimeFrom(), c.getDepartureTimeTill(), c.getDepartureTransport());
                                    freighter.addContainer(cont);
                                    //System.out.println(cont);
                                }
                            }
                        }
                    }
                    freighter.place(0, 4f, 85);

                    break;
                    
                default:
                    break;
            }
        } else if (cmd.getCommand().equals("loadInternVehicle")) 
        {
            System.out.println(cmd.getCommand());
            
            HashMap<String, Object> map = (HashMap<String, Object>) cmd.getObject();
            int id = Integer.parseInt(map.get("id").toString());
            System.out.println("" + id);

            Type type = Type.valueOf(map.get("vehicleType").toString());
            System.out.println("" + type.toString());
           
        } else if (cmd.getCommand().equals("followPath")) 
        {
            System.out.println(cmd.getCommand());
            
            HashMap<String, Object> map = (HashMap<String, Object>) cmd.getObject();
            int id = Integer.parseInt(map.get("id").toString());
            System.out.println("" + id);

            Type type = Type.valueOf(map.get("vehicleType").toString());
            System.out.println("" + type.toString());
            
            MotionPath path = new MotionPath();
            containing.Road.Route route = (containing.Road.Route)map.get("motionPath");
            //containing.Road.Route route = containing.Settings.port.getMainroad().getPath();
            List<containing.Vector3f> motion = route.getWeg();
            for (containing.Vector3f v : motion)
            {
                path.addWayPoint(new Vector3f(v.x, 5.5f, v.z));
            }
            path.setCurveTension(0.0f);
            float duration = Float.parseFloat(map.get("duration").toString());
            
            MotionEvent motev;
            switch (type)
            {
                case TRAIN:
                    train.place(-41.5f, 5.5f, 0f);
                    motev = new MotionEvent(train.train, path, duration);
                    motev.setSpeed(1f);
                    motev.play();
                    break;
                    
                case AGV:
                    for (AGV a : agv)
                    {
                        if (a.id == id)
                        {
                            motev = new MotionEvent(a.model, path, duration);
                            motev.setSpeed(1f);
                            motev.setDirectionType(MotionEvent.Direction.Path);
                            motev.play();
                        }
                    }
                    break;
            }
        }

    }
}
