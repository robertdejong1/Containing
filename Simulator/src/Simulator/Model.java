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
 *
 * @author Benjamin
 */
public abstract class Model
{
    Spatial model;
    private Node node;
    
    public Model(AssetManager assetManager, String path, Node node)
    {
        this.node = node;
        
        model = assetManager.loadModel(path);
    }
    
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
    
    private void place(Vector3f loc)
    {
        Spatial model_cur = model;
        model_cur.setLocalTranslation(loc);
        node.attachChild(model_cur);
    }
    
    public void place(float x, float y, float z)
    {
        place(new Vector3f(x,y,z));
    }
    
    public void place()
    {
        place(Vector3f.ZERO);
    }
    
    public void move(Vector3f offset)
    {
        model.move(offset);
    }
    
    public void move(float x, float y, float z)
    {
        model.move(x,y,z);
        //move(new Vector3f(x,y,z));
    }
    
    public void scale(float factor)
    {
        model.scale(factor);
    }
    
    public void rotate(float xAngle, float yAngle, float zAngle)
    {
        model.rotate(xAngle, yAngle, zAngle);
    }
    
    public void update()
    {
        
    }
    
}
