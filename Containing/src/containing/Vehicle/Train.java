/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package containing.Vehicle;

import containing.Platform.Platform;
import containing.Vector3f;
import java.util.Date;

/**
 *
 * @author Robert
 */
public class Train extends ExternVehicle{
 
    protected int timeCounter = 0;
    public static float width = 5f; //????????
    public static float length = 30f; //??????????
    private static int nrContainersDepth = 1;
    private static int nrContainersHeight = 1;
    private static int nrContainersWidth = 30;
   
    public Train(Date arrivalDate, float arrivalTime, Platform platform, String company)
    {

        super(arrivalDate, arrivalTime,  nrContainersDepth,nrContainersWidth,nrContainersHeight, platform, company, Type.TRAIN); //true if vehicle comes to load, otherwise false
        this.position = new Vector3f(1.4f,0f,0f);
        this.maxSpeedLoaded = 360;
        this.maxSpeedUnloaded = 360;

    }

    @Override
    public int getMaxSpeedUnloaded(){return 20;}
    @Override
    public int getMaxSpeedLoaded(){return 10;}
    
   
}
