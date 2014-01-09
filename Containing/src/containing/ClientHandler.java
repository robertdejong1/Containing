package containing;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Robert
 */
public class ClientHandler implements Runnable {

    public enum ClientType { APP, SIM, WEB }
    
    private Socket client;
    private int id;
    private long lastPing;
    private ClientType clientType;

    /**
     * Creates a ClientHandler instance
     * @param client Client socket
     * @param id unique id for this instance
     */
    public ClientHandler(Socket client, int id) {
        this.client = client;
        this.id = id;

        Date date = new Date();
        this.lastPing = (date.getTime() / 1000);
        CommandHandler.addidTime(id);
    }
    
    @Override
    public void run() {
        try {
            PrintWriter writer = new PrintWriter(client.getOutputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));

            while (client.isConnected()) {
                if (!isAlive()) {
                    client.close();
                    break;
                }
                if (reader.ready()) {
                    String inputLine = reader.readLine();
                    Settings.messageLog.AddMessage("Received: " + inputLine);
                    
                    switch (inputLine) {
                        case "PING":
                            Date date = new Date();
                            this.lastPing = (date.getTime() / 1000);
                            Settings.messageLog.AddMessage("Sending: PONG");
                            writer.println("PONG"); //PONG terug sturen
                            writer.flush();
                            break;
                        case "IDENTIFY:APP":
                            this.clientType = ClientType.APP;
                            break;
                        case "IDENTIFY:SIM":
                            this.clientType = ClientType.SIM;
                            Command cmd = new Command("INIT", Settings.port);
                            Settings.messageLog.AddMessage("Sending init info " +cmd.toString());
                            writer.println(cmd.toString());
                            writer.flush();
                            break;
                        case "IDENTIFY:WEB":
                            this.clientType = ClientType.WEB;
                            break;
                        default:
                            Command returnCmd = CommandHandler.handle(inputLine);
                            if (returnCmd != null) {
                                Settings.messageLog.AddMessage("Sending in reply to " + inputLine + ": " + returnCmd.toString());
                                writer.println(returnCmd.toString());
                                writer.flush();
                            }   break;
                    }

                }
                else {
                    List<Command> commands = CommandHandler.getNewCommands(this.id, this.clientType);
                    if (commands != null && commands.size() > 0) {
                        for (Command cmd : commands) {
                            if(this.clientType == ClientType.WEB){
                                Settings.messageLog.AddMessage("Sending: (" +cmd.getCommand() +") " + cmd.toString(false));
                                writer.println(cmd.toString(false));
                                writer.flush();
                            }
                            else{
                                Settings.messageLog.AddMessage("Sending: (" +cmd.getCommand() +") " + cmd.toString());
                                writer.println(cmd.toString());
                                writer.flush();
                            }
                        }
                    }
                }
            }
            Settings.messageLog.AddMessage("Client disconnected from server");
        }
        catch (IOException e) {
            ErrorLog.logMsg("An error occured while handling a client connection", e);
        }
    }

    /**
     * Checks if the connection is still alive
     * @return true if the connection is alive, false otherwise
     */
    private boolean isAlive() {
        Date date = new Date();
        long currentTime = (date.getTime() / 1000);
        return !(currentTime - this.lastPing > 90);
    }
}