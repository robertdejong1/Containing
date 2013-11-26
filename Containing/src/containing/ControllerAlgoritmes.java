package containing;

import static containing.Container.TransportType.Barge;
import static containing.Container.TransportType.Seaship;
import static containing.Container.TransportType.Train;
import static containing.Container.TransportType.Truck;
import containing.Vehicle.Barge;
import containing.Vehicle.ExternVehicle;
import containing.Vehicle.Seaship;
import containing.Vehicle.Train;
import containing.Vehicle.Truck;
import containing.Vehicle.Vehicle;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Stack;

class ControllerAlgoritmes 
{
    private static List<Job> JobQeueUnsorted;
    private static Stack<Job> JobQeueSorted;
    private static List<ExternVehicle> scheduledArrivingVehicles;
    
    public static void SortInCommingContainers(List<Container> ContainersFromXML, UserInterface UserInterface)
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
    public static void SortOutgoingContainer(Container UnloadedContainer)
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
    public static Job GetNextJob(List<ExternVehicle> externalVehicles) 
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
        ExternVehicle VehicleToReturn = null;
        
        switch (typeofVehicle)
        {
            case Truck:
                VehicleToReturn = new Truck(date, time);
                break;
            case Train:
                VehicleToReturn = new Train(date, time);
                break;    
            case Barge:
                VehicleToReturn = new Barge(date, time);
                break;
            case Seaship:
                VehicleToReturn = new Seaship(date, time);
                break;
        }
        
        return VehicleToReturn;
    }
}
