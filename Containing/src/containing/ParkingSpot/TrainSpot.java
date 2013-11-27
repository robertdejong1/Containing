package containing.ParkingSpot;

import containing.Vector3f;

public class TrainSpot extends ParkingSpot 
{
    private final float LENGHT;
    private final float WIDTH;
    
    public TrainSpot(Vector3f position, Vector3f entryPoint) 
    {
        super(position, entryPoint);
        this.LENGHT = 10; //???
        this.WIDTH = 10; //???
    }

    public float getLENGHT() {
        return LENGHT;
    }

    public float getWIDTH() {
        return WIDTH;
    }
}
