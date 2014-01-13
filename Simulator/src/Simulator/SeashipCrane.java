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
public class SeashipCrane extends Model
{
    
    public SeashipCrane(AssetManager assetManager, Node node, int id)
    {
        super(assetManager, "Models/freecrane.j3o", node);
        super.model.setName("SeashipCrane " + id);
        super.model.scale(0.5f);
        super.model.rotate(0, 180*FastMath.DEG_TO_RAD, 0);
    }
    
}
