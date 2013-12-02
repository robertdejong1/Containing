/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Simulator;

import com.jme3.asset.AssetManager;
import com.jme3.math.FastMath;
import com.jme3.scene.Node;

/**
 *
 * @author Benjamin
 */
public class AGV extends Model
{
    Container container;
    
    public AGV(AssetManager assetManager, Node node)
    {
        super(assetManager, "Models/agv.j3o", node);
        this.model.rotate(0, 90*FastMath.DEG_TO_RAD, 0);
        this.model.scale(0.2f);
        this.model.scale(1.2f, 1.2f, 0.8f);
    }
    
    public void attachContainer(Container con)
    {
        this.container = con;
    }
    
    @Override
    public void move(float x, float y, float z)
    {
        super.move(x, y, z);
        if (this.container != null)
        {
            this.container.move(x, y, z);
        }    }
    
}
