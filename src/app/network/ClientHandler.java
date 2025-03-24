package app.network;

import java.util.ArrayList;

import java.net.Socket;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.InputStreamReader;
import java.io.IOException;

public class ClientHandler implements Runnable{
    
    // To hold all the clients
    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    
    private Socket socket;                  // socket to connect
    private BufferedReader bufferedReader; // for reading client inputs
    private BufferedWriter bufferedWriter; // for sending data to client
    private String clientUsername;

    public ClientHandler(Socket socket) {

        try {
            this.socket = socket;
            // getOutputStream returns a byte stream
            // OutputStreamWriter will convert the byte stream to character stream
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            
            // Similarly for inputs
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            
            // ReadLine is the same as nextLine for console
            this.clientUsername = bufferedReader.readLine();

            // Add this new client to our list of clients
            clientHandlers.add(this);

            // To send message to all clients
            broadcastMessage("SERVER: " + clientUsername + " has joined!");
            
            // Log the username of the client in the server
            System.out.println(this.clientUsername + " has joined the game!");
        } catch (IOException e) {
           closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    // We need to use this function to listen for messages
    // If we did not split new threads with this, we will have to wait for 
    // each client to send a message first before we can do anything
    @Override
    public void run() {
        String messageFromClient;

        while (socket.isConnected()) {
            try {
                
                // Waiting for client to send a message
                messageFromClient = bufferedReader.readLine();

                // Send back the message to all clients
                broadcastMessage(messageFromClient);

            } catch (IOException e) {
                closeEverything(socket, bufferedReader, bufferedWriter);
                break; // run() is a while loop, we need to stop it once connection closes
            }
        }
    }

    public void broadcastMessage(String messageToSend) {
        // for each clientHandler in the array list client handlers
        for (int i = 0; i<clientHandlers.size(); i++) {
            try {
                ClientHandler clientHandler = clientHandlers.get(i);
                // Prevent sending the same message back to yourself
                if (!clientHandler.clientUsername.equals(clientUsername)) {
                    clientHandler.bufferedWriter.write(messageToSend); // send the message
                    clientHandler.bufferedWriter.newLine();            // create a new line for each client receiving msg
                    clientHandler.bufferedWriter.flush();              // In case the buffer is not full, fill up the buffer so message can be sent
                }
            } catch (IOException e) {
                closeEverything(socket, bufferedReader, bufferedWriter);
            } catch (NullPointerException e) {
                closeEverything(socket, bufferedReader, bufferedWriter);
            }
        }
        
        
    } 

    public void removeClientHander() {
        if (clientHandlers.contains(this)){
            clientHandlers.remove(this);
            System.out.println(this.clientUsername + " has left the chat...");
            broadcastMessage("SERVER: " + this.clientUsername + " has left the chat!");
        }
    }

    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        removeClientHander();

        // prevent NullPointerException
        try {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
