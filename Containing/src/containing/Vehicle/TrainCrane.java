/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package containing.Vehicle;

import containing.Container;
import containing.Exceptions.CargoOutOfBoundsException;
import containing.Exceptions.VehicleOverflowException;
import containing.Platform.Platform;
import containing.Vector3f;

/**
 *
 * @author Robert
 */
public class TrainCrane extends Crane {
    private static int counter = 0;
    private int id;
    private int currentRow;
    public static float width = 5f; //????????
    public static float length = 6f; //??????????
    //hier TrainCrane specific variables
   
    public TrainCrane(Vector3f startPosition, Platform platform){ //variabelen doorgeven aan constructor crane
        super(startPosition, platform, Type.TRAINCRANE, width, length);
        id = counter;
        counter++;
    }
    
    public Container unload(){return super.unload();}
    
    public void load(Container container) throws VehicleOverflowException, CargoOutOfBoundsException
    {
        try{super.load(container);}
        catch(Exception e){throw e;}
    }
    
    public void reset(){super.reset();}
  
    public void move(int direction){ //-1 = left + 1 = rechts
        int starttime = this.timeCounter;
        while (this.timeCounter < this.metersToNextAgvSpot * this.maxSpeedUnloaded * 10){}
        this.startPosition.x = this.startPosition.x + direction * this.metersToNextAgvSpot;
    }
    
    @Override
    public int getMaxSpeedUnloaded(){return 3;}
    @Override
    public int getMaxSpeedLoaded(){return 2;}

}
