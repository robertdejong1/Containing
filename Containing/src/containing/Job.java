package containing;

import containing.Vehicle.ExternVehicle;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Stack;

public class Job 
{
    private Date date;
    private float departureTime;
    private Container.TransportType vehicleType;
    private List<Container> Containers;
    private ExternVehicle targetVehicle;
    private String companyName;
    
    public Job(Date date, float departureTime, Container.TransportType vehicleType, String companyName) 
    {
        this.date = date;
        this.departureTime = departureTime;
        this.vehicleType = vehicleType;
        this.Containers = new ArrayList<>();
        this.companyName = companyName;
    }
    
    public void addContainer(Container container)
    {
        Containers.add(container);
    }

    public Date getDate() 
    {
        return date;
    }

    public float getDepartureTime() 
    {
        return departureTime;
    }

    public Container.TransportType getVehicleType() 
    {
        return vehicleType;
    }

    public List<Container> getContainers() 
    {
        return Containers;
    }

    public ExternVehicle getTargetVehicle() 
    {
        return targetVehicle;
    }

    public void setTargetVehicle(ExternVehicle TargetVehicle) 
    {
        this.targetVehicle = TargetVehicle;
    }
    
    public void changeContainerDepartureTime(float departureTime)
    {
        this.departureTime = departureTime;
        
        for (Container c : Containers)
        {
            c.setDepartureTimeFrom(departureTime);
        }
    }

    public String getCompanyName() 
    {
        return companyName;
    }
    
    
    
    public static Stack<Job> sortOutGoingJobs(List<Job> Jobs)
    {   
        Stack JobStack = new Stack<>();
        List<Job> JobsTemp = new ArrayList<>();
        
        JobsTemp.addAll(Jobs);
        
        if (JobsTemp.size() > 0)
        {
            Job LatestJob = JobsTemp.get(0);
        
            while (JobsTemp.size() > 0)
            {
                for (Job j : JobsTemp)
                {
                    if (j.date.after(LatestJob.date) && j.departureTime > LatestJob.getDepartureTime())
                    {
                        LatestJob = j;
                    }
                }
                
                JobStack.push(LatestJob);
                JobsTemp.remove(LatestJob);
                
                if (JobsTemp.size() > 0)
                {
                    LatestJob = JobsTemp.get(0);
                }
                
            }
        }
        
        return JobStack;
    }
}
