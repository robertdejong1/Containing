package containing;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MessageLog 
{
    private List<String> Messages;
    
    public MessageLog()
    {
        Messages = new ArrayList();
        Messages.add("Controller Version: " + Settings.version);
    }
    
    public void AddMessage(String Message)
    {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
        String formattedDate = sdf.format(date);
        
        Messages.add("[" + formattedDate + "]: " + Message);
        Settings.userInterface.MessageLogTextArea.setText(GetLastMessages());
    }
    
    public String GetLastMessages()
    {
        int MessageCounter = 0;
        
        if (Messages.size() > 25)
        {
            MessageCounter = 25;
        }
        else
        {
            MessageCounter = Messages.size();
        }
        
        String StringToReturn = "";
        
        for (int i = MessageCounter - 1; i > -1; i--)
        {
            StringToReturn = StringToReturn + Messages.get(i) + "\n";
        }
        
        StringToReturn += "";
        return StringToReturn;
        
    }
    
    public void ClearMessages()
    {
        Messages = new ArrayList();
        Messages.add("Controller Version: " + Settings.version);
        GetLastMessages();
    }
    
    public void WriteMessagesToFile()
    {
        
    }
}
