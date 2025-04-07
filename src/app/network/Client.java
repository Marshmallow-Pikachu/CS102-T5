package app.network;

import java.net.Socket;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.IOException;

/**
 * This class contains the implementation of a client.
 * It is used for each player to join the game server
 */
public class Client {
    
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String username;
    private boolean ready;
    private boolean gameEnd;

    /**
     * The constructor for a client
     * @param socket the socket to connect the client to the server
     * @param username the username of the player
     */
    public Client(Socket socket, String username) {

        try {
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.username = username;
            this.ready = false;
            this.gameEnd = false;
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }

    }

    /**
     * To send a message to the game server
     * The game server will send a message to activate the client to actually send 
     * a response to the server, else it will discard any other input
     * @param sc - The scanner to read inputs from terminal
     */
    public void sendMessage(Scanner sc) {
        try {
            bufferedWriter.write(username);
            bufferedWriter.newLine();
            bufferedWriter.flush();

            while (socket.isConnected() && !gameEnd) {
                String message = sc.nextLine();
                if (ready) {
                    bufferedWriter.write(message);
                    bufferedWriter.newLine();
                    bufferedWriter.flush();
                    ready = false;
                }
            }
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }

    }

    /**
     * Method to listen for messages from the game server. Uses a thread so that
     * we are able to listen to messages while continuing to read the terminal to
     * send messages
     */
    public void listenForMessage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String message;

                connected:
                while (socket.isConnected()) {
                    try {
                        message = bufferedReader.readLine();

                        switch (message) {
                            case null:
                                System.out.println("Something went wrong with the connection to server...");
                                gameEnd = true;
                                closeEverything(socket, bufferedReader, bufferedWriter);
                                break connected;
                            case "Thank you for playing":
                                System.out.println(message);
                                gameEnd = true;
                                closeEverything(socket, bufferedReader, bufferedWriter);
                                break connected;
                            case "Username must be between 1 to 80 characters long.":
                                System.out.println(message);
                                gameEnd = true;
                                closeEverything(socket, bufferedReader, bufferedWriter);
                                break connected;
                            case "Sorry this username has been taken :(":
                                System.out.println(message);
                                gameEnd = true;
                                closeEverything(socket, bufferedReader, bufferedWriter);
                                break connected;
                            case "Your turn":
                                ready = true;
                                break;
                            default:
                                System.out.println(message);
                        }
                    } catch (IOException e) {
                        closeEverything(socket, bufferedReader, bufferedWriter);
                    } catch (NullPointerException e) {
                        closeEverything(socket, bufferedReader, bufferedWriter);
                    } catch (Exception e) {
                        System.out.println("Something went wrong when trying to shutdown client");
                    } 
                }
                closeEverything(socket, bufferedReader, bufferedWriter);
                System.out.println("enter anything to continue!");
            }
        }).start();
    }

    /**
     * Method to shutdown the client
     * @param socket The client's socket
     * @param bufferedReader The client's bufferedReader that reads messages from server
     * @param bufferedWriter The client's bufferedWriter that sends messages to server
     */
    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
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
            
        }
    }
}
