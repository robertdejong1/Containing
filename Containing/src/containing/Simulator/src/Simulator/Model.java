/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Simulator;

import com.jme3.asset.AssetManager;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

/**
 *
 * @author Benjamin
 */
public abstract class Model
{
    AssetManager assetManager;
    Node model;
    
    public Model(AssetManager assetManager)
    {
        this.assetManager = assetManager;
    }
    
    public void init(String path, Vector3f loc)
    {
        
    }
    
    public void update()
    {
        
    }
    
}
