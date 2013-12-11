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
    private static int nrContainersWidth = 25;
   
    public Train(Date arrivalDate, float arrivalTime, Platform platform, String company)
    {

        super(arrivalDate, arrivalTime,  nrContainersDepth,nrContainersWidth,nrContainersHeight, platform, company, Type.TRAIN); //true if vehicle comes to load, otherwise false
        this.position = new Vector3f(-41.5f, 5.5f, -82.15f);
        this.maxSpeedLoaded = 80;
        this.maxSpeedUnloaded = 70;

    }

    @Override
    public int getMaxSpeedUnloaded(){return 20;}
    @Override
    public int getMaxSpeedLoaded(){return 10;}
    
   
}
