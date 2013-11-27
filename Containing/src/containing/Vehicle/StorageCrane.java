/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package containing.Vehicle;

import containing.Container;
import containing.Vector3f;

/**
 *
 * @author Robert
 */
public class StorageCrane extends Crane {
    private static int counter = 0;
    private int id;
    private int currentRow;
    
    //hier StorageCrane specific variables
    public StorageCrane(){this(new Vector3f(0,0,0));} //om te testen zolang posities niet bekend zijn
    public StorageCrane(Vector3f startPosition){ //variabelen doorgeven aan constructor crane
        super(startPosition);
        id = counter;
        counter++;
    }
    
    public Container unload(){return super.unload();}
    
    public void load(Container container){super.load(container);}
    
    public void reset(){super.reset();}
    
    @Override
    public int getMaxSpeedUnloaded(){return 3;}
    @Override
    public int getMaxSpeedLoaded(){return 2;}
    
}
