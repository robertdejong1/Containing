/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Simulator;

import com.jme3.asset.AssetManager;
import com.jme3.asset.TextureKey;
import com.jme3.material.Material;
import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.texture.Texture;

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
        
        Material stone_mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        Texture tex2 = assetManager.loadTexture(new TextureKey("Textures/asphalt.jpg"));
        tex2.setWrap(Texture.WrapMode.Repeat);
        stone_mat.setTexture("ColorMap", tex2);
        ((Geometry) port).getMesh().scaleTextureCoordinates(new Vector2f(64, 64));
        port.setMaterial(stone_mat);
        
        port.scale(10);
        port.scale(1f, 0.2f, 1f);
        port.move(0,4,0);
        port_node.attachChild(port);
        
        cranerail.setLocalTranslation(-1, 5, 81);
        cranerail.rotate(0, 90*FastMath.DEG_TO_RAD, 0);
        cranerail.scale(1, 1, 10);
        port_node.attachChild(cranerail.clone());
        cranerail.setLocalTranslation(-1, 5, 78.95f);
        port_node.attachChild(cranerail);
        
        trainrail.scale(2);
        for (int i = 0; i < 14*5; i++)
        {
        trainrail.setLocalTranslation(-42f, 5f, -77.25f+((10.4f/5)*i));
        port_node.attachChild(trainrail.clone());
        }
        
        node.attachChild(port_node);
    }
    
}
