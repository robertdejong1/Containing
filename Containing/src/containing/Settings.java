package containing;

import java.sql.Timestamp;
import java.util.Date;

/**
 * Class with static variables 
 */
public class Settings 
{
    public static String version = "0.0.0.1";
    public static Port port;
    public static int ClockDelay = 100;
    public static MessageLog messageLog;
    public static UserInterface userInterface;
    public static Timestamp CurrentTime;
    public final static float METER = 0.1f;
    
    /**
     * Calculates timestamp by given date and from-time of a container
     * @param date Date of container
     * @param from From-time of container
     * @return Timestamp
     */
    public static long getTimeStamp(Date date, float from)
    {
        return date.getTime() + (long)((int)(from*3600) * 1000) + (long)((((from % 1) * 100) * 60) * 1000);
    }
}
