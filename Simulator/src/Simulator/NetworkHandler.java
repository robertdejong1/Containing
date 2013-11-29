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
import java.util.Date;
import java.util.List;

/**
 *
 * @author Robert
 */
public class NetworkHandler implements Runnable {

    private String ip;
    private int port;
    private Socket client;
    private long lastPing;

    public NetworkHandler(String ip, int port) {
        this.ip = ip;
        this.port = port;
        Date date = new Date();
        this.lastPing = (date.getTime() / 1000);
    }

    @Override
    public void run() {
        try{
            client = new Socket(ip, port);
            System.out.println("Connected to " +ip +":" +port);
            PrintWriter writer = new PrintWriter(client.getOutputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));

            while(true){
                if(sendPing()){
                    System.out.println("Sending: PING");
                    writer.println("PING");
                    writer.flush();
                }
                if(!reader.ready()){
                    if(CommandHandler.newCommandsAvailable()){
                        List<String> commands = CommandHandler.getCommands();
                        for(String cmd : commands){
                            System.out.println("Sending: " +cmd);
                            writer.println(cmd);
                            writer.flush();
                        }
                        CommandHandler.clearCommands();
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
    
    private boolean sendPing(){
        Date date = new Date();
        long currentTime = (date.getTime() / 1000);
        if(currentTime - this.lastPing > 60){
            this.lastPing = currentTime;
            return true;
        }
        return false;
    }
}
