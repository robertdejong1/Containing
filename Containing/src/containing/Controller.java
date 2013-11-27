package containing;

import containing.Platform.Platform;
import containing.Vehicle.AGV;
import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class Controller 
{
    private static UserInterface UserInterface;
    private static List<Command> QeuedCommands;
    private static Clock clock; 
    
    public static void main(String[] args) 
    {
       UserInterface = new UserInterface(); 
       clock = new Clock();
       Settings.messageLog = new MessageLog();
       updateMessageLogWindow();
       
       Runnable networkHandler = new NetworkHandler(1337);
       Thread networkHandlerThread = new Thread(networkHandler);
       networkHandlerThread.start();
    }
   
    public static void ReadXMLAndSortOutput(File XMLFile)
    {
        //Create new Port
        buildPort();
        
        XmlHandler xmlHandler = new XmlHandler();
        ControllerAlgoritmes.sortInCommingContainers(xmlHandler.openXml(XMLFile), UserInterface);
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
    
    public static void updateMessageLogWindow()
    {
        UserInterface.MessageLogLabel.setText(Settings.messageLog.GetLastMessagesAsHTMLString());
    }
    
    public static void update(Timestamp timestamp)
    {
        Settings.port.update();
        ControllerAlgoritmes.checkIncomingVehicles(timestamp);
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
    
    public static void AddContainerTOJobQeue(Container container)
    {
        ControllerAlgoritmes.sortOutgoingContainer(container);
    }
    
    public static boolean RequestNextContainer(Container container, Platform requestingPlatform)
    {
        if (Settings.port.getStoragePlatform().isContainerHere(container))
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
                
    public static void requestNextJobForLoadedAGV(AGV agv)
    {
        //kijken waar container heen moet
        //route aanmaken
        //route naar agv sturen
        agv.followRoute(null);
    }
    
    public static boolean requestIdleAGV(Platform RequestingPlatform)
    {
        //kijk lijst na op idle agv's
        //kijk welke de dichstbijzijnde is
        //geef agv de route 
        //geef agv door voor welke container hij komt???? bespreken met groep
        
        return false;
    }
}
