package containing;

import java.sql.Timestamp;
import java.util.Date;

public class Settings 
{
    public static String version = "0.0.0.1";
    public static Port port;
    public static int ClockDelay = 100;
    public static MessageLog messageLog;
    public static UserInterface userInterface;
    public static Timestamp CurrentTime;
    
    public static long getTimeStamp(Date date, float from)
    {
        return date.getTime() + (long)((int)(from*3600) * 1000) + (long)((((from % 1) * 100) * 60) * 1000);
    }
}
