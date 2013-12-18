/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Simulator;

import com.jme3.asset.AssetManager;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.util.ArrayList;
import java.util.List;


public class Train
{
    Node train;
    Node node;
    Spatial wag;
    int wagons;
    
    public List<Container> containers;
    
    public Train(AssetManager assetManager, Node node)
    {
        containers = new ArrayList<Container>();
        train = new Node();
        train.setName("Train");
        this.node = node;
        
        Spatial loco = assetManager.loadModel("Models/locomotive.j3o");
        wag = assetManager.loadModel("Models/trainwagon.j3o");
        
        wag.scale(0.2f);
        wag.scale(1.2f, 1, 1);
        
        loco.scale(0.17f);
        train.attachChild(loco);
    }
    
    public void addWagon(Container con)
    {
        wag.setLocalTranslation(0, 0, -1.5f*(wagons+1));
        train.attachChild(wag.clone());
        
        con.place(0, 0.23f, 0.1f + -1.5f*(wagons+1));
        train.attachChild(con.model);
        containers.add(con);
        
        wagons++;
    }
    
    private void place(Vector3f loc)
    {
        train.setLocalTranslation(loc);
        node.attachChild(train);
    }
    
    public void place(float x, float y, float z)
    {
        place(new Vector3f(x,y,z));
    }
    
    public void move(float x, float y, float z)
    {
        train.move(x, y, z);
    }
    
}
