/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Simulator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.List;

/**
 *
 * @author Robert
 */
public class NetworkHandler implements Runnable {

    private String ip;
    private int port;
    private Socket client;

    public NetworkHandler(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    @Override
    public void run() {
        try{
            client = new Socket(ip, port);
            System.out.println("Connected to " +ip +":" +port);
            PrintWriter writer = new PrintWriter(client.getOutputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));

            while(true){
                if(!reader.ready()){
                    List<String> commands = CommandHandler.getCommands();
                    for(String cmd : commands){
                        System.out.println("Sending: " +cmd);
                        writer.println(cmd);
                        writer.flush();
                    }
                }
                else{
                    String inputLine = reader.readLine();
                    System.out.println("Received: " +inputLine);
                    CommandHandler.handle(inputLine);
                }
            }
        }
        catch(IOException e){
            ErrorLog.logMsg("Error while connecting to Controller Server", e);
            System.out.println("Connection lost. Trying to reconnect...");
            run();
        }
    }
}
