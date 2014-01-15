/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Simulator;

import com.jme3.asset.AssetManager;
import com.jme3.math.FastMath;
import com.jme3.scene.Node;


public class FreeCrane extends Model
{
    public int id; 
    Container con;
    boolean occupied = false;
    int loadstate = 0;
    
    public FreeCrane(AssetManager assetManager, Node node, int id)
    {
        super(assetManager, "Models/freecrane.j3o", node);
        super.model.setName("FreeCrane " + id);
        super.model.scale(0.5f);
        super.model.rotate(0, -90*FastMath.DEG_TO_RAD, 0);
        this.id = id;
    }
    
    public void loadCrane(Container con)
    {
        this.con = con;
        loadstate = 1;
        occupied = true;
    }
    
    public Container unloadCrane()
    {
        loadstate = 3;
        return this.con;
    }
    
    public void update(float tpf)
    {
        switch (loadstate)
        {
            case 0:
                //Doe niks
                break;
            
            case 1:
                //System.out.println("Y: " + con.model.getLocalTranslation().y);
                con.move(0, tpf, 0);
                if (con.model.getLocalTranslation().y > 8.2f)
                {
                    loadstate = 2;
                }
                break;
                
            case 2:
                con.move(-tpf, 0, 0);
                if (con.model.getLocalTranslation().x < this.model.getLocalTranslation().x - 1f)
                {
                    loadstate = 0;
                }
                break;
                
            case 3:
                con.move(0, -tpf, 0); 
                if (con.model.getLocalTranslation().y < 5.81f)
                {
                    loadstate = 4;
                }
                break;
                
            case 4:
                this.con = null;
                occupied = false;
                loadstate = 0;
                break;
        }
    }
    
}
