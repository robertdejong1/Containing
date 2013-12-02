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
import java.util.Date;

/**
 *
 * @author Robert
 */
public class Train extends ExternVehicle{
    private static int counter;
    private int trainID;
    static int capicity = 10000;
    protected int timeCounter = 0;
    public static float width = 5f; //????????
    public static float length = 30f; //??????????
    private static int nrContainersDepth = 2;
    private static int nrContainersHeight = 1;
    private static int nrContainersWidth = 1;
    public Train(Date arrivalDate, float arrivalTime, Platform platform, String company)
    {

        super(arrivalDate, arrivalTime,  nrContainersDepth,nrContainersHeight,nrContainersWidth, platform, company, Type.TRAIN); //true if vehicle comes to load, otherwise false
        
        trainID = counter;
        counter++;
    }

    @Override
    public int getMaxSpeedUnloaded(){return 20;}
    @Override
    public int getMaxSpeedLoaded(){return 10;}
    
     public void update(){
        timeCounter++;
    }
}
