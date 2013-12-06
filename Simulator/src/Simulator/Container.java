/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Simulator;

import com.jme3.asset.AssetManager;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Node;
import containing.Container.TransportType;
import java.util.Date;


public class Container extends Model
{
    public static float width = 2.44f;
    public static float height = 2.59f;
    public static float depth = 12.19f;
            
    public int containerId;
    public Date arrivalDate;
    public float arrivalTimeFrom;
    public float arrivalTimeTill;
    public TransportType arrivalTransport;
    public String arrivalTransportCompany;
    public containing.Vector3f arrivalPosition;
    public String owner;
    public Date departureDate;
    public float departureTimeFrom;
    public float departureTimeTill;
    public TransportType departureTransport;
    
    public Container(AssetManager assetManager, Node node, ColorRGBA color)
    {
        super(assetManager, "Models/container.j3o", node, color);
    }
    
    public void setData(int containerId, Date arrivalDate, float arrivalTimeFrom, float arrivalTimeTill, TransportType arrivalTransport, String arrivalTransportCompany, containing.Vector3f arrivalPosition, String owner, Date departureDate, float departureTimeFrom, float departureTimeTill, TransportType departureTransport) 
    {
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
    
    @Override
    public String toString()
    {
        return "Container info: ---------------------------------------" +
                "\n   ID: "+this.containerId +
                "\n   ArrivalDate: "+this.arrivalDate +
                "\n   ArrivalTimeFrom: "+this.arrivalTimeFrom +
                "\n   ArrivalTimeTill: "+this.arrivalTimeTill +
                "\n   ArrivalTransport: "+this.arrivalTransport +
                "\n   ArrivalTransportCompany: "+this.arrivalTransportCompany +
                "\n   ArrivalPosition: "+this.arrivalPosition +
                "\n   Owner: "+this.owner +
                "\n   DepartureDate: "+this.departureDate +
                "\n   DepartureTimeFrom: "+this.departureTimeFrom +
                "\n   DepartureTimeTill: "+this.departureTimeTill +
                "\n   DepartureTransport: "+this.departureTransport + "\n";
    }
} 
