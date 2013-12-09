/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package containing;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Robert
 */
public class NetworkHandler implements Runnable {

    private ServerSocket server;
    static int idCounter = 0;

    public NetworkHandler(int port) {
        try {
            server = new ServerSocket(port);
            server.setSoTimeout(999999999);
            Settings.messageLog.AddMessage("Server running.");
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
