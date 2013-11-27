package containing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Calendar;
import javax.swing.Timer;

public class Clock 
{
    private int delay;
    private Timer timer;
    private Timestamp currentDateAndTime;

    public Clock() 
    {
        this.currentDateAndTime = new Timestamp(System.currentTimeMillis());
        
        timer = new Timer(100, new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                TimerFunctions();
            }
        });

        timer.setRepeats(true);
    }

    public boolean StartClock(int delay) 
    {
        this.delay = delay;
        
        try 
        {
            if (!timer.isRunning()) 
            {
                timer.setDelay(delay);
                timer.start();
                return true;
            } 
            else 
            {
                return false;
            }
        } 
        catch (Exception e) 
        {
            ErrorLog.logMsg("Could not start the clock", e);
            return false;
        }
    }

    public boolean SetDelay(int delay) 
    {
        this.delay = delay;
        
        try 
        {
            timer.setDelay(delay);
            timer.stop();
            timer.start();
            return true;
        } 
        catch (Exception e) 
        {
            ErrorLog.logMsg("Could not set the delay of the clock", e);
            return false;
        }
    }

    public boolean StopClock() 
    {
        try 
        {
            if (!timer.isRunning()) 
            {
                timer.stop();
                return true;
            } else {
                return false;
            }
        } 
        catch (Exception e) 
        {
            ErrorLog.logMsg("Could not stop the clock", e);
            return false;
        }
    }

    public Timestamp getCurrentDateAndTime() 
    {
        return currentDateAndTime;
    }

    private void TimerFunctions() 
    {
        System.out.println(currentDateAndTime + " test");
        
        //SetTimeStamp
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(currentDateAndTime.getTime());
        cal.add(Calendar.MILLISECOND, delay);
        currentDateAndTime = new Timestamp(cal.getTime().getTime());
        
        //UpdateController
        Controller.update(currentDateAndTime);
    }
}
