/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Simulator;

import com.jme3.asset.AssetManager;
import com.jme3.bounding.BoundingBox;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.util.ArrayList;
import java.util.List;


public class Barge
{
    public List<Container> containers;
    Node barge;
    Node node;
    Spatial barge_model;
    
    public Barge(AssetManager assetManager, Node node)
    {
        this.node = node;
        barge = new Node();
        
        barge_model = assetManager.loadModel("Models/barge.j3o");
        barge_model.scale(3f);
        barge_model.scale(2f, 1f, 1f);
        barge.attachChild(barge_model);
        barge.setName("Barge");
        
        containers = new ArrayList<Container>();
    }
    
    public void addContainer(Container con)
    {
        con.place(con.arrivalPosition.x*(Container.width/10), con.arrivalPosition.y*(Container.height/10), con.arrivalPosition.z*(Container.depth/10));
        con.model.move((((BoundingBox)barge_model.getWorldBound()).getXExtent()/2)*-1, ((BoundingBox)barge_model.getWorldBound()).getYExtent()/2, 0);
        barge.attachChild(con.model);
        containers.add(con);
    }
    
    public void place(Vector3f loc)
    {
        barge.setLocalTranslation(loc);
        node.attachChild(barge);
    }
    
    public void place(float x, float y, float z)
    {
        barge.setLocalTranslation(new Vector3f(x,y,z));
        node.attachChild(barge);
    }
    
    public Container detachContainer(int containerID)
    {
        for (Container c : containers)
        {
            if (c.containerId == containerID)
                return c;
        }
        return null;
    }
}
