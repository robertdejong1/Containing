package containing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;

public class Clock {

    private Timer timer;

    public Clock() 
    {
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

    private void TimerFunctions() 
    {
        Controller.update();
    }
    
}
