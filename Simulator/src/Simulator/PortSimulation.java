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
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.util.SkyFactory;
import containing.Command;
import java.util.HashMap;
import java.util.List;

/**
 * test
 *
 * @author normenhansen
 */
public class PortSimulation extends SimpleApplication {

    AGV avg;
    //Container[] container = new Container[19];
    FreeCrane[] freeCranes = new FreeCrane[4];
    Port port;
    RailCrane railCrane;
    //private StorageCrane storageCrane = new StorageCrane(assetManager, rootNode);
    //Truck[] truck = new Truck[20];
    
    ChaseCamera chaseCam;

    public static void main(String[] args) {
        PortSimulation app = new PortSimulation();
        app.start();

        Runnable networkHandler = new NetworkHandler("141.252.222.189", 1337);
        Thread t = new Thread(networkHandler);
        t.start();
    }

    @Override
    public void simpleInitApp() {
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


        freeCranes[0].place(0, 5.5f, 81);
        freeCranes[1].place(10, 5.5f, 81);
        freeCranes[2].place(-40, 5.5f, 81);
        freeCranes[3].place(-30, 5.5f, 81);

        railCrane = new RailCrane(assetManager, rootNode);
        railCrane.place(-41.25f, 5.5f, -1.52f);
        //avg = new AVG(assetManager, rootNode);


        //for (int i = 0; i < 19; i++) {
            //container[i] = new Container(assetManager, rootNode, ColorRGBA.randomColor());
            //container[i].place(-42, 5.23f, -1.5f * (i + 1));
        //}

        //railCrane.attachContainer(container);

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
        
        inputManager.addMapping("mousedown", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
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
                    Geometry closest = results.getClosestCollision().getGeometry();
                    if (!closest.getName().equals("port"))
                    {
                        chaseCam.setSpatial(closest);
                    }
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
        Train train = new Train(assetManager, rootNode);
        
        if(!CommandHandler.newStackedCommandsAvailable()){
            return;
        }
        Command cmd = CommandHandler.getStackedCommand();
        if(cmd == null){
            return;
        }

        if (cmd.getCommand().equals("enterExternVehicle")) {
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
            List<containing.Vector3f> motion = route.getWeg();
            for (containing.Vector3f v : motion)
            {
                path.addWayPoint(new Vector3f(v.x, 5.5f, v.z));
            }
            int movspeed = Integer.parseInt(map.get("speed").toString());
            float mov_speed = (float)movspeed/100;
            
            train.place(-41.5f, 5.5f, 0f);
            MotionEvent motev = new MotionEvent(train.train, path);
            motev.setSpeed(mov_speed);
            motev.play();
        }

    }
}
