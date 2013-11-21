package containing;

import java.util.ArrayList;
import java.util.List;

public class MessageLog 
{
    private List<String> Messages;
    
    public MessageLog()
    {
        Messages = new ArrayList();
    }
    
    public void AddMessage(String Message)
    {
        Messages.add(Message);
        
    }
    
    public String GetLastMessagesAsHTMLString()
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
            StringToReturn = StringToReturn + Messages.get(i) + "<br />";
        }
        
        return StringToReturn;
        
    }
    
    public void ClearMessages()
    {
        Messages = new ArrayList();
    }
    
    public void WriteMessagesToFile()
    {
        
    }
}
