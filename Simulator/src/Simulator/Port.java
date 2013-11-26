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
    Spatial cranerail;
    Spatial trainrail;
    
    public Port(AssetManager assetManager, Node node)
    {
        port = assetManager.loadModel("Models/port.j3o");
        cranerail = assetManager.loadModel("Models/rail.j3o");
        trainrail = assetManager.loadModel("Models/trainrails.j3o");
        
        port.scale(10);
        port_node.attachChild(port);
        
        cranerail.setLocalTranslation(-1, 5, 81);
        cranerail.rotate(0, 90*FastMath.DEG_TO_RAD, 0);
        cranerail.scale(1, 1, 10);
        port_node.attachChild(cranerail.clone());
        cranerail.setLocalTranslation(-1, 5, 78.95f);
        port_node.attachChild(cranerail);
        
        trainrail.scale(10);
        for (int i = 0; i < 14; i++)
        {
        trainrail.setLocalTranslation(-42, 5.1f, -77.25f+(10.4f*i));
        port_node.attachChild(trainrail.clone());
        }
        
        node.attachChild(port_node);
    }
    
}
