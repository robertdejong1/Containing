/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Simulator;

import com.jme3.asset.AssetManager;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;


public class Truck
{
    int id;
    Container c;
    Node node;
    Node truck;
    Spatial truck_model;
    
    public Truck(AssetManager assetManager, Node node, int id)
    {
        this.node = node;
        truck = new Node();
        
        truck_model = assetManager.loadModel("Models/truck.j3o");
        truck_model.scale(1, 1, 0.8f);
        truck.attachChild(truck_model);
        truck.setName("Truck " + id);

        this.id = id;
    }

    public void addContainer(Container cont) {
        this.c = cont;
        truck.attachChild(c.model);
        cont.model.move(0, 0.25f, 0.17f);
    }
    
    public void place(Vector3f loc)
    {
        truck.setLocalTranslation(loc);
        node.attachChild(truck);
    }
    
    public void place(float x, float y, float z)
    {
        truck.setLocalTranslation(new Vector3f(x,y,z));
        node.attachChild(truck);
    }
    
    public Container detachContainer()
    {
        truck.detachChild(c.model);
        return c;
    }
}
