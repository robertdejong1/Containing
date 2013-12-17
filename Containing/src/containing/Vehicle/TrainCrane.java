/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package containing.Vehicle;

import containing.Command;
import containing.CommandHandler;
import containing.Platform.Platform;
import containing.Vector3f;
import java.util.HashMap;

/**
 *
 * @author Robert
 */
public class TrainCrane extends Crane {

    public static float width = 5f; //????????
    public static float length = 6f; //??????????
    //hier TrainCrane specific variables
   
    public TrainCrane(Vector3f startPosition, Platform platform){ //variabelen doorgeven aan constructor crane
        super(startPosition, platform, Type.TRAINCRANE, width, length);
        this.maxSpeedLoaded = 50;
        this.maxSpeedUnloaded = 40;

    }
    
    @Override
    public void update()
    {
       
    }
    
  
    
  

    
    
    
    @Override
    public int getMaxSpeedUnloaded(){return 3;}
    @Override
    public int getMaxSpeedLoaded(){return 2;}

}
