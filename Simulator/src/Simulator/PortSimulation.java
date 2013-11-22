package Simulator;

import com.jme3.app.SimpleApplication;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;

/**
 * test
 * @author normenhansen
 */
public class PortSimulation extends SimpleApplication {
  
    //private AVG avg = new AVG(assetManager, rootNode);
    //private Barge barge = new Barge(assetManager, rootNode);
    //private Container container = new Container(assetManager, rootNode);
    FreeCrane freeCrane;
    //private Freighter freighter = new Freighter(assetManager, rootNode);
    //RailCrane railCrane = new RailCrane(assetManager, rootNode);
    //private StorageCrane storageCrane = new StorageCrane(assetManager, rootNode);
    //private Train train = new Train(assetManager, rootNode);
    //private Truck truck = new Truck(assetManager, rootNode);

    public static void main(String[] args) 
    {
        PortSimulation app = new PortSimulation();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        flyCam.setMoveSpeed(50f);
        freeCrane = new FreeCrane(assetManager, rootNode);
        freeCrane.place();
        
        AmbientLight ambient = new AmbientLight();
        ambient.setColor(ColorRGBA.White);
        rootNode.addLight(ambient); 
    }

    @Override
    public void simpleUpdate(float tpf) {
        //TODO: add update code
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }
}
