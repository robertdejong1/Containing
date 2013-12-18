/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Simulator;

import com.jme3.asset.AssetManager;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;


public class RailCrane
{
    public int id;
    
    Node crane;
    Node node;
    
    Spatial frame;
    Spatial cable;
    Spatial cable2;
    Spatial cable3;
    Spatial cable4;
    Spatial cable5;
    Spatial grab;
    Spatial top;
    
    public RailCrane(AssetManager assetManager, Node node, int id)
    {
        this.node = node;
        this.id = id;
        crane = new Node();
        crane.setName("RailCrane " + id);
        
        frame = assetManager.loadModel("Models/railcrane/frame.j3o");
        cable = assetManager.loadModel("Models/railcrane/cable.j3o");
        grab = assetManager.loadModel("Models/railcrane/grab.j3o");
        top = assetManager.loadModel("Models/railcrane/top.j3o");
        
        crane.attachChild(frame);
        crane.attachChild(cable);
        crane.attachChild(grab);
        crane.attachChild(top);
        
        cable.move(0, 0, -0.55f);
        grab.move(0, 0, -0.55f);
        top.move(0, 0, -0.55f);
        
        cable2 = cable.clone();
        crane.attachChild(cable2);
        cable3 = cable.clone();
        crane.attachChild(cable3);
        cable4 = cable.clone();
        crane.attachChild(cable4);
        cable5 = cable.clone();
        crane.attachChild(cable5);
        
        crane.rotate(0, 90*FastMath.DEG_TO_RAD, 0);
        crane.scale(0.33f);
        crane.scale(1f, 1f, 1.1f);
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
    
    private void moveCrane(float z)
    {
        crane.move(0, 0, z);
    }
    
    private void moveTop(float x)
    {
        crane.getChild(1).move(0, 0, x);
        crane.getChild(2).move(0, 0, x);
        crane.getChild(3).move(0, 0, x);
        crane.getChild(4).move(0, 0, x);
        crane.getChild(5).move(0, 0, x);
        crane.getChild(6).move(0, 0, x);
        crane.getChild(7).move(0, 0, x);
        
        //if (occupied)
        //{
           // containers[con_index].move(x/3.01f, 0, 0);
        //}
    }
    
    private void moveGrab(float y)
    {
        crane.getChild(2).move(0, y, 0);
        crane.getChild(1).move(0, y, 0);
        crane.getChild(4).move(0, 0, 0);
        crane.getChild(5).move(0, y/2, 0);
        crane.getChild(6).move(0, y/4, 0);
        crane.getChild(7).move(0, (y/4)*3f, 0);
        
        //if (occupied)
        //{
            //containers[con_index].move(0, y/3f, 0);
        //}
    }
    
    public void attachContainer(Container[] con)
    {
        //this.containers = con;
    }
    
    private int grabstate = 1;
    private boolean occupied = false;
    private int con_index = 0;
    
    public void update(float tpf)
    {
        
        float x = crane.getChild(3).getLocalTranslation().z;
        float y = crane.getChild(2).getLocalTranslation().y;
        float z = crane.getLocalTranslation().z;
        
        switch (grabstate)
        {
            //grab down
            case 1:
                if (y > -2.5f)
                {
                    moveGrab(-tpf);
                } else {
                    grabstate = 2;
                }
                break;
                
            //attach container    
            case 2:
                grabstate = 3;
                occupied = true;
                break;
            
            //grab up
            case 3:
                if (y < 0)
                {
                    moveGrab(tpf);
                } else {
                    grabstate = 4;
                }
                break;
                
            //move top to side
            case 4:
                if (x < 4.5f)
                {
                    moveTop(tpf);
                } else {
                    grabstate = 5;
                }
                break;
            
            //grab down
            case 5:
                if (y > -2.25f)
                {
                    moveGrab(-tpf);
                } else {
                    grabstate = 6;
                }
                break;
            
            //dettach container    
            case 6:
                grabstate = 7;
                occupied = false;
                break;   
             
            //grab up
            case 7:
                if (y < 0)
                {
                    moveGrab(tpf);
                } else {
                    grabstate = 8;
                }
                break;
           
            //return top to default location
            case 8:
                if (x > -0.5f)
                {
                    moveTop(-tpf);
                } else {
                    grabstate = 9;
                }
                break; 
                
            //next container
            case 9:
                if (z > -1.5f*(con_index+2))
                {
                    moveCrane(-tpf);
                } else {
                    /*if (con_index >= containers.length-1)
                    {
                        grabstate = 10;
                    }
                    else
                    {
                        con_index++;
                        grabstate = 1;
                    }*/
                }
                break;
                
            //ready
            case 10:
                break;
        }

    }
            

}
