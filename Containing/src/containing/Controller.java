package containing;

import static containing.Container.TransportType.Seaship;
import containing.Vehicle.Vehicle;
import java.io.File;
import java.util.Date;
import java.util.List;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class Controller {

    private static UserInterface UserInterface;
    private static Clock clock; 
    
    public static void main(String[] args) 
    {
       UserInterface = new UserInterface(); 
       clock = new Clock();
    }
    
    //Errors in deze functie ivm niet geimplementeerde functies! voor nu zijn ze weggecomment
    public static void ReadXMLAndSortOutput(File XMLFile)
    {
        XmlHandler xmlHandler = new XmlHandler();
        List<Container> ContainersFromXML = xmlHandler.openXml(XMLFile);
        
        //Create new Port
        BuildPort();
        
        //Sort Containers into Vehicles
        for (int i = 0; i < ContainersFromXML.size(); i++)
        {
            Date ArrivalDateContainer = ContainersFromXML.get(i).getArrivalDate();
            float ArrivalTimeFromContainer = ContainersFromXML.get(i).getArrivalTimeFrom();
            Vehicle VehicleWithMatchingDateAndTime = null;
            
            /*
            //Search List of arriving vehicles for a match on arrivalDate & time
            for (int j = 0; j < Settings.scheduledVehicles.size(); j++)
            {
                if (
                        (Settings.scheduledVehicles.get(j).GetArrivalDate() == ArrivalDateContainer) 
                        && 
                        (Settings.scheduledVehicles.get(j).GetArrivalTimeFrom == ArrivalTimeFromContainer)
                    )
                {
                    VehicleWithMatchingDateAndTime = Settings.scheduledVehicles.get(j);
                    break;
                }
            }
            
            //Add container to the matching vehicle or create a new vehicle
            if (VehicleWithMatchingDateAndTime != null)
            {
                //Catch error if vehicle happens to be full
                try
                {
                    VehicleWithMatchingDateAndTime.AddContainer(ContainersFromXML.get(i));
                    //Write to log
                }
                catch (Exception e)
                {
                    ErrorLog.logMsg("Vehicle has no room for this container.", e);
                }
            }
            else
            {
                Vehicle NewVehicle = CreateNewVehicle(ContainersFromXML.get(i).getArrivalTransport());
                NewVehicle.AddContainer(ContainersFromXML.get(i));
                Settings.scheduledVehicles.add(NewVehicle);
                //Write to log
            }
            * */
            
            UserInterface.StartSimulationButton.setEnabled(true);
            
            
        }
        
        
    }
    
    private static void BuildPort()
    {
        Settings.port = new Port();
    }
    
    private static Vehicle CreateNewVehicle(Container.TransportType TypeofVehicle)
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
    
    public static void SetSimulationStatus(boolean Status)
    {
        
        
    }
    
    
    
}
