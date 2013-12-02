package containing;

public class Dimension2f implements Serializable {
    
    public float width;
    public float length;
    
    public Dimension2f(float width, float length) 
    {
        this.width = width;
        this.length = length;
    }
    
    @Override
    public String toString()
    {
        return String.format("Dimension:[%.1f,%.1f]", width, length);   
    }
    
}
