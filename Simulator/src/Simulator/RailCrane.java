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
    
    private float cableScale = 1f;
    
    public RailCrane(AssetManager assetManager, Node node)
    {
        this.node = node;
        crane = new Node();
        
        frame = assetManager.loadModel("Models/railcrane/frame.j3o");
        cable = assetManager.loadModel("Models/railcrane/cable.j3o");
        grab = assetManager.loadModel("Models/railcrane/grab.j3o");
        top = assetManager.loadModel("Models/railcrane/top.j3o");
        
        crane.attachChild(frame);
        crane.attachChild(cable);
        crane.attachChild(grab);
        crane.attachChild(top);
        
        cable.move(0, 0, -0.5f);
        grab.move(0, 0, -0.5f);
        top.move(0, 0, -0.5f);
        
        cable2 = cable.clone();
        crane.attachChild(cable2);
        cable3 = cable.clone();
        crane.attachChild(cable3);
        cable4 = cable.clone();
        crane.attachChild(cable4);
        cable5 = cable.clone();
        crane.attachChild(cable5);
        
        crane.rotate(0, 90*FastMath.DEG_TO_RAD, 0);
        crane.scale(0.3f);
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
    
    private void moveTop(float x)
    {
        crane.getChild(1).move(0, 0, x);
        crane.getChild(2).move(0, 0, x);
        crane.getChild(3).move(0, 0, x);
        crane.getChild(4).move(0, 0, x);
        crane.getChild(5).move(0, 0, x);
        crane.getChild(6).move(0, 0, x);
        crane.getChild(7).move(0, 0, x);
    }
    
    private void moveGrab(float y)
    {
        crane.getChild(2).move(0, y, 0);
        crane.getChild(1).move(0, y, 0);
        crane.getChild(4).move(0, 0, 0);
        crane.getChild(5).move(0, y/2, 0);
        crane.getChild(6).move(0, y/4, 0);
        crane.getChild(7).move(0, (y/4)*3, 0);
    }
    
    private int grabstate = 1;
    
    public void update(float tpf)
    {
        float x = crane.getChild(3).getLocalTranslation().z;
        float y = crane.getChild(2).getLocalTranslation().y;
        
        switch (grabstate)
        {
            case 1:
                if (y > -2.5f)
                {
                    moveGrab(-tpf);
                } else {
                    grabstate = 2;
                }
                break;
            
            case 2:
                if (y < 0)
                {
                    moveGrab(tpf);
                } else {
                    grabstate = 3;
                }
                break;
                
            case 3:
                if (x < 4.5f)
                {
                    moveTop(tpf);
                } else {
                    grabstate = 4;
                }
                break;
            
            case 4:
                if (y > -2.5f)
                {
                    moveGrab(-tpf);
                } else {
                    grabstate = 5;
                }
                break;
            
            case 5:
                if (y < 0)
                {
                    moveGrab(tpf);
                } else {
                    grabstate = 6;
                }
                break;
           
            case 6:
                if (x > -0.5f)
                {
                    moveTop(-tpf);
                } else {
                    grabstate = 1;
                }
                break;    
        } 
    }
            

}
