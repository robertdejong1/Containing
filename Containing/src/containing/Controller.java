package containing;

import static containing.Container.TransportType.Seaship;
import containing.Vehicle.Vehicle;
import java.io.File;
import java.util.Date;
import java.util.List;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class Controller 
{

    private static UserInterface UserInterface;
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
    
    private static void sendATVToPlatform()
    {
        
    }
    
    public static void AddCommand(String Command)
    {
        
        
    }
    
    public static List<String> getNewCommands()
    {
        return null;
    }
    
    
    
}
