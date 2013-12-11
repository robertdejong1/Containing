package containing;

import containing.Exceptions.NoJobException;
import containing.Exceptions.ParseErrorException;
import containing.Platform.Platform;
import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class Controller 
{
    private static UserInterface UserInterface;
    private static List<Command> QeuedCommands;
    private static Clock clock; 
    public static boolean started = false;
    
    public static void main(String[] args) 
    {
       UserInterface = new UserInterface(); 
       Settings.messageLog = new MessageLog();
       Settings.userInterface = UserInterface;
       
       //Create new Port
       buildPort();
       
       Runnable networkHandler = new NetworkHandler(1337);
       Thread networkHandlerThread = new Thread(networkHandler);
       networkHandlerThread.start();
    }
   
    public static void ReadXMLAndSortOutput(File XMLFile)
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
        
        started = false;
    }
    
    private static void buildPort()
    {
        Settings.port = new Port();
    }
    
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
    
    public static void update(Timestamp timestamp)
    {
        Settings.CurrentTime = timestamp;
        Settings.port.update();
        Controlleralgorithms.checkIncomingVehicles(timestamp);
        UserInterface.setTitle("Containing 2013 - " + timestamp.toString());
        UserInterface.MessageLogLabel.setText(Settings.messageLog.GetLastMessagesAsHTMLString());
    }
    
    public static void addCommand(Command command)
    {
        if (QeuedCommands == null)
        {
            QeuedCommands = new ArrayList<>();
        }
        
        QeuedCommands.add(command);
        Settings.messageLog.AddMessage("Server: " + command.getCommand());
    }
    
    public static List<Command> getNewCommands()
    {
        List<Command> CommandsToReturn = QeuedCommands;
        QeuedCommands = new ArrayList<>();
        Settings.messageLog.AddMessage("Sending Commandqeue to NetworkHandler."); //Tijdelijk om te zien of de functie correct wordt aangeroepen door networkHandler.
        return CommandsToReturn;
    }
    
    public static void sortOutgoingContainer(Container container)
    {
        Controlleralgorithms.sortOutgoingContainer(container);
    }
    
    public static boolean RequestNextContainer(Container container, Platform requestingPlatform)
    {
        if (Settings.port.getStoragePlatform().hasContainer(container))
        {
            //Route route = new Route(requestingPlatform);
            //Settings.port.getStoragePlatform().loadContainerInAgv(); // Moet route meegeven maar ik snap eigenlijk niet hoe ik dat moet doen :D
            return true;
        }
        else
        {
            return false;
        }
    }
    
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
    
    public static void updateUserInterface()
    {
        UserInterface.MessageLogLabel.setText(Settings.messageLog.GetLastMessagesAsHTMLString());
    }
}
