package containing.ParkingSpot;

import containing.Vector3f;

public class BargeSpot extends ParkingSpot 
{
    private final float LENGHT;
    private final float WIDTH;

    public BargeSpot(Vector3f position, Vector3f entryPoint) 
    {
        super(position, entryPoint);
        this.LENGHT = 10; //????
        this.WIDTH = 10; //????
    }

    public float getLENGHT() {
        return LENGHT;
    }

    public float getWIDTH() {
        return WIDTH;
    }
}
