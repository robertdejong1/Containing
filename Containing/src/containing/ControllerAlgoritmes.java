package containing;

import static containing.Container.TransportType.Barge;
import static containing.Container.TransportType.Seaship;
import static containing.Container.TransportType.Train;
import static containing.Container.TransportType.Truck;
import containing.Platform.Platform;
import containing.Vehicle.Barge;
import containing.Vehicle.ExternVehicle;
import containing.Vehicle.Seaship;
import containing.Vehicle.Train;
import containing.Vehicle.Truck;
import containing.Vehicle.Vehicle;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Stack;
import java.util.Calendar;

class ControllerAlgoritmes 
{
    private static List<Job> JobQeueUnsorted;
    private static Stack<Job> JobQeueSorted;
    private static List<ExternVehicle> scheduledArrivingVehicles;
    
    public static void sortInCommingContainers(List<Container> ContainersFromXML, UserInterface UserInterface)
    {
        scheduledArrivingVehicles = new ArrayList<>();
        
        if (ContainersFromXML != null)
        {
            Settings.messageLog.AddMessage("Created " + ContainersFromXML.size() + " Containers from xml file");
            
            //Sort Containers into Vehicles
            for (int i = 0; i < ContainersFromXML.size(); i++)
            {
                Date ArrivalDateContainer = ContainersFromXML.get(i).getArrivalDate();
                float ArrivalTimeFromContainer = ContainersFromXML.get(i).getArrivalTimeFrom();
                Vehicle VehicleWithMatchingDateAndTime = null;
                
                //Search List of arriving vehicles for a match on arrivalDate & time
                for (int j = 0; j < scheduledArrivingVehicles.size(); j++)
                {
                    if (
                            (scheduledArrivingVehicles.get(j).getArrivalDate().equals(ArrivalDateContainer))
                            && 
                            (scheduledArrivingVehicles.get(j).getArrivalTime() == ArrivalTimeFromContainer)
                        )
                    {
                        VehicleWithMatchingDateAndTime = scheduledArrivingVehicles.get(j);
                        break;
                    }
                }
            
                //Add container to the matching vehicle or create a new vehicle
                if (VehicleWithMatchingDateAndTime != null)
                {
                    //Catch error if vehicle happens to be full
                    try
                    {
                        VehicleWithMatchingDateAndTime.load(ContainersFromXML.get(i));
                    }
                    catch (Exception e)
                    {
                        ErrorLog.logMsg("Vehicle has no room for this container.", e);
                    }
                }
                else
                {
                    ExternVehicle NewVehicle = createNewVehicle(ContainersFromXML.get(i).getArrivalTransport(), ContainersFromXML.get(i).getArrivalDate(), ContainersFromXML.get(i).getArrivalTimeFrom());
                    Settings.messageLog.AddMessage("Created new " + ContainersFromXML.get(i).getArrivalTransport()); // : " + VehicleWithMatchingDateAndTime.toString());
                    NewVehicle.load(ContainersFromXML.get(i));
                    scheduledArrivingVehicles.add(NewVehicle);
                }
                
            }
            
            UserInterface.StartSimulationButton.setEnabled(true);
        }
        else
        {
            Settings.messageLog.AddMessage("File is emtpy or not a valid xml file");
        }
        
    }
    
    /*
     * Search if job exists with same timestamp
     * true? add container to job
     * false? create new job
     * Sort List of jobs
     */
    public static void sortOutgoingContainer(Container UnloadedContainer)
    {
        boolean SuitingJobFound = false;
        
        for (Job j : JobQeueUnsorted)
        {
            if (j.getDate().equals(UnloadedContainer.getDepartureDate()))
            {
                SuitingJobFound = true;
                j.addContainer(UnloadedContainer);
                break;
            }
        }
        
        if (!SuitingJobFound)
        {
            Job job = new Job(
                    UnloadedContainer.getDepartureDate(), 
                    UnloadedContainer.getDepartureTimeFrom(), 
                    UnloadedContainer.getDepartureTransport());
            job.addContainer(UnloadedContainer);
             JobQeueUnsorted.add(job);
        }
        
        JobQeueSorted = Job.sortOutGoingJobs(JobQeueUnsorted);
    }
    
   /*
    *Removes job from the JobQeue
    *Checks if there is already a docked vehicle waiting
    *if not create a new vehicle
    *return job
    */ 
    public static Job getNextJob(List<ExternVehicle> externalVehicles) //intern bijhouden
    {
        //Get job and remove from qeue
        Job job = JobQeueSorted.pop();
        JobQeueUnsorted.remove(job);
        
        //Check if there is an existing vehicle
        for (ExternVehicle v : externalVehicles)
        {
            if (v.getClass().equals(
                    createNewVehicle(
                        job.getVehicleType(), 
                        job.getDate(), 
                        job.getDepartureTime()
                    ).getClass())
                    &&
                    false//v.getVehicleStatus <-------- Moet nog geimplementeerd worden!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
               )
            {
                //Set Status of vehicle to loading
                job.setTargetVehicle(v);
            }
        }
        
        //Check if job has a vehicle, if not create a new 1
        if (job.getTargetVehicle() == null)
        {
            ExternVehicle eV = createNewVehicle(
                        job.getVehicleType(), 
                        job.getDate(), 
                        job.getDepartureTime());
            
            job.setTargetVehicle(eV); // mischien dit omzetten naar platformen uiteindelijk -> praten met mindardusssss
        }
        
        return job;
    }
    
    //Creates vehicle based on type
    private static ExternVehicle createNewVehicle(Container.TransportType typeofVehicle, Date date, float time)
    {
        ExternVehicle vehicleToReturn = null;
        
        switch (typeofVehicle)
        {
            case Truck:
                vehicleToReturn = new Truck(date, time);
                break;
            case Train:
                vehicleToReturn = new Train(date, time);
                break;    
            case Barge:
                vehicleToReturn = new Barge(date, time);
                break;
            case Seaship:
                vehicleToReturn = new Seaship(date, time);
                break;
        }
        
        return vehicleToReturn;
    }
    
    //Check list for matching vehicles
    public static void checkIncomingVehicles(Timestamp timeStamp)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(timeStamp);
        
        System.out.println(calendar.get(Calendar.DAY_OF_MONTH) + " " + calendar.get(Calendar.MONTH) + " " + calendar.get(Calendar.YEAR));
        
        for (ExternVehicle ev : scheduledArrivingVehicles)
        {
            System.out.println(ev.getArrivalDate().toString());
            
            if (
                    (false) // timestamp vergelijken, moet nog worden uitgezocht maar kan nu niet compilen
                    &&
                    (true) // functie maken die docktype vergelijkt met vehilce type en dan checken of het vrij is
                ) 
            {
                ev.enter();
                Settings.messageLog.AddMessage(ev.toString() + " is entering.");
                scheduledArrivingVehicles.remove(ev);
            }
        }
    }
    
    private static Platform matchVehicleTypeWithPlatform(Container.TransportType transportType)
    {
        return null;
    }
}
