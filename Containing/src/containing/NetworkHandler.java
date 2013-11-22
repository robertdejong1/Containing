/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package containing;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.WritableByteChannel;
import java.util.List;

/**
 *
 * @author Robert
 */
public class NetworkHandler implements Runnable {

    private Port port;
    private ServerSocketChannel serverSocket;
    private SocketChannel clientSocket;

    public NetworkHandler(Port port) {
        this.port = port;
    }

    @Override
    public void run() {
        try {
            serverSocket = ServerSocketChannel.open();
            serverSocket.bind(new InetSocketAddress(1337));
            serverSocket.configureBlocking(false);

            while (true) {
                clientSocket = serverSocket.accept();
                //Client verbonden
                if (clientSocket != null) {
                    System.out.println("Client connected");

                    ReadableByteChannel rbc = Channels.newChannel(clientSocket.socket().getInputStream());
                    WritableByteChannel wbc = Channels.newChannel(clientSocket.socket().getOutputStream());

                    String inputLine = getInput(rbc);
                    if(!inputLine.equals("")){
                        Command cmd = processCommand(inputLine);
                        wbc.write(ByteBuffer.wrap(cmd.toString().getBytes()));
                    }
                    else{
                        List<Command> commands = Controller.getNewCommands();
                        if(commands.size() > 0){
                            for(Command cmd : commands){
                                wbc.write(ByteBuffer.wrap(cmd.toString().getBytes()));
                            }
                        }
                    }
                }
            }

        } catch (IOException e) {
            ErrorLog.logMsg("Error while creating Server Socket", e);
        }
    }

    public String getInput(ReadableByteChannel rbc) {
        ByteBuffer b = ByteBuffer.allocate(8); // read 8 bytes 
        String ret = "";
        try {
            while (rbc.read(b) != -1) {
                b.flip();
                while (b.hasRemaining()) {
                    ret += b.toString();
                }
                b.clear();
            }
        }
        catch(IOException e){
            ErrorLog.logMsg("Error while reading from Socket", e);
        }
        return ret;
    }

    private Command processCommand(String cmd) {
        String prefix = cmd.split(":", 1)[0];
        switch (prefix) {
            case "PORT":
                return new Command("port", port);
            //Meer cases

            default:
                return null;
        }
    }
}
