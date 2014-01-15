/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Simulator;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

/**
 * Abstract class containing main methods for models
 * @author Benjamin
 */
public abstract class Model
{
    Spatial model;
    Node node;
    
    /**
     * Creates new model instance
     * @param assetManager AssetManager for loading assets
     * @param path the path pointing to the modelfile
     * @param node the node to attach the model on
     */
    public Model(AssetManager assetManager, String path, Node node)
    {
        this.node = node;
        model = assetManager.loadModel(path);
    }
    
    /**
     * Creates new model instance with colored material
     * @param assetManager AssetManager for loading assets
     * @param path the path pointing to the modelfile
     * @param node the node to attach the model on
     * @param color the color for the model's material
     */
    public Model(AssetManager assetManager, String path, Node node, ColorRGBA color)
    {
        this.node = node;
        Material mat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        mat.setBoolean("UseMaterialColors", true);
        mat.setColor("Diffuse", color);
        mat.setColor("Specular", color);
        
        model = assetManager.loadModel(path);
        model.scale(0.1f);
        model.setMaterial(mat);
    }
    
    /**
     * Place the model on specified location
     * @param loc location to place model
     */
    private void place(Vector3f loc)
    {
        Spatial model_cur = model;
        model_cur.setLocalTranslation(loc);
        node.attachChild(model_cur);
    }
    
    /**
     * Place the model on specified location
     * @param x x value of position
     * @param y y value of position
     * @param z z value of position
     */
    public void place(float x, float y, float z)
    {
        place(new Vector3f(x,y,z));
    }
    
    /**
     * Place the model on default location, Vector3f.ZERO
     */
    public void place()
    {
        place(Vector3f.ZERO);
    }
    
    /**
     * Move the model to specified offset
     * @param offset offset to move the model
     */
    public void move(Vector3f offset)
    {
        model.move(offset);
    }
    
    /**
     * Move te model to specified offset
     * @param x x value to move
     * @param y y value to move
     * @param z z value to move
     */
    public void move(float x, float y, float z)
    {
        model.move(x,y,z);
        //move(new Vector3f(x,y,z));
    }
    
    /**
     * Scale the model with a factor
     * @param factor the factor to scale the model
     */
    public void scale(float factor)
    {
        model.scale(factor);
    }
    
    /**
     * Rotate the model 
     * @param xAngle x-angle to rotate
     * @param yAngle y-angle to rotate
     * @param zAngle z-angle to rotate
     */
    public void rotate(float xAngle, float yAngle, float zAngle)
    {
        model.rotate(xAngle, yAngle, zAngle);
    }
    
    /**
     * The model update method
     */
    public void update()
    {
        
    }
    
}
