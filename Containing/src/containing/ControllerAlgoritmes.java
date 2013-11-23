package containing;

import containing.Vehicle.Vehicle;
import java.util.Date;
import java.util.List;

class ControllerAlgoritmes 
{
    public static void SortInCommingContainers(List<Container> ContainersFromXML, UserInterface UserInterface)
    {
        if (ContainersFromXML != null)
        {
            Settings.messageLog.AddMessage("Created " + ContainersFromXML.size() + " Containers from xml file");
            
            //Sort Containers into Vehicles
            for (int i = 0; i < ContainersFromXML.size(); i++)
            {
                Date ArrivalDateContainer = ContainersFromXML.get(i).getArrivalDate();
                float ArrivalTimeFromContainer = ContainersFromXML.get(i).getArrivalTimeFrom();
                Vehicle VehicleWithMatchingDateAndTime = null;
                
                /*
                //Search List of arriving vehicles for a match on arrivalDate & time
                for (int j = 0; j < Settings.scheduledArrivingVehicles.size(); j++)
                {
                    if (
                            (Settings.scheduledArrivingVehicles.get(j).GetArrivalDate() == ArrivalDateContainer) 
                            && 
                            (Settings.scheduledArrivingVehicles.get(j).GetArrivalTimeFrom == ArrivalTimeFromContainer)
                        )
                    {
                        VehicleWithMatchingDateAndTime = Settings.scheduledArrivingVehicles.get(j);
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
                        Settings.messageLog.AddMessage("Added " + ContainersFromXML.get(i).toString() + " to " + VehicleWithMatchingDateAndTime.toString());
                    }
                    catch (Exception e)
                    {
                        ErrorLog.logMsg("Vehicle has no room for this container.", e);
                    }
                }
                else
                {
                    Vehicle NewVehicle = CreateNewVehicle(ContainersFromXML.get(i).getArrivalTransport());
                    Settings.messageLog.AddMessage("Created: " + VehicleWithMatchingDateAndTime.toString());
                    NewVehicle.AddContainer(ContainersFromXML.get(i));
                    Settings.scheduledArrivingVehicles.add(NewVehicle);
                    Settings.messageLog.AddMessage("Added " + ContainersFromXML.get(i).toString() + " to " + VehicleWithMatchingDateAndTime.toString());
                }
                */
            }
            
            UserInterface.StartSimulationButton.setEnabled(true);
        }
        else
        {
            Settings.messageLog.AddMessage("File is emtpy or not a valid xml file");
        }
        
    }
    
    public static void SortOutgoingCOntainers(List<Container> ContainersFromXML, UserInterface UserInterface)
    {
        
    }
}
