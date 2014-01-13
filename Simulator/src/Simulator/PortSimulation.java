package Simulator;

import static Simulator.Type.TRAINCRANE;
import com.jme3.app.SimpleApplication;
import com.jme3.cinematic.MotionPath;
import com.jme3.cinematic.events.MotionEvent;
import com.jme3.collision.CollisionResults;
import com.jme3.font.BitmapText;
import com.jme3.input.ChaseCamera;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.util.SkyFactory;
import containing.Point3D;
import containing.Command;
import java.util.HashMap;
import java.util.List;

public class PortSimulation extends SimpleApplication {

    AGV[] agv = new AGV[100];
    //Container[] container = new Container[19];
    FreeCrane[] freeCranes = new FreeCrane[4];
    StorageCrane[] storageCranes;
    RailCrane[] railCrane;
    Port port;
    //private StorageCrane storageCrane = new StorageCrane(assetManager, rootNode);
    //Truck[] truck = new Truck[20];
    Train train;
    BitmapText hudText;
    BitmapText containerText;
    ChaseCamera chaseCam;
    int camstate = 1;
    String currentChaseTarget;
    String containerInfo;
    Vector3f flyCamPos;
    
    public static void main(String[] args) {
        PortSimulation app = new PortSimulation();
        app.start();

        Runnable networkHandler = new NetworkHandler("localhost", 1337);
        //Runnable networkHandler = new NetworkHandler("141.252.222.88", 1337);
        Thread t = new Thread(networkHandler);
        t.start();
    }

    @Override
    public void simpleInitApp() {
        train = new Train(assetManager, rootNode);
        this.setDisplayStatView(false);
        flyCam.setEnabled(false);
        rootNode.setName("rootnode");
        rootNode.attachChild(SkyFactory.createSky(assetManager, "Textures/Sky/Bright/BrightSky.dds", false));
        flyCamPos = new Vector3f(817f / 20, 80f, 1643f / 20);
        this.setPauseOnLostFocus(false);
        
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
        
        /*
        MotionPath mp = new MotionPath();
        mp.addWayPoint(new Vector3f());
        
        mp.addWayPoint(new Vector3f(71.700005f, 5.5f, 1.9f));
        mp.addWayPoint(new Vector3f(71.700005f, 5.5f, 156.2f));
        mp.addWayPoint(new Vector3f(71.700005f, 5.5f, 0.0f));
        mp.addWayPoint(new Vector3f(10.3f, 5.5f, 0.0f));
        mp.addWayPoint(new Vector3f(10.3f, 5.5f, 1.5f));
        mp.addWayPoint(new Vector3f(3.575f, 5.5f, 1.5f));
        mp.setCurveTension(0.0f);
        
        agv[0] = new AGV(assetManager, rootNode, 1);
        agv[0].place(71.700005f, 5.5f, 156.2f);
        chaseCamSetTarget(agv[0].agv, agv[0].agv.getName());
        camstate = 2;
        
        MotionEvent motev = new MotionEvent(agv[0].agv, mp, LoopMode.Loop);
        motev.setDirectionType(MotionEvent.Direction.Path);
        motev.setSpeed(1f);
        //motev.play();
        */
        
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

        inputManager.clearMappings();
        inputManager.addMapping("mousedown", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addListener(analogListener, "mousedown");
        inputManager.addMapping("escape", new KeyTrigger(KeyInput.KEY_ESCAPE));
        inputManager.addListener(actionListener, "escape");
        inputManager.addMapping("flycam", new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addListener(actionListener, "flycam");

        // font color
        hudText = new BitmapText(guiFont, false);
        hudText.setSize(guiFont.getCharSet().getRenderedSize());      // font size
        hudText.setColor(ColorRGBA.White);
        guiNode.attachChild(hudText);
        
        containerText = new BitmapText(guiFont, false);
        containerText.setSize(guiFont.getCharSet().getRenderedSize()); 
        containerText.setColor(ColorRGBA.White);
        containerText.setLocalTranslation(Vector3f.ZERO);
        guiNode.attachChild(containerText);
        
        /*
        Geometry quad = new Geometry("quad", new Quad(300,300));
        Material transblack = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        transblack.setColor("Color", new ColorRGBA(1, 0, 0, 0));
        transblack.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);  
        quad.setMaterial(transblack);
        quad.setQueueBucket(Bucket.Transparent);    
        quad.setLocalTranslation(0, settings.getHeight()-300, 0);
        guiNode.attachChild(quad); */
        
    }
    
    private ActionListener actionListener = new ActionListener() {
        public void onAction(String name, boolean keyPressed, float tpf) {
            if (name.equals("escape") && !keyPressed) {
                if (camstate == 3)
                {
                    flyCamPos = cam.getLocation();
                }
                camstate = 1;
            }
            else if (name.equals("flycam") && !keyPressed) {
                camstate = 3;
                if (camstate != 3)
                {
                    cam.setLocation(flyCamPos);
                }
            }
        }
    };
    
    private AnalogListener analogListener = new AnalogListener() {
        public void onAnalog(String name, float intensity, float tpf) {
            if (name.equals("mousedown")) {
                Vector3f origin = cam.getWorldCoordinates(inputManager.getCursorPosition(), 0.0f);
                Vector3f direction = cam.getWorldCoordinates(inputManager.getCursorPosition(), 0.3f);
                direction.subtractLocal(origin).normalizeLocal();

                Ray ray = new Ray(origin, direction);
                CollisionResults results = new CollisionResults();

                rootNode.collideWith(ray, results);
                if (results.size() > 0) {
                    Geometry closest = results.getClosestCollision().getGeometry();
                    if (!closest.getName().equals("port")) {
                        if (closest.getName().contains("Container"))
                        {
                            chaseCamSetTarget(closest, closest.getName());
                        }
                        else if (closest.getName().contains("AGV"))
                        {
                            chaseCamSetTarget(closest, closest.getName());
                        } 
                        else if(!closest.getParent().getName().contains("-objnode"))
                        {
                            chaseCamSetTarget(closest, closest.getParent().getName());
                        }
                        else
                        {
                            chaseCamSetTarget(closest, closest.getParent().getParent().getName());
                    
                        }
                        camstate = 2;
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
        if (railCrane != null)
        {
            for (RailCrane c : railCrane)
            {
                c.update(tpf);
            }
        }
        if (storageCranes != null)
        {
            for (StorageCrane c : storageCranes)
            {
                c.update(tpf);
            }
        }
        for (AGV c : agv)
        {
            if (c != null)
                c.update();
        }

        switch (camstate) {
            case 1:
                if (chaseCam!=null)
                {
                    chaseCam.setEnabled(false);
                    chaseCam = null;
                }
                inputManager.setCursorVisible(true);
                cam.setLocation(new Vector3f(817f / 20, 140f, 1643f / 20));
                cam.lookAt(new Vector3f(817f / 20, 0f, 1643f / 20), Vector3f.UNIT_X);

                hudText.setText("Top view");             // the text
                hudText.setLocalTranslation(settings.getWidth() / 2 - hudText.getLineWidth() / 2, settings.getHeight() - hudText.getLineHeight() / 2, 0); // position
                containerInfo = "";
                containerText.setText(containerInfo);
                break;

            case 2:
                hudText.setText("Chasing: " + currentChaseTarget);             // the text
                hudText.setLocalTranslation(settings.getWidth() / 2 - hudText.getLineWidth() / 2, settings.getHeight() - hudText.getLineHeight() / 2, 0); // position
                containerText.setText(containerInfo);
                containerText.setLocalTranslation(0, settings.getHeight() - hudText.getLineHeight() / 2, 0);
                break;
                
            case 3:
                if (chaseCam!=null)
                {
                    chaseCam.setEnabled(false);
                    chaseCam = null;
                }
                flyCam.setEnabled(true);
                flyCam.setMoveSpeed(10f);
                inputManager.setCursorVisible(false);
                hudText.setText("FlyCam");             // the text
                hudText.setLocalTranslation(settings.getWidth() / 2 - hudText.getLineWidth() / 2, settings.getHeight() - hudText.getLineHeight() / 2, 0); // position
                containerInfo = "";
                containerText.setText(containerInfo);
                break;
        }

        if (!CommandHandler.newStackedCommandsAvailable()) {
            return;
        }
        Command cmd = CommandHandler.getStackedCommand();
        if (cmd == null) {
            return;
        }

        if (cmd.getCommand().equals("INIT")) {
            System.out.println(cmd.getCommand());

            containing.Port _port = (containing.Port) cmd.getObject();

            containing.Platform.TrainPlatform trainPlatform = ((containing.Platform.TrainPlatform) _port.getPlatforms().get(2));
            int aantal_traincranes = trainPlatform.CRANES;
            railCrane = new RailCrane[aantal_traincranes];

            for (int i = 0; i < aantal_traincranes; i++) {
                containing.Vehicle.TrainCrane crane = (containing.Vehicle.TrainCrane) trainPlatform.getCranes().get(i);
                railCrane[i] = new RailCrane(assetManager, rootNode, crane.getID());
                railCrane[i].place(crane.getPosition().x, crane.getPosition().y, crane.getPosition().z);
            }
            
            containing.Platform.StoragePlatform storagePlatform = ((containing.Platform.StoragePlatform) _port.getStoragePlatform());
            int aantal_storagecranes = storagePlatform.getStripAmount();
            storageCranes = new StorageCrane[aantal_storagecranes];
            
            for (int i = 0; i <  aantal_storagecranes; i++)
            {
                containing.Vehicle.StorageCrane crane = (containing.Vehicle.StorageCrane) storagePlatform.getCranes().get(i);
                storageCranes[i] = new StorageCrane(assetManager, rootNode, crane.getID());
                storageCranes[i].place(crane.getPosition().x, crane.getPosition().y, crane.getPosition().z);
            }

            for (int i = 0; i < 100; i++) {
                containing.Vehicle.AGV _agv = _port.getStoragePlatform().getAgvs().get(i);
                agv[i] = new AGV(assetManager, rootNode, _agv.getID());
                agv[i].agv.rotate(0, 90 * FastMath.DEG_TO_RAD, 0);
                agv[i].place(_agv.getPosition().x, _agv.getPosition().y, _agv.getPosition().z);
            }

        } else if (cmd.getCommand().equals("enterExternVehicle")) {
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
        } else if (cmd.getCommand().equals("loadInternVehicle")) {
            System.out.println(cmd.getCommand());

            HashMap<String, Object> map = (HashMap<String, Object>) cmd.getObject();
            int id = Integer.parseInt(map.get("id").toString());
            System.out.println("" + id);

            Type type = Type.valueOf(map.get("vehicleType").toString());
            System.out.println("" + type.toString());

        } else if (cmd.getCommand().equals("followPath")) {
            System.out.println(cmd.getCommand());

            HashMap<String, Object> map = (HashMap<String, Object>) cmd.getObject();
            int id = Integer.parseInt(map.get("id").toString());
            System.out.println("" + id);

            Type type = Type.valueOf(map.get("vehicleType").toString());
            System.out.println("" + type.toString());

            MotionPath path = new MotionPath();
            containing.Road.Route route = (containing.Road.Route) map.get("motionPath");
            //containing.Road.Route route = containing.Settings.port.getMainroad().getPath();
            List<containing.Vector3f> motion = route.getWeg();
            for (containing.Vector3f v : motion) {
                path.addWayPoint(new Vector3f(v.x, 5.5f, v.z));
            }
            path.setCurveTension(0.0f);
            float duration = Float.parseFloat(map.get("duration").toString());

            MotionEvent motev;
            switch (type) {
                case TRAIN:
                    train.place(-41.5f, 5.5f, 0f);
                    motev = new MotionEvent(train.train, path, duration);
                    motev.setSpeed(1f);
                    motev.play();
                    break;

                case AGV:
                    for (AGV a : agv) {
                        if (a.id == id) {
                            motev = new MotionEvent(a.agv, path, duration);
                            motev.setSpeed(1f);
                            motev.setDirectionType(MotionEvent.Direction.Path);
                            motev.play();
                            
                            if (a.occupied)
                            {
                                MotionPath conpath = new MotionPath();
                                for (int i = 0; i < path.getNbWayPoints(); i++)
                                {
                                    Vector3f waypoint = path.getWayPoint(i).clone();
                                    waypoint.setY(waypoint.getY()+0.31f);
                                    conpath.addWayPoint(waypoint);
                                }
                                conpath.setCurveTension(0.0f);
                                
                                MotionEvent conmotion = new MotionEvent(a.con.model, conpath, duration);
                                conmotion.setSpeed(1f);
                                conmotion.setDirectionType(MotionEvent.Direction.Path);
                                conmotion.play();
                            }
                        }

                    }
                    break;
                
                case TRAINCRANE:
                    for (RailCrane r : railCrane) {
                        if (r.id == id) {
                            motev = new MotionEvent(r.crane, path, duration);
                            motev.setSpeed(1f);
                            motev.play();
                        }
                    }
                    break;
            }
        } else if (cmd.getCommand().equals("loadCrane")) {
            System.out.println(cmd.getCommand());

            HashMap<String, Object> map = (HashMap<String, Object>) cmd.getObject();
            int vehicle_id = Integer.parseInt(map.get("clientid").toString());
            System.out.println("Vehicle id:" + vehicle_id);
            int crane_id = Integer.parseInt(map.get("craneid").toString());
            System.out.println("Crane id:" + crane_id);
            
            Type type = Type.valueOf(map.get("vehicleType").toString());
            System.out.println("" + type.toString());
            
            containing.Container con = (containing.Container) map.get("container");
            
            switch (type)
            {
                case TRAINCRANE:
                    Container c = train.detachContainer(con.getContainerId());
                    for (RailCrane crane : railCrane)
                    {
                        if (crane.id == crane_id)
                        {
                            c.move(train.train.getLocalTranslation());
                            crane.loadCrane(c);
                        }
                    }
                    break;
            }
            
         } else if (cmd.getCommand().equals("unloadCrane")) {
            System.out.println(cmd.getCommand());

            HashMap<String, Object> map = (HashMap<String, Object>) cmd.getObject();
            int agv_id = Integer.parseInt(map.get("AGVID").toString());
            System.out.println("AGV id:" + agv_id);
            int crane_id = Integer.parseInt(map.get("craneid").toString());
            System.out.println("Crane id:" + crane_id);
            
            Type type = Type.valueOf(map.get("vehicleType").toString());
            System.out.println("" + type.toString());
            
            switch (type)
            {
                case TRAINCRANE:
                    for (RailCrane crane : railCrane)
                    {
                        if (crane.id == crane_id)
                        {
                            Container c = crane.unloadCrane();
                            
                            for (AGV a : agv)
                            {
                                if (a.id == agv_id)
                                {
                                    a.attachContainer(c);
                                    
                                    Vector3f pos = a.agv.getLocalTranslation();
                                    pos.setY(pos.getY() + 0.31f + (2.25f*0.33f));
                                    c.place(pos.x, pos.y, pos.z);
                                }
                            }
                        }
                    }
                    break;
            } 
            
        } else if (cmd.getCommand().equals("loadStorageCrane")) 
        {
            System.out.println(cmd.getCommand());

            HashMap<String, Object> map = (HashMap<String, Object>) cmd.getObject();
            int vehicle_id = Integer.parseInt(map.get("clientid").toString());
            System.out.println("Vehicle id:" + vehicle_id);
            int crane_id = Integer.parseInt(map.get("craneid").toString());
            System.out.println("Crane id:" + crane_id);
            
            Type type = Type.valueOf(map.get("vehicleType").toString());
            System.out.println("" + type.toString());
            
            int index_nr = Integer.parseInt(map.get("indexnr").toString());
            System.out.println("" + index_nr);
            
            MotionPath path = new MotionPath();
            List<containing.Vector3f> motion = (List<containing.Vector3f>) map.get("path");
            //containing.Road.Route route = containing.Settings.port.getMainroad().getPath();
            for (containing.Vector3f v : motion) {
                path.addWayPoint(new Vector3f(v.x, 5.5f, v.z));
            }
            
            path.setCurveTension(0.0f);
            float duration = Float.parseFloat(map.get("duration").toString());
            
            //Container c = train.detachContainer(con.getContainerId());
            for (StorageCrane crane : storageCranes)
            {
                if (crane.id == crane_id)
                {
                    AGV _agv = null;
                    for (AGV a : agv)
                    {
                        if (a.id == vehicle_id)
                        {
                            _agv = a;
                            break;
                        }
                    }
                    MotionEvent motev = new MotionEvent(crane.crane, path);
                    motev.setSpeed(1f);
                    Container con = _agv.releaseContainer();
                    crane.loadCrane(con, index_nr, motev);
                }
            }

        } else if (cmd.getCommand().equals("unloadStorageCrane")) 
        {
            System.out.println(cmd.getCommand());

            HashMap<String, Object> map = (HashMap<String, Object>) cmd.getObject();
            int crane_id = Integer.parseInt(map.get("craneid").toString());
            System.out.println("Crane id:" + crane_id);
            
            Type type = Type.valueOf(map.get("vehicleType").toString());
            System.out.println("" + type.toString());
            
            //int index_nr = Integer.parseInt(map.get("indexnr").toString());
            //System.out.println("" + index_nr);
            
            MotionPath path = new MotionPath();
            List<containing.Vector3f> motion = (List<containing.Vector3f>) map.get("path");
            //containing.Road.Route route = containing.Settings.port.getMainroad().getPath();
            for (containing.Vector3f v : motion) {
                path.addWayPoint(new Vector3f(v.x, 5.5f, v.z));
            }
            
            containing.Point3D droppoint = (containing.Point3D)map.get("containerindex");
            System.out.println("DROPPOINT: z="+droppoint.z);
            path.setCurveTension(0.0f);
            float duration = Float.parseFloat(map.get("duration").toString())/10;
            
            //Container c = train.detachContainer(con.getContainerId());
            for (StorageCrane crane : storageCranes)
            {
                if (crane.id == crane_id)
                {
                    MotionEvent motev = new MotionEvent(crane.crane, path);
                    motev.setSpeed(1f);
                    
                    MotionPath con_path = new MotionPath();
                    for (int i = 0; i < path.getNbWayPoints(); i++)
                    {
                        con_path.addWayPoint(new Vector3f(path.getWayPoint(i).x, crane.con.model.getLocalTranslation().y, crane.con.model.getLocalTranslation().z));
                    }
                    con_path.setCurveTension(0.0f);
                    MotionEvent con_motev = new MotionEvent(crane.con.model, con_path);
                    con_motev.setSpeed(1f);
                    
                    crane.unloadCrane(motev, con_motev, droppoint);
                }
            }

        }
    }

    public void chaseCamSetTarget(Spatial target, String name) {
        if (chaseCam == null)
        {
            cam.setLocation(new Vector3f(0f, 0f, 10f));
            cam.lookAt(new Vector3f(0f, 0f, 0f), Vector3f.UNIT_Y);

            chaseCam = new ChaseCamera(cam, target, inputManager);
            chaseCam.setSmoothMotion(true);
            chaseCam.setInvertVerticalAxis(true);
            chaseCam.setMaxDistance(1000);
            chaseCam.setMinVerticalRotation(0.32259342f);
            chaseCam.setMaxVerticalRotation(0.32259343f);
            chaseCam.setDragToRotate(false);
            chaseCam.setDefaultVerticalRotation(0.32259343f);
            currentChaseTarget = name;
            
            //Show containers information
            if (name.contains("Container"))
            {
                String data = target.getUserData("data").toString();
                containerInfo = data;
            } else {
                containerInfo = "";
            }
        }
    }
}
