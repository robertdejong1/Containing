/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Simulator;

import com.jme3.asset.AssetManager;
import com.jme3.scene.Node;


public class Truck extends Model
{
    int id;
    Container c;
    
    public Truck(AssetManager assetManager, Node node, int id)
    {
        super(assetManager, "Models/truck.j3o", node);
        super.model.setName("Truck " + id);
        super.model.scale(1, 1, 0.8f);
        this.id = id;
    }

    public void addContainer(Container cont) {
        this.c = cont;
        super.node.attachChild(cont.model);
        cont.model.move(super.model.getLocalTranslation().clone().setY(5.75f));
        cont.model.move(0, 0, 0.17f);
    }
    
}
