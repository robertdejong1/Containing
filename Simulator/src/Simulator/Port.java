/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Simulator;

import com.jme3.asset.AssetManager;
import com.jme3.asset.TextureKey;
import com.jme3.material.Material;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture;

/**
 *
 * @author Benjamin
 */
public class Port
{
    Node port_node = new Node("port");
    //Spatial port;
    Spatial cranerail;
    Spatial trainrail;
    Spatial storage;
    Spatial road;
    
    public Port(AssetManager assetManager, Node node, ViewPort viewPort)
    {
        //port = assetManager.loadModel("Models/port.j3o");
        cranerail = assetManager.loadModel("Models/rail.j3o");
        trainrail = assetManager.loadModel("Models/trainrails.j3o");
        //storage = assetManager.loadModel("Models/storageplatform.j3o");
        //road = assetManager.loadModel("Models/road.j3o");
        
        Material port_mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        Texture tex2 = assetManager.loadTexture(new TextureKey("Textures/haven.jpg"));
        port_mat.setTexture("ColorMap", tex2);
        //((Geometry) port).getMesh().scaleTextureCoordinates(new Vector2f(1, 1));
        //port.setMaterial(stone_mat);
        
        //port.scale(10);
        //port.scale(1f, 0.2f, 1f);
        //port.move(0,4,0);
        
        Box port = new Box(817f, 10f, 1643f);
        Geometry port_geo = new Geometry("port", port);
        port_geo.setMaterial(port_mat);
        port_geo.move(817*0.05f,5f,1643*0.05f);
        port_geo.scale(0.05f);
        port_node.attachChild(port_geo);
              
        trainrail.scale(2);
        for (int i = 0; i < 14*5+4; i++)
        {
            trainrail.setLocalTranslation(1.4f, 5.5f, (10.4f/5)*i);
            port_node.attachChild(trainrail.clone());
        }
        
        //Water
        /*SimpleWaterProcessor waterProcessor = new SimpleWaterProcessor(assetManager);
        waterProcessor.setReflectionScene(port_node);

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
        water.setLocalTranslation(-200, 4.5f, 200);
        water.setShadowMode(RenderQueue.ShadowMode.Receive);
        water.setMaterial(waterProcessor.getMaterial());

        port_node.attachChild(water);

        viewPort.addProcessor(waterProcessor);*/
        
        node.attachChild(port_node);
    }
    
}
