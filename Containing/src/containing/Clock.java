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
            timer.setDelay(delay);
            timer.start();
            return true;
        }
        catch (Exception e)
        {
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
            return false;
        }   
    }
    
    public boolean StopClock()
    {
        try
        {
            timer.stop();
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }
    
    private void TimerFunctions()
    {
        //Controller doet hier iets :D
    }
}
