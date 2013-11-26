/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package containing;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

/**
 *
 * @author Robert
 */
public class NetworkHandler implements Runnable {

    private ServerSocket server;
    private Socket client;

    public NetworkHandler(int port) {
        try {
            server = new ServerSocket(port);
            server.setSoTimeout(999999999);
            System.out.println("Server running.");
        } catch (IOException e) {
            ErrorLog.logMsg("An error occured while creating Socket Server", e);
        }
    }

    @Override
    public void run() {
        try {
            client = server.accept();
            System.out.println("Client connected from: " +client.getInetAddress().getHostAddress());
            
            PrintWriter writer = new PrintWriter(client.getOutputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
                
            while (client.isConnected()) {
                if (reader.ready()) {
                    String inputLine = reader.readLine();
                    System.out.println("Received: " +inputLine);
                    Command returnCmd = CommandHandler.handle(inputLine);
                    if (returnCmd != null) {
                        System.out.println("Sending in reply to " +inputLine +": " +returnCmd.toString());
                        writer.println(returnCmd.toString());
                        writer.flush();
                    }
                }
                else{
                    List<Command> commands = Controller.getNewCommands();
                    if(commands != null && commands.size() > 0){
                        for(Command cmd : commands){
                            System.out.println("Sending: " +cmd.toString());
                            writer.println(cmd.toString());
                            writer.flush();
                        }
                    }
                }
            }
            //Uit de while loop. Client niet meer verbonden.
            System.out.println("Connection from client lost. Restarting...");
            run();
            
        } catch (IOException e) {
            ErrorLog.logMsg("An error occured", e);
        }

    }
}
