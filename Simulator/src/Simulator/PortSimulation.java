package Simulator;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.AssetManager;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Node;

/**
 * test
 * @author normenhansen
 */
public class PortSimulation extends SimpleApplication {
  
    private static AVG avg;
    private Barge barge;
    private Container container;
    private FreeCrane freeCrane;
    private Freighter freighter;
    private RailCrane railCrane;
    private StorageCrane storageCrane;
    private Train train;
    private Truck truck;

    public static void main(String[] args) 
    {
        PortSimulation app = new PortSimulation();
        app.start();
        
        avg = new AVG(assetManager, rootNode);
    }

    @Override
    public void simpleInitApp() {

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
