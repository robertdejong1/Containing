/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Simulator;

import com.jme3.asset.AssetManager;
import com.jme3.scene.Node;


public class FreeCrane extends Model
{

    public FreeCrane(AssetManager assetManager, Node node, int id)
    {
        super(assetManager, "Models/freecrane.j3o", node);
        super.model.setName("FreeCrane " + id);
        super.model.scale(0.5f);
    }
    
}
