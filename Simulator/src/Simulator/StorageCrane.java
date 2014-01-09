/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Simulator;

import com.jme3.asset.AssetManager;
import com.jme3.cinematic.events.MotionEvent;
import com.jme3.cinematic.MotionPathListener;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;


public class StorageCrane
{
    int id;
    Node crane;
    Node node;
    Container con;
    
    Spatial frame;
    Spatial grab;
    Spatial top;
    
    public StorageCrane(AssetManager assetManager, Node node, int id)
    {
        this.node = node;
        crane = new Node();
        crane.setName("StorageCrane " + id);
        this.id = id;
        
        frame = assetManager.loadModel("Models/storagecrane/frame.j3o");
        grab = assetManager.loadModel("Models/storagecrane/grab.j3o");
        top = assetManager.loadModel("Models/storagecrane/top.j3o");
        
        crane.attachChild(frame);
        crane.attachChild(grab);
        crane.attachChild(top);

        crane.rotate(0, 90*FastMath.DEG_TO_RAD, 0);
        crane.scale(0.41f);
    }
    
    private void place(Vector3f loc)
    {
        crane.setLocalTranslation(loc);
        node.attachChild(crane);
    }
    
    public void place(float x, float y, float z)
    {
        place(new Vector3f(x,y,z));
    }
    
    public void moveCrane(float z)
    {
        crane.move(0, 0, z);
        
        if (occupied)
        {
            con.model.move(0, 0, z);
        }
    }
    
    public void moveTop(float x)
    {
        crane.getChild(1).move(x, 0, 0);
        crane.getChild(2).move(x, 0, 0);
    }
    
    public void moveGrab(float y)
    {
        crane.getChild(1).move(0, y, 0);
        
        if (occupied)
        {
            con.model.move(0, y*0.41f, 0);
        }
    }
    
    private boolean occupied = false;
    private int cranestate = 0;
    private int con_index = 0;
    private MotionEvent motev;
    private MotionEvent con_motev;
    
    public void loadCrane(Container con, int con_index, MotionEvent motev)
    {
        this.con = con;
        this.con_index = con_index;
        cranestate = 1;
        this.motev = motev;
        node.attachChild(con.model);
    }
    
    public void unloadCrane(MotionEvent motev, MotionEvent con_motev)
    {
        this.motev = motev;
        this.con_motev = con_motev;
        cranestate = 5;
        
    }
    
    public void update(float tpf)
    {
        float top_x = crane.getChild(2).getLocalTranslation().x;
        float grab_y = crane.getChild(1).getLocalTranslation().y;
        
        switch (cranestate)
        {
            case 0:
                //Default state
                break;
                
            case 1:
                //Move crane to parkingspot
                if (motev.getPath().getNbWayPoints() >= 2)
                {
                    if (motev.getPath().getWayPoint(0).equals(motev.getPath().getWayPoint(1)))
                    {
                        cranestate = 2;
                    } else {
                        motev.play();
                        
                        motev.getPath().addListener(new MotionPathListener() {
                            public void onWayPointReach(MotionEvent control, int wayPointIndex) {
                                 if (motev.getPath().getNbWayPoints() == wayPointIndex + 1) {
                                     cranestate = 2;
                                 }
                            }
                        });
                    }
                }
                break;
                
            case 2:
                //Move top to parkingspot
                float goto_top = 2.4f - con_index;
                if (goto_top < 0)
                {
                    if (top_x > goto_top)
                        moveTop(-tpf);
                    else
                        cranestate = 3;
                    break;
                } else {
                    if (top_x < goto_top)
                        moveTop(tpf);
                    else
                        cranestate = 3;
                    break;
                }
                
            case 3:
                //Move grab down
                if (grab_y > -3.9f)
                    moveGrab(-tpf);
                else
                    cranestate = 4;
                break;
            
            case 4:
                //Move grab up
                occupied = true;
                
                if (grab_y < 0)
                    moveGrab(tpf);
                else
                    cranestate = 0;          
                break;
                
            case 5:
                //Move to storageposition
                motev.play();
                con_motev.play();
                
                motev.getPath().addListener(new MotionPathListener() {
                    public void onWayPointReach(MotionEvent control, int wayPointIndex) {
                         if (motev.getPath().getNbWayPoints() == wayPointIndex + 1) {
                             cranestate = 6;
                         }
                    }
                });
                break;
                
            case 6:
                //Move grab with container down
                if (grab_y > -4.2f)
                    moveGrab(-tpf);
                else
                    cranestate = 7;
                break;
            
            case 7:
                occupied = false;
                this.con = null;
                
                if (grab_y < 0)
                    moveGrab(tpf);
                else
                    cranestate = 0;          
                break;
        }
    }
}
