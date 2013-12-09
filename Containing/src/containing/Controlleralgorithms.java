package containing;

import static containing.Container.TransportType.Barge;
import static containing.Container.TransportType.Seaship;
import static containing.Container.TransportType.Train;
import static containing.Container.TransportType.Truck;
import containing.Exceptions.CargoOutOfBoundsException;
import containing.Exceptions.NoJobException;
import containing.Exceptions.VehicleOverflowException;
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

class Controlleralgorithms 
{
    private static List<Job> jobQeueUnsorted;
    private static Stack<Job> jobQeueSorted;
    private static List<ExternVehicle> scheduledArrivingVehicles;
    
    public static void sortInCommingContainers(List<Container> ContainersFromXML)
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
                        
                        ErrorLog.logMsg(VehicleWithMatchingDateAndTime.getVehicleType() + " has no room for this container.", e);
                    }
                }
                else
                {
                    ExternVehicle NewVehicle = createNewVehicle(
                            ContainersFromXML.get(i).getArrivalTransport(), 
                            ContainersFromXML.get(i).getArrivalDate(), 
                            ContainersFromXML.get(i).getArrivalTimeFrom(),
                            ContainersFromXML.get(i).getArrivalTransportCompany());
                    
                    Settings.messageLog.AddMessage("Created new " + ContainersFromXML.get(i).getArrivalTransport()); // : " + VehicleWithMatchingDateAndTime.toString());
                    try
                    {
                        NewVehicle.load(ContainersFromXML.get(i));
                    }
                    catch (CargoOutOfBoundsException | VehicleOverflowException e)
                    {
                        //isvoor later
                    }
                    
                    scheduledArrivingVehicles.add(NewVehicle);
                }
                
            }
            
            try
            {
                Settings.userInterface.StartSimulationButton.setEnabled(true);
            }
            catch (NullPointerException E)
            {
                //voor test
            }            
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
        
        if (jobQeueUnsorted == null)
        {
            jobQeueUnsorted = new ArrayList<>();
        }
        
        for (Job j : jobQeueUnsorted)
        {
            if (j.getDate().equals(UnloadedContainer.getDepartureDate())
                    &&
                j.getVehicleType().equals(UnloadedContainer.getDepartureTransport())
                    &&
                createNewVehicle(
                    j.getVehicleType(), 
                    UnloadedContainer.getDepartureDate(),
                    UnloadedContainer.getDepartureTimeFrom(), 
                    UnloadedContainer.getArrivalTransportCompany()).getCapicity() > j.getContainers().size()
                )
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
                    UnloadedContainer.getDepartureTransport(),
                    UnloadedContainer.getArrivalTransportCompany());
            job.addContainer(UnloadedContainer);
            
            jobQeueUnsorted.add(job);
        }
        
        jobQeueSorted = Job.sortOutGoingJobs(jobQeueUnsorted);
    }
    
   /*
    *Removes job from the JobQeue
    *Checks if there is already a docked vehicle waiting
    *if not create a new vehicle
    *return job
    */ 
    public static Job getNextJob(Platform platform, Timestamp timeStamp) throws NoJobException
    {
        Job job = null;
        
        //Create date and time from timestamp
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(timeStamp);
        
        Date date = new Date(calendar.getTime().getYear(), calendar.getTime().getMonth(), calendar.getTime().getDate());
        float departureTime = timeStamp.getHours() + ((float)timeStamp.getMinutes() / 100);
        
        //Get job and remove from qeue
        try
        {
            job = jobQeueSorted.pop();
        }
        catch (Exception e)//OOPS hou geen rekening met verschillende types platformen
        {
            throw new NoJobException("No jobs in qeue.");
        }
        
        if (
                job.getVehicleType() == platform.getTransportType()
                &&
                (
                    job.getDate().equals(date) || job.getDate().before(date)
                )
                &&
                (
                    job.getDepartureTime() <= departureTime
                )
            )
        {
            jobQeueUnsorted.remove(job);
            
            //Check platform for empty parkingspot:
            if (platform.hasFreeParkingSpot())
            {
                ExternVehicle eV = createNewVehicle(
                        job.getVehicleType(), 
                        job.getDate(), 
                        job.getDepartureTime(), 
                        job.getCompanyName());
                
                job.setTargetVehicle(eV);
            }
            else
            {
                //job.changeContainerDepartureTime(job.getDepartureTime());
                jobQeueSorted.push(job);
                jobQeueUnsorted.add(job);
            
                job = null;
                throw new NoJobException("No job available");
            }
        
            return job;
        }
        else
        {
            jobQeueSorted.push(job);
            throw new NoJobException("No jobs for platform.");
        }
        
    }
    
    //Creates vehicle based on type
    private static ExternVehicle createNewVehicle(Container.TransportType typeofVehicle, Date date, float time, String companyName)
    {
        ExternVehicle vehicleToReturn = null;
        
        switch (typeofVehicle)
        {
            case Truck:
                vehicleToReturn = new Truck(date, time, matchTransportTypeWithPlatform(typeofVehicle), companyName);
                break;
            case Train:
                vehicleToReturn = new Train(date, time , matchTransportTypeWithPlatform(typeofVehicle), companyName);
                break;    
            case Barge:
                vehicleToReturn = new Barge(date, time, matchTransportTypeWithPlatform(typeofVehicle), companyName);
                break;
            case Seaship:
                vehicleToReturn = new Seaship(date, time, matchTransportTypeWithPlatform(typeofVehicle), companyName);
                break;
        }
        
        return vehicleToReturn;
    }
    
    //Check list for matching vehicles
    public static void checkIncomingVehicles(Timestamp timeStamp)
    {
        //System.out.println(timeStamp);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(timeStamp);
        
        Date date = new Date(calendar.getTime().getYear(), calendar.getTime().getMonth(), calendar.getTime().getDate());
        float arrivalTime = timeStamp.getHours() + ((float)timeStamp.getMinutes() / 100);
        
        for (ExternVehicle ev : scheduledArrivingVehicles)
        {             
            if (
                    (date.equals(ev.getArrivalDate()) && (arrivalTime == ev.getArrivalTime()))
                    &&
                    (ev.getCurrentPlatform().hasFreeParkingSpot())
                ) 
            {
                ev.enter();
                Settings.messageLog.AddMessage(ev.toString() + " is entering.");
                scheduledArrivingVehicles.remove(ev);
                break;
            }
        }
    }
    
    private static Platform matchTransportTypeWithPlatform(Container.TransportType transportType)
    {
        Platform platform = null;
        
        for (Platform p : Settings.port.getPlatforms())
        {
            if (p.getTransportType() == transportType)
            {
                platform = p;
            }
        }
        
        return platform;
    }
    
    public static long getFirstDate(List<Container> ContainersFromXML)
    {
        if (ContainersFromXML.size() > 0)
        {
            Container FirstContainer = ContainersFromXML.get(0);
        
            for (Container c : ContainersFromXML)
            {
                if ((c.getArrivalDate().before(FirstContainer.getArrivalDate())
                        &&
                   (c.getArrivalTimeFrom() <= FirstContainer.getArrivalTimeFrom()))
                        ||
                   ((c.getArrivalDate().equals(FirstContainer.getArrivalDate())
                        &&
                   (c.getArrivalTimeFrom() <= FirstContainer.getArrivalTimeFrom()))
                        
                   ))
                {
                    FirstContainer = c;
               }
            }
            
            int hours = (int)FirstContainer.getArrivalTimeFrom();
            float minutes = ((float)FirstContainer.getArrivalTimeFrom() % 1) * 100;
            
            Date date = new Date(FirstContainer.getArrivalDate().getYear(), FirstContainer.getArrivalDate().getMonth(), FirstContainer.getArrivalDate().getDate(), hours, (int)minutes);
            return date.getTime();
        }
        else
        {
            return(1102806000000l);
        }
    }

    public static List<ExternVehicle> getScheduledArrivingVehicles() 
    {
        return scheduledArrivingVehicles;
    }

    public static Stack<Job> getJobQeueSorted() 
    {
        return jobQeueSorted;
    }

    public static List<Job> getJobQeueUnsorted() 
    {
        return jobQeueUnsorted;
    }
    
    public static void ClearLists()
    {
        jobQeueUnsorted.clear();
        jobQeueSorted.clear();
        scheduledArrivingVehicles.clear();
    }
    
}
