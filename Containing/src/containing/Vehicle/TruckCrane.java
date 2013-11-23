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
public class TruckCrane extends Crane {
    private static int counter = 0;
    private int id;
    private int currentRow;
    
    //hier TruckCrane specific variables
    public TruckCrane(){this(new Vector3f(0,0,0));} //om te testen zolang posities niet bekend zijn
    public TruckCrane(Vector3f startPosition){ //variabelen doorgeven aan constructor crane
        super(startPosition);
        id = counter;
        counter++;
    }
    
    public Container unload(){return super.unload();}
    
    public void load(Container container){super.load(container);}
    
    public void reset(){super.reset();}

}
