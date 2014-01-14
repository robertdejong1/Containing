package containing;

import java.awt.Color;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileNameExtensionFilter;

public class UserInterface extends JFrame
{
    private File CurrentDir = null;   
    public JButton StartSimulationButton;
    public JButton StopSimulationButton;
    public JButton QuickXml2Button;
    public JButton QuickXml3Button;
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
        
        //Quick Load xml 2 button
        QuickXml2Button = new JButton("Quick load xml2");
        QuickXml2Button.setBounds(470, 5, 150, 25);
        this.add(QuickXml2Button);
        QuickXml2Button.addActionListener
        (
            new ActionListener() 
            {
                @Override
                public void actionPerformed(ActionEvent e) 
                {
                    File xmlFile = new File("xmlfiles\\xml2.xml");
                    Controller.readXMLAndSortOutput(xmlFile);
                }
            }
        );
        
        //Quick Load xml 3 k button
        QuickXml3Button = new JButton("Quick load xml3");
        QuickXml3Button.setBounds(625, 5, 150, 25);
        this.add(QuickXml3Button);
        QuickXml3Button.addActionListener
        (
            new ActionListener() 
            {
                @Override
                public void actionPerformed(ActionEvent e) 
                {
                    File xmlFile = new File("xmlfiles\\xml3.xml");
                    Controller.readXMLAndSortOutput(xmlFile);
                    JOptionPane.showMessageDialog(new Frame(),
                        "Fock you robert met je kut button.");
                }
            }
        );
        
    }
    
    private void AddLabels()
    {
        MessageLogTextArea = new JTextArea("");
        MessageLogTextArea.setBounds(5, 35, 785, 430);
        MessageLogTextArea.setBorder(BorderFactory.createLineBorder(Color.black));
        MessageLogTextArea.setEditable(false);
        this.add(MessageLogTextArea);
    }
}
