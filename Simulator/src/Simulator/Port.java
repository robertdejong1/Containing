/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Simulator;

import com.jme3.asset.AssetManager;
import com.jme3.asset.TextureKey;
import com.jme3.material.Material;
import com.jme3.math.FastMath;
import com.jme3.math.Plane;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Quad;
import com.jme3.texture.Texture;
import com.jme3.water.SimpleWaterProcessor;

/**
 *
 * @author Benjamin
 */
public class Port
{
    Node port_node = new Node("port");
    Spatial port;
    Spatial cranerail;
    Spatial trainrail;
    Spatial storage;
    Spatial road;
    
    public Port(AssetManager assetManager, Node node, ViewPort viewPort)
    {
        port = assetManager.loadModel("Models/port.j3o");
        cranerail = assetManager.loadModel("Models/rail.j3o");
        trainrail = assetManager.loadModel("Models/trainrails.j3o");
        storage = assetManager.loadModel("Models/storageplatform.j3o");
        road = assetManager.loadModel("Models/road.j3o");
        
        Material stone_mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        Texture tex2 = assetManager.loadTexture(new TextureKey("Textures/asphalt.jpg"));
        tex2.setWrap(Texture.WrapMode.Repeat);
        stone_mat.setTexture("ColorMap", tex2);
        ((Geometry) port).getMesh().scaleTextureCoordinates(new Vector2f(64, 64));
        //port.setMaterial(stone_mat);
        
        port.scale(10);
        //port.scale(1f, 0.2f, 1f);
        //port.move(0,4,0);
        port_node.attachChild(port);
        
        storage.scale(10);
        storage.move(0,0.01f,-4);
        port_node.attachChild(storage);
        
        //road.scale(10);
        //road.move(0,0.01f,-4);
        //port_node.attachChild(road);
        
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
        
        //Water
        SimpleWaterProcessor waterProcessor = new SimpleWaterProcessor(assetManager);
        waterProcessor.setReflectionScene(node);

        Vector3f waterLocation = new Vector3f(0, 4.5f, 0);
        waterProcessor.setPlane(new Plane(Vector3f.UNIT_Y, waterLocation.dot(Vector3f.UNIT_Y)));
        viewPort.addProcessor(waterProcessor);

        waterProcessor.setWaterDepth(40);         // transparency of water
        waterProcessor.setDistortionScale(0.05f); // strength of waves
        waterProcessor.setWaveSpeed(0.05f);       // speed of waves

        Quad quad = new Quad(400, 400);
        quad.scaleTextureCoordinates(new Vector2f(24f, 24f));

        Geometry water = new Geometry("water", quad);
        water.setLocalRotation(new Quaternion().fromAngleAxis(-FastMath.HALF_PI, Vector3f.UNIT_X));
        water.setLocalTranslation(-200, 4.5f, 250);
        water.setShadowMode(RenderQueue.ShadowMode.Receive);
        water.setMaterial(waterProcessor.getMaterial());

        port_node.attachChild(water);

        viewPort.addProcessor(waterProcessor);
        
        node.attachChild(port_node);
    }
    
}
