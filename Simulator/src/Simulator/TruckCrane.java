/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Simulator;

import com.jme3.asset.AssetManager;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

/**
 *
 * @author Benjamin
 */
public class TruckCrane {
    
    int id;
    Node crane;
    Node node;
    Container con;
    
    Spatial frame;
    Spatial grab;
    Spatial top;
    
    public TruckCrane(AssetManager assetManager, Node node, int id)
    {
        this.node = node;
        crane = new Node();
        crane.setName("TruckCrane " + id);
        this.id = id;
        
        frame = assetManager.loadModel("Models/storagecrane/framegreen.j3o");
        grab = assetManager.loadModel("Models/storagecrane/grab.j3o");
        top = assetManager.loadModel("Models/storagecrane/top.j3o");
        
        crane.attachChild(frame);
        crane.attachChild(grab);
        crane.attachChild(top);

        //crane.rotate(0, 90*FastMath.DEG_TO_RAD, 0);
        crane.scale(0.41f);
    }
    
    private void place(Vector3f loc)
    {
        crane.setLocalTranslation(loc);
        node.attachChild(crane);
    }
    
    public void place(float x, float y, float z)
    {
        place(new Vector3f(x,y,z));
    }
    
}
