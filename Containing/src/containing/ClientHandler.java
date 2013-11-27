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
import java.net.Socket;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Robert
 */
public class ClientHandler implements Runnable {

    private Socket client;
    private int id;
    private long lastPing;

    public ClientHandler(Socket client, int id) {
        this.client = client;
        this.id = id;

        Date date = new Date();
        this.lastPing = (date.getTime() / 1000);
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
                    System.out.println("Received: " + inputLine);

                    if (inputLine.equals("PING")) {
                        Date date = new Date();
                        this.lastPing = (date.getTime() / 1000);
                    } else
                    {
                        Command returnCmd = CommandHandler.handle(inputLine);
                        if (returnCmd != null) {
                            System.out.println("Sending in reply to " + inputLine + ": " + returnCmd.toString());
                            writer.println(returnCmd.toString());
                            writer.flush();
                        }
                    }

                }
                else {
                    List<Command> commands = CommandHandler.getNewCommands(this.id);
                    if (commands != null && commands.size() > 0) {
                        for (Command cmd : commands) {
                            System.out.println("Sending: " + cmd.toString());
                            writer.println(cmd.toString());
                            writer.flush();
                        }
                    }
                }
            }
            System.out.println("Client disconnected from server");
        } catch (IOException e) {
            ErrorLog.logMsg("An error occured while handling a client connection", e);
        }
    }

    private boolean isAlive() {
        Date date = new Date();
        long currentTime = (date.getTime() / 1000);
        return !(currentTime - this.lastPing > 90);
    }
}
