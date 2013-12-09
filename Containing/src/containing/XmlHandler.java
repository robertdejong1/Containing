/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package containing;

import containing.Container.TransportType;
import containing.Exceptions.InvalidTransportException;
import containing.Exceptions.ParseErrorException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author Robert
 */
public class XmlHandler {

    public List<Container> openXml(File xmlfile) throws ParseErrorException{
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try {
            builder = builderFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new ParseErrorException("Error while parsing xml file" +e.getMessage());
        }

        try {
            Document doc = builder.parse(xmlfile);
            return this.parse(doc);
        }
        catch (InvalidTransportException | IOException | SAXException e) {
            throw new ParseErrorException("Error while parsing xml file" +e.getMessage());
        }
    }

    private List<Container> parse(Document doc) throws InvalidTransportException {
        List<Container> containers = new ArrayList<>();

        NodeList nodes = doc.getDocumentElement().getChildNodes();
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            if (node instanceof Element) {
                int id = Integer.parseInt(node.getAttributes().getNamedItem("id").getNodeValue().substring(2));

                String d = getNestedValue(node, Arrays.asList("aankomst", "datum", "d")).getNodeValue();
                String m = getNestedValue(node, Arrays.asList("aankomst", "datum", "m")).getNodeValue();
                String j = "20" + getNestedValue(node, Arrays.asList("aankomst", "datum", "j")).getNodeValue();
                Date arrivalDate = new Date(Integer.parseInt(j) - 1900, Integer.parseInt(m) - 1, Integer.parseInt(d));
                
                String Atill = getNestedValue(node, Arrays.asList("aankomst", "tijd", "van")).getNodeValue();
                float arrivalTimeFrom = Float.parseFloat(Atill);

                String Afrom = getNestedValue(node, Arrays.asList("aankomst", "tijd", "tot")).getNodeValue();
                float arrivalTimeTill = Float.parseFloat(Afrom);

                String Atransport = getNestedValue(node, Arrays.asList("aankomst", "soort_vervoer")).getNodeValue();
                Container.TransportType arrivalTransport;
                switch (Atransport) {
                    case "vrachtauto":
                        arrivalTransport = TransportType.Truck;
                        break;

                    case "zeeschip":
                        arrivalTransport = TransportType.Seaship;
                        break;

                    case "binnenschip":
                        arrivalTransport = TransportType.Barge;
                        break;

                    case "trein":
                        arrivalTransport = TransportType.Train;
                        break;

                    default:
                        throw new InvalidTransportException("Invalid transport type: " +Atransport);
                }

                String arrivalTransportCompany = getNestedValue(node, Arrays.asList("aankomst", "bedrijf")).getNodeValue();
                
                String x = getNestedValue(node, Arrays.asList("aankomst", "positie", "x")).getNodeValue();
                String y = getNestedValue(node, Arrays.asList("aankomst", "positie", "y")).getNodeValue();
                String z = getNestedValue(node, Arrays.asList("aankomst", "positie", "z")).getNodeValue();
                Vector3f arrivalPosition = new Vector3f(Float.parseFloat(x), Float.parseFloat(y), Float.parseFloat(z));
                
                String owner = getNestedValue(node, Arrays.asList("eigenaar", "naam")).getNodeValue();
                
                String dd = getNestedValue(node, Arrays.asList("vertrek", "datum", "d")).getNodeValue();
                String dm = getNestedValue(node, Arrays.asList("vertrek", "datum", "m")).getNodeValue();
                String dj = "20" +getNestedValue(node, Arrays.asList("vertrek", "datum", "j")).getNodeValue();
                
                Date departureDate = new Date(Integer.parseInt(dj) - 1900, Integer.parseInt(dm) - 1, Integer.parseInt(dd));
                
                String dFrom = getNestedValue(node, Arrays.asList("vertrek", "tijd", "van")).getNodeValue();
                float departureTimeFrom = Float.parseFloat(dFrom);
                
                String dTill = getNestedValue(node, Arrays.asList("vertrek", "tijd", "tot")).getNodeValue();
                float departureTimeTill = Float.parseFloat(dTill);
                
                String dTransport = getNestedValue(node, Arrays.asList("vertrek", "soort_vervoer")).getNodeValue();
                TransportType departureTransport;
                switch (dTransport) {
                    case "vrachtauto":
                        departureTransport = TransportType.Truck;
                        break;

                    case "zeeschip":
                        departureTransport = TransportType.Seaship;
                        break;

                    case "binnenschip":
                        departureTransport = TransportType.Barge;
                        break;

                    case "trein":
                        departureTransport = TransportType.Train;
                        break;

                    default:
                        throw new InvalidTransportException("Invalid transport type: " +Atransport);
                }
                Container con = new Container(id, arrivalDate, arrivalTimeFrom, arrivalTimeTill, arrivalTransport, arrivalTransportCompany, arrivalPosition, owner, departureDate, departureTimeFrom, departureTimeTill, departureTransport);
                containers.add(con);
            }
            
        }
        return containers;

    }

    private Node getNestedValue(Node node, List<String> nests) {
        if (nests.isEmpty()) {
            return node.getChildNodes().item(0);
        }

        NodeList nodes = node.getChildNodes();
        for (int i = 0; i < nodes.getLength(); i++) {
            Node n = nodes.item(i);
            if (nests.get(0).equalsIgnoreCase(n.getNodeName())) {
                return getNestedValue(n, nests.subList(1, nests.size()));
            }
        }
        return null;
    }
}
