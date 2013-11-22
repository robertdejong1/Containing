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
import org.json.simple.JSONObject;

/**
 *
 * @author Robert
 */
public class NetworkHandler implements Runnable {

    private Port port;
    private ServerSocket serverSocket;
    private Socket clientSocket;

    public NetworkHandler(Port port) {
        this.port = port;
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(1337);
            //Wachten op een connectie
            clientSocket = serverSocket.accept();

            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            String inputLine, outputLine;
            while (true) {
                inputLine = in.readLine();
                if (inputLine == null) {
                    System.out.println(inputLine);
                    outputLine = processCommand(inputLine);
                    System.out.println(outputLine);
                    out.println(outputLine);
                    if (outputLine.equals("QUIT")) {
                        break;
                    }
                }
                else{
                    List<String> commands = Controller.getNewCommands();
                    if(commands.size() > 0){
                        for(String cmd : commands){
                            out.println(cmd);
                        }
                    }
                }
            }

        } catch (IOException e) {
            ErrorLog.logMsg("Error while creating Socket Server", e);
            return;
        }
    }

    private String processCommand(String cmd) {
        JSONObject json = new JSONObject();
        String prefix = cmd.split(":", 1)[0];
        switch (prefix) {
            case "PORT":
                return json.put("PORT", port).toString();
            //Meer cases

            default:
                return "";
        }
    }
}
