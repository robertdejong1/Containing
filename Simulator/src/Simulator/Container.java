/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Simulator;

import com.jme3.asset.AssetManager;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import containing.Container.TransportType;
import java.util.Date;


public class Container extends Model
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
    
    public Container(AssetManager assetManager, Node node, ColorRGBA color)
    {
        super(assetManager, "Models/container.j3o", node, color);
    }
    
    public void setData(int containerId, Date arrivalDate, float arrivalTimeFrom, float arrivalTimeTill, TransportType arrivalTransport, String arrivalTransportCompany, Vector3f arrivalPosition, String owner, Date departureDate, float departureTimeFrom, float departureTimeTill, TransportType departureTransport) 
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
}
