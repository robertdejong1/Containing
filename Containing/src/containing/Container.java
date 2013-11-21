package containing;

import java.util.Date;

public class Container 
{
    private int containerId;
    private Date arrivalDate;
    private float arrivalTimeFrom;
    private float arrivalTimeTill;
    private TransportType arrivalTransport;
    private String arrivalTransportCompany;
    private Vector3f arrivalPosition;
    private String owner;
    private Date departureDate;
    private float departureTimeFrom;
    private float departureTimeTill;
    private TransportType departureTransport;
    
    public enum TransportType{
        Truck, Train, Barge, Seaship
    }

    public Container(int containerId, Date arrivalDate, float arrivalTimeFrom, float arrivalTimeTill, TransportType arrivalTransport, String arrivalTransportCompany, Vector3f arrivalPosition, String owner, Date departureDate, float departureTimeFrom, float departureTimeTill, TransportType departureTransport) {
        this.containerId = containerId;
        this.arrivalDate = arrivalDate;
        this.arrivalTimeFrom = arrivalTimeFrom;
        this.arrivalTimeTill = arrivalTimeTill;
        this.arrivalTransport = arrivalTransport;
        this.arrivalTransportCompany = arrivalTransportCompany;
        this.arrivalPosition = arrivalPosition;
        this.owner = owner;
        this.departureDate = departureDate;
        this.departureTimeFrom = departureTimeFrom;
        this.departureTimeTill = departureTimeTill;
        this.departureTransport = departureTransport;
    }
    
    public int getContainerId() {
        return containerId;
    }

    public Date getArrivalDate() {
        return arrivalDate;
    }

    public float getArrivalTimeFrom() {
        return arrivalTimeFrom;
    }

    public float getArrivalTimeTill() {
        return arrivalTimeTill;
    }

    public TransportType getArrivalTransport() {
        return arrivalTransport;
    }

    public String getArrivalTransportCompany() {
        return arrivalTransportCompany;
    }

    public String getOwner() {
        return owner;
    }

    public Date getDepartureDate() {
        return departureDate;
    }

    public float getDepartureTimeFrom() {
        return departureTimeFrom;
    }

    public float getDepartureTimeTill() {
        return departureTimeTill;
    }

    public TransportType getDepartureTransport() {
        return departureTransport;
    }
    
    @Override
    public String toString(){
        String ret = "Container:[";
        ret += this.containerId +", ";
        ret += this.arrivalDate.toString() +", ";
        ret += this.arrivalTimeFrom +", ";
        ret += this.arrivalTimeTill +", ";
        ret += this.arrivalTransport +", ";
        ret += this.arrivalTransportCompany +", ";
        ret += this.arrivalPosition.toString() +", ";
        ret += this.owner +", ";
        ret += this.departureDate +", ";
        ret += this.departureTimeFrom +", ";
        ret += this.departureTimeTill +", ";
        ret += this.departureTransport +"]";
        
        return ret;
    }
}
