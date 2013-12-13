package containing;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Robert
 */
public class NetworkHandler implements Runnable {

    private ServerSocket server;
    static int idCounter = 0;

    /**
     * Creates a NetworkHandler instance
     * @param port Network port where to server should listen on
     */
    public NetworkHandler(int port) {
        try {
            server = new ServerSocket(port);
            server.setSoTimeout(999999999);
            Settings.messageLog.AddMessage("Server running on " +Inet4Address.getLocalHost().getHostAddress() +".");
        } catch (IOException e) {
            ErrorLog.logMsg("An error occured while creating Socket Server", e);
        }
    }

    @Override
    public void run() {
        try {
            while(true){
                Socket client = server.accept();
                Settings.messageLog.AddMessage("Client connected from: " +client.getInetAddress().getHostAddress());
                Runnable clientHandler = new ClientHandler(client, getNewId());
                Thread t =  new Thread(clientHandler);
                t.start();
            }
        }
        catch(IOException e){
            ErrorLog.logMsg("An error occured while creating socket client", e);
            run();
        }

    }
    
    static int getNewId(){
        return ++idCounter;
    }
}
