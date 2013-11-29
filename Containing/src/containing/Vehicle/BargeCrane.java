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
public class BargeCrane extends Crane {
    private static int counter = 0;
    private int id;
    private int currentRow;
    public static float width = 5f; //????????
    public static float length = 6f; //??????????
    /*
    private final int liftTimeMin = 0;
    private final float liftTimeMax = 3.5f * 60;
    private final float dropTimeMin = 0;
    private final float dropTimeMax = 3.5f * 60;*/
    
    
    
    public BargeCrane(Vector3f startPosition, Platform platform){ //variabelen doorgeven aan constructor crane
        super(startPosition, platform, Type.BARGECRANE, width, length);
        id = counter;
        counter++;
    }
    
    public void reset(){super.reset();}
  
    public void move(int direction){ //-1 = left + 1 = rechts
        int starttime = this.timeCounter;
        while (this.timeCounter < starttime + this.metersToNextAgvSpot * this.maxSpeedUnloaded){}
        this.startPosition.x = this.startPosition.x + direction * this.metersToNextAgvSpot;
    }
    
    @Override
    public int getMaxSpeedUnloaded(){return 8;}
    @Override
    public int getMaxSpeedLoaded(){return 3;}
    
    
}
