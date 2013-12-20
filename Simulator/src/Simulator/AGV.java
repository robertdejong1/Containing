/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Simulator;

import com.jme3.asset.AssetManager;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

/**
 * This class contains the Automatic Guided Vehicle
 * @author Benjamin
 */
public class AGV
{
    Container con;
    boolean occupied = false;
    public int id;
    
    Node node;
    Node agv_model;
    Node agv;
    
    /**
     * Creates AGV instance
     * @param assetManager AssetManager for loading assets
     * @param node the node to attach the AGV on
     * @param id the AGV's id
     */
    public AGV(AssetManager assetManager, Node node, int id)
    {
        this.node = node;
        agv = new Node();
        agv.setName("AGV " + id);
        this.id = id;
        agv_model = new Node();

        Spatial model = assetManager.loadModel("Models/agv.j3o");
        model.scale(0.8f, 1.4f, 1.4f);
        
        agv_model.attachChild(model);
        agv.attachChild(agv_model);

        this.con = null;
    }
      
    /**
     * Attach container to an AGV
     * @param con the container to attach
     * @return false if AGV's already occupied, true on succes
     */
    public void attachContainer(Container con)
    {
        this.con = con;
        this.occupied = true;
        //agv.attachChild(con.model);
        //con.model.move(agv.getLocalTranslation().x-0.07f,agv.getLocalTranslation().y+0.35f,agv.getLocalTranslation().z-0.33f);
    }
    
    /**
     * Detach container from AGV
     * @return the container
     */
    public Container releaseContainer()
    {
        if (occupied)
        {
            Container temp = this.con;
            this.con = null;
            occupied = false;
            return temp;
        }
        return null;
    }
    
    private void place(Vector3f loc)
    {
        agv.setLocalTranslation(loc);
        node.attachChild(agv);
    }
    
    public void place(float x, float y, float z)
    {
        place(new Vector3f(x,y,z));
    }
}
