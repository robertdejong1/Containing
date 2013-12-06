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

public class Freighter
{
    public List<Container> containers;
    Node freighter;
    Node node;
    Spatial freighter_model;
    
    public Freighter(AssetManager assetManager, Node node)
    {
        this.node = node;
        freighter = new Node();
        
        freighter_model = assetManager.loadModel("Models/freighter.j3o");
        freighter_model.scale(3f);
        freighter_model.scale(2f, 1f, 1f);
        freighter.attachChild(freighter_model);
        
        containers = new ArrayList<Container>();
    }
    
    public void addContainer(Container con)
    {
        con.place(con.arrivalPosition.x*(Container.width/10), con.arrivalPosition.y*(Container.height/10), con.arrivalPosition.z*(Container.depth/10));
        con.model.move((((BoundingBox)freighter_model.getWorldBound()).getXExtent()/2)*-1, ((BoundingBox)freighter_model.getWorldBound()).getYExtent()/2, 0);
        freighter.attachChild(con.model);
        containers.add(con);
    }
    
    public void place(float x, float y, float z)
    {
        freighter.setLocalTranslation(new Vector3f(x,y,z));
        node.attachChild(freighter);
    }
    
}
