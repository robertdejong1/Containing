/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Simulator;

import com.jme3.asset.AssetManager;
import com.jme3.math.FastMath;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

/**
 *
 * @author Benjamin
 */
public class Port
{
    Node port_node = new Node();
    Spatial port;
    Spatial rail;
    
    public Port(AssetManager assetManager, Node node)
    {
        port = assetManager.loadModel("Models/port.j3o");
        rail = assetManager.loadModel("Models/rail.j3o");
        
        port.scale(10);
        port_node.attachChild(port);
        
        rail.setLocalTranslation(0, 5, 81);
        rail.rotate(0, 90*FastMath.DEG_TO_RAD, 0);
        rail.scale(1, 1, 10);
        port_node.attachChild(rail.clone());
        rail.setLocalTranslation(0, 5, 78.95f);
        port_node.attachChild(rail);
        
        node.attachChild(port_node);
    }
    
}
