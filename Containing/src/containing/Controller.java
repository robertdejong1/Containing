package containing;

import java.io.File;

public class Controller {

    private static UserInterface UserInterface;
    private static Clock clock; 
    
    public static void main(String[] args) 
    {
       UserInterface = new UserInterface(); 
       clock = new Clock();
    }
    
    public static void ReadXML(File XMLFile)
    {
        XmlHandler xmlHandler = new XmlHandler();
        System.out.println(xmlHandler.openXml(XMLFile).toString());
        //Lees xml
        //Sorteer containers naar boten
        
    }
    
    
    
}
