package containing;

import java.io.Serializable;
import java.util.Date;

public class Container implements Serializable
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
    public static float width = 2.44f;
    public static float height = 2.59f;
    public static float depth = 12.19f;
    
    public enum TransportType{
        Truck, Train, Barge, Seaship
    }

    /**
     * Creates a Container instance
     * @param containerId Id for this container
     * @param arrivalDate Arrival date for this container
     * @param arrivalTimeFrom Arrival time from for this container
     * @param arrivalTimeTill Arrival time till for this container
     * @param arrivalTransport Arrival transport for this container
     * @param arrivalTransportCompany Arrival transport type for this container
     * @param arrivalPosition Arrival position for this container
     * @param owner Owner of this container
     * @param departureDate Departure date of this container
     * @param departureTimeFrom Departure time from for this container
     * @param departureTimeTill Departure time till for this container
     * @param departureTransport Departure transport type for this container
     */
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

    public Vector3f getArrivalPosition() {
        return arrivalPosition;
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

    public void setDepartureDate(Date departureDate) {
        this.departureDate = departureDate;
    }

    public void setDepartureTimeFrom(float departureTimeFrom) {
        this.departureTimeFrom = departureTimeFrom;
    }

    public void setDepartureTimeTill(float departureTimeTill) {
        this.departureTimeTill = departureTimeTill;
    }
    
    @Override
    public String toString() {
        return "Container{" + "containerId=" + containerId + ", arrivalDate=" + arrivalDate + ", arrivalTimeFrom=" + arrivalTimeFrom + ", arrivalTimeTill=" + arrivalTimeTill + ", arrivalTransport=" + arrivalTransport + ", arrivalTransportCompany=" + arrivalTransportCompany + ", arrivalPosition=" + arrivalPosition + ", owner=" + owner + ", departureDate=" + departureDate + ", departureTimeFrom=" + departureTimeFrom + ", departureTimeTill=" + departureTimeTill + ", departureTransport=" + departureTransport + '}';
    }
}
