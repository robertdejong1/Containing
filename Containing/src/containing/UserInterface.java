package containing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;



public class UserInterface extends JFrame
{
    private File CurrentDir = null;    
    
    public UserInterface()
    {
        super();
        this.setLayout(null);
        this.setBounds(25, 25, 500, 500);
        AddButtons();
        this.setVisible(true);
    }
    
    private void AddButtons()
    {
        //Open xml Button
        JButton OpenXMLButton = new JButton("Open xml file");
        OpenXMLButton.setBounds(5, 5, 150, 25);
        this.add(OpenXMLButton);
        
        OpenXMLButton.addActionListener
                (
                    new ActionListener() 
                    {
                        @Override
                        public void actionPerformed(ActionEvent e) 
                        {
                           JFileChooser XMLDialog = new JFileChooser();
                           
                           if (CurrentDir != null)
                           {
                               XMLDialog.setCurrentDirectory(CurrentDir);
                           }
                           
                           int OpenFileDialogResult = XMLDialog.showOpenDialog(UserInterface.this);
                           
                           if (OpenFileDialogResult == JFileChooser.APPROVE_OPTION)
                           {
                               File xmlFile = XMLDialog.getSelectedFile();
                               CurrentDir = XMLDialog.getCurrentDirectory();
                               
                               Controller.ReadXML(xmlFile);
                           }
                           else
                           {
                               ErrorLog.logMsg("Could not load file", null);
                               
                           }
                        }
                    }
                );
        
        //Start Button
        JButton StartSimulationButton = new JButton("Start simulation");
        StartSimulationButton.setBounds(160, 5, 150, 25);
        this.add(StartSimulationButton);
        
        //stop Button
        JButton StopSimulationButton = new JButton("Stop simulation");
        StopSimulationButton.setBounds(315, 5, 150, 25);
        this.add(StopSimulationButton);
    }
}
