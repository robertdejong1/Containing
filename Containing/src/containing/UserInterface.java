package containing;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileNameExtensionFilter;

public class UserInterface extends JFrame
{
    private File CurrentDir = null;   
    public JButton StartSimulationButton;
    public JButton StopSimulationButton;
    public JTextArea MessageLogTextArea;
    
    public UserInterface()
    {
        super();
        this.setLayout(null);
        this.getContentPane().setBackground(Color.white);
        this.setTitle("Containing 2013");
        this.setBounds(25, 25, 800, 500);
        AddButtons();
        AddLabels();
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
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
                           XMLDialog.setFileFilter(new FileNameExtensionFilter("XML file", "xml"));
                           XMLDialog.setFileSelectionMode(JFileChooser.FILES_ONLY);
                           
                           if (CurrentDir != null)
                           {
                               XMLDialog.setCurrentDirectory(CurrentDir);
                           }
                           
                           int OpenFileDialogResult = XMLDialog.showOpenDialog(UserInterface.this);
                           
                           if (OpenFileDialogResult == JFileChooser.APPROVE_OPTION)
                           {
                               File xmlFile = XMLDialog.getSelectedFile();
                               CurrentDir = XMLDialog.getCurrentDirectory();
                               
                               Controller.readXMLAndSortOutput(xmlFile);
                           }
                           else
                           {
                               ErrorLog.logMsg("Could not load file");
                               
                           }
                        }
                    }
                );
        
        //Start Button
        StartSimulationButton = new JButton("Start simulation");
        StartSimulationButton.setEnabled(false);
        StartSimulationButton.setBounds(160, 5, 150, 25);
        this.add(StartSimulationButton);
        
        StartSimulationButton.addActionListener
                (
                    new ActionListener()
                    {
                        @Override
                        public void actionPerformed(ActionEvent e) 
                        {
                            Controller.setSimulationStatus(true);
                            StartSimulationButton.setEnabled(false);
                            StopSimulationButton.setEnabled(true);
                        }
                    }
                );
        
         //stop button
        StopSimulationButton = new JButton("Stop simulation");
        StopSimulationButton.setEnabled(false);
        StopSimulationButton.setBounds(315, 5, 150, 25);
        this.add(StopSimulationButton);
        
        StopSimulationButton.addActionListener
                (
                    new ActionListener()
                    {
                        @Override
                        public void actionPerformed(ActionEvent e) 
                        {
                            Controller.setSimulationStatus(false);
                            
                            StartSimulationButton.setEnabled(true);
                            StopSimulationButton.setEnabled(false);
                        }
                    }
                );
    }
    
    private void AddLabels()
    {
        MessageLogTextArea = new JTextArea("");
        MessageLogTextArea.setBounds(5, 35, 785, 430);
        MessageLogTextArea.setBorder(BorderFactory.createLineBorder(Color.black));
        this.add(MessageLogTextArea);
    }
}
