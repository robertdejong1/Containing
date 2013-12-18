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
import java.net.SocketTimeoutException;
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
    private long lastPong;

    /**
     * Creates a NetworkHandler instance
     * @param ip IP address to connect to
     * @param port Network port to connect to
     */
    public NetworkHandler(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }
    
    public static void main(String[] args){
        Runnable r = new NetworkHandler("141.252.222.150", 1337);
        Thread t = new Thread(r);
        t.start();
    }

    @Override
    public void run() {
        try {
            client = new Socket(ip, port);
            Date date = new Date();
            this.lastPing = (date.getTime() / 1000);
            this.lastPong = (date.getTime() / 1000);
            client.setSoTimeout(1000);
            System.out.println("Connected to " + ip + ":" + port);
            PrintWriter writer = new PrintWriter(client.getOutputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));

            CommandHandler.addCommand("IDENTIFY:SIM");

            while (true) {
                if (!isAlive()) {
                    client.close();
                    break;
                }
                if (sendPing()) {
                    System.out.println("Sending: PING");
                    writer.println("PING");
                    writer.flush();
                }
                if (!reader.ready()) {
                    if (CommandHandler.newCommandsAvailable()) {
                        List<String> commands = CommandHandler.getCommands();
                        for (String cmd : commands) {
                            System.out.println("Sending: " + cmd);
                            writer.println(cmd);
                            writer.flush();
                        }
                        CommandHandler.clearCommands();
                    }
                } else {
                    try {
                        String inputLine = reader.readLine();
                        System.out.println("Received: " + inputLine);
                        if (inputLine.equals("PONG")) {
                            Date d = new Date();
                            this.lastPong = (d.getTime() / 1000);
                        } else {
                            CommandHandler.handle(inputLine);
                        }
                    } catch (SocketTimeoutException e) {
                        System.out.println("Socket blocked for 1 second. Continueing.");
                    }

                }
            }
            System.out.println("Connection lost. Trying to reconnect....");
            run();
        } catch (IOException e) {
            ErrorLog.logMsg("Error while connecting to Controller Server", e);
            System.out.println("Connection lost. Trying to reconnect...");
            run();
        }
    }

     /**
     * Checks if the connection is still alive 
     * @return true is the connection is still alive, false otherwise
     */
    private boolean isAlive() {
        Date date = new Date();
        long currentTime = (date.getTime() / 1000);
        return !(currentTime - this.lastPong > 90);
    }

    /**
     * Checks if it's time to send ping to the server
     * @return true if it's time to send ping to the server, false otherwise
     */
    private boolean sendPing() {
        Date date = new Date();
        long currentTime = (date.getTime() / 1000);
        if (currentTime - this.lastPing > 60) {
            this.lastPing = currentTime;
            return true;
        }
        return false;
    }
}
