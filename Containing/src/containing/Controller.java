package containing;

import static containing.Container.TransportType.Seaship;
import containing.Platform.Platform;
import containing.Vehicle.AGV;
import containing.Vehicle.Vehicle;
import java.io.File;
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
       
       Runnable networkHandler = new NetworkHandler(Settings.port);
       Thread networkHandlerThread = new Thread(networkHandler);
       networkHandlerThread.start();
    }
    
    //Errors in deze functie ivm niet geimplementeerde functies! voor nu zijn ze weggecomment
    public static void ReadXMLAndSortOutput(File XMLFile)
    {
        //Create new Port
        buildPort();
        
        XmlHandler xmlHandler = new XmlHandler();
        ControllerAlgoritmes.SortInCommingContainers(xmlHandler.openXml(XMLFile), UserInterface);
    }
    
    private static void buildPort()
    {
        Settings.port = new Port();
        Settings.messageLog.AddMessage("Created Harbor Object: " + Settings.port.toString());
    }
    
    private static Vehicle createNewVehicle(Container.TransportType TypeofVehicle)
    {
        Vehicle VehicleToReturn = null;
        
        switch (TypeofVehicle)
        {
            case Truck:
                //VehicleToReturn = new Truck();
                break;
            case Train:
                //VehicleToReturn = new Train();
                break;    
            case Barge:
                //VehicleToReturn = new Barge();
                break;
            case Seaship:
                //VehicleToReturn = new Seaship();
                break;
        }
        
        return VehicleToReturn;
    }
    
    public static void setSimulationStatus(boolean Status)
    {
        if (Status)
        {
            clock.StartClock(1000);
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
    
    public static void update()
    {
        System.out.println("test ;]");
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
    
    public static void requestNextContainer(Platform requestingPlatform)
    {
        
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
