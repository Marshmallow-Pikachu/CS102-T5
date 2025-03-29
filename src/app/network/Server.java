package app.network;

import java.io.*;

import java.net.ServerSocket;
import java.net.Socket;
import app.network.ClientHandler;


public class Server implements Runnable {
    
    // initialise the server socket to listen for cilents
    private ServerSocket serverSocket;
    private int playerCount;

    // constructor for Server
    public Server(ServerSocket serverSocket, int playerCount) {
        this.serverSocket = serverSocket;
        this.playerCount = playerCount;
    }

    public boolean startServer() {
        
        try {
            // keep the server up until the socket is closed
            while (!serverSocket.isClosed()) {

                Socket socket = serverSocket.accept();
                System.out.println("A new client has connected!");

                // to allow more than one client to connect on our server
                // Split each cilent to a different thread
                ClientHandler clientHandler = new ClientHandler(socket);

                // The thread will invoke the run() method in clientHandler
                Thread thread = new Thread(clientHandler);
                thread.start();

                if (ClientHandler.getSize() == this.playerCount) {
                    return true;
                }
            }   

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return true;

        

    }

    public void closeServerSocket() {
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        if (startServer()) {
            System.out.println("Ready to play game!");
            closeServerSocket();;
        }
    }

    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.err.println("Usage: java Server <port number>");
            System.exit(1);
         }
   
        int port = Integer.parseInt(args[0]);
        ServerSocket serverSocket = new ServerSocket(port);
        Server server = new Server(serverSocket, 2);
        System.out.println("Server is online!");
        if (server.startServer()) {
            System.out.println("Ready to play game!");
        };
    }
}
