package containing;

import containing.Exceptions.NoJobException;
import containing.Exceptions.ParseErrorException;
import containing.Platform.Platform;
import java.io.File;
import java.sql.Timestamp;
import java.util.List;

public class Controller 
{
    private static UserInterface UserInterface;
    private static Clock clock; 
    
    /**
     * Entry point for program
     * @param args 
     */
    public static void main(String[] args) 
    {
       UserInterface = new UserInterface(); 
       Settings.messageLog = new MessageLog();
       Settings.userInterface = UserInterface;
       
       //Create new Port
       Settings.port = new Port();
       
       Runnable networkHandler = new NetworkHandler(1337);
       Thread networkHandlerThread = new Thread(networkHandler);
       networkHandlerThread.start();
    }
   
    /**
     * reads xml and sorts output in a list of containing.containers
     * @param XMLFile 
     */
    public static void readXMLAndSortOutput(File XMLFile)
    {   
        XmlHandler xmlHandler = new XmlHandler();
        List<Container> ContainersFromXMList;
        
        try
        {
            ContainersFromXMList = xmlHandler.openXml(XMLFile);
            Controlleralgorithms.sortInCommingContainers(ContainersFromXMList);
            clock = new Clock(Controlleralgorithms.getFirstDate(ContainersFromXMList));
        }
        catch (ParseErrorException e)
        {
               Settings.messageLog.AddMessage("Could not parse xml!");
        }
    }
    
    /**
     * starts or stops the simulation
     * @param Status 
     */
    public static void setSimulationStatus(boolean Status)
    {
        if (Status)
        {
            clock.StartClock(Settings.ClockDelay);
        }
        else
        {
            clock.StopClock();
        }
        
    }
    
    /**
     * updates the simulation
     * @param timestamp 
     */
    public static void update(Timestamp timestamp)
    {
        Settings.CurrentTime = timestamp;
        Settings.port.update();
        Controlleralgorithms.checkIncomingVehicles(timestamp);
        UserInterface.setTitle("Containing 2013 - " + timestamp.toString());
    }
    
    /**
     * adds container to outgoing containers
     * @param container 
     */
    public static void sortOutgoingContainer(Container container)
    {
        Controlleralgorithms.sortOutgoingContainer(container);
    }
    
    public static boolean RequestNextContainer(Container container, Platform requestingPlatform)
    {
        if (Settings.port.getStoragePlatform().hasContainer(container))
        {
            //Route route = new Route(requestingPlatform);
            //Settings.port.getStoragePlatform().loadContainerInAgv(container, requestingPlatform); 
            return true;
        }
        else
        {
            return false;
        }
    }
    
    /**
     * requests next job for a platform
     * @param platform
     * @returns job object
     * @throws NoJobException 
     */
    public static Job RequestNewJob(Platform platform) throws NoJobException
    {
        try
        {
            return Controlleralgorithms.getNextJob(platform, Settings.CurrentTime);
        }
        catch (NoJobException e)
        {
            throw e;
        }
    }
    
    /**
     * updates messsagelog
     */
    public static void updateUserInterface()
    {
        try
        {
            UserInterface.MessageLogTextArea.setText(Settings.messageLog.GetLastMessages() +UserInterface.MessageLogTextArea.getText());
            Settings.messageLog.ClearMessages();
        }
        catch (NullPointerException e)
        {
            //unit test
        }
    }
}
