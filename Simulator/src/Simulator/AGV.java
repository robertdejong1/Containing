/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Simulator;

import com.jme3.asset.AssetManager;
import com.jme3.scene.Node;

/**
 * This class contains the Automatic Guided Vehicle
 * @author Benjamin
 */
public class AGV extends Model
{
    Container container;
    boolean occupied;
    public int id;
    
    /**
     * Creates AGV instance
     * @param assetManager AssetManager for loading assets
     * @param node the node to attach the AGV on
     * @param id the AGV's id
     */
    public AGV(AssetManager assetManager, Node node, int id)
    {
        super(assetManager, "Models/agv.j3o", node);
        super.model.setName("AGV " + id);
        //this.model.rotate(0, 90*FastMath.DEG_TO_RAD, 0);
        //this.model.scale(0.2f);
        //this.model.scale(1.2f, 1.2f, 0.8f);
        this.id = id;
        this.occupied = false;
    }
      
    /**
     * Attach container to an AGV
     * @param con the container to attach
     * @return false if AGV's already occupied, true on succes
     */
    public boolean attachContainer(Container con)
    {
        if (!occupied)
        {
            this.container = con;
            this.occupied = true;
            return true;
        }
        return false;
    }
    
    /**
     * Detach container from AGV
     * @return the container
     */
    public Container releaseContainer()
    {
        if (occupied)
        {
            Container temp = this.container;
            this.container = null;
            occupied = false;
            return temp;
        }
        return null;
    }
    
    /**
     * Move the AGV in specified direction
     * @param x x value to move
     * @param y y value to move
     * @param z z value to move
     */
    @Override
    public void move(float x, float y, float z)
    {
        super.move(x, y, z);
        if (this.container != null)
        {
            this.container.move(x, y, z);
        }    
    }
    
}
