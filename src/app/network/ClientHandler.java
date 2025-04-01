package app.network;

import java.util.ArrayList;

import app.entity.Player;
import app.entity.HumanPlayer;
import app.resource.Card;
import app.utilities.Printer;

import java.net.Socket;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.InputStreamReader;
import java.io.IOException;

public class ClientHandler implements Runnable {
    
    // To hold all the clients
    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    public static ArrayList<String> playerList = new ArrayList<>();
    
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

            // Add this new client to our list of clients if the username hasn't existed
            if (!addClientHandler(this, clientUsername)) {
                this.bufferedWriter.write("Sorry, the username you have used is taken :("); // send the message
                this.bufferedWriter.newLine();            // create a new line for each client receiving msg
                this.bufferedWriter.flush(); 
                this.socket.close();
                return;
            }

            // To send message to all clients
            String playerListMsg = "Players:\n";
            for (ClientHandler c : clientHandlers) {
                playerListMsg += String.format("%s\n", c.getName());
            }
            broadcastMessage(playerListMsg);
            
            // Log the username of the client in the server
            System.out.println(this.clientUsername + " has joined the game!");
        } catch (IOException e) {
           e.printStackTrace();
        }
    }

    public String getName() {
        return this.clientUsername;
    }

    public static int getSize() {
        return clientHandlers.size();
    }
    // We need to use this function to listen for messages
    // If we did not split new threads with this, we will have to wait for 
    // each client to send a message first before we can do anything
    @Override
    public void run() {
        while (socket.isConnected()) {
        }
        closeEverything();
    }

    public static boolean addClientHandler(ClientHandler clientHandler, String username) {
        ArrayList<String> paradeBotNames = new ArrayList<>();
        paradeBotNames.add("Alice");
        paradeBotNames.add("Mad Hatter");
        paradeBotNames.add("White Rabbit");
        paradeBotNames.add("Humpty Dumpty");
        paradeBotNames.add("Cheshire Cat");
        paradeBotNames.add("Dodo Bird");
        if (playerList.indexOf(username) == -1 && paradeBotNames.indexOf(username) == -1) {
            clientHandlers.add(clientHandler);
            playerList.add(username);
            return true;
        }
        return false;
    }

    public static Card promptPlayers(String name, Player current) {

        // for each clientHandler in the array list client handlers
        for (int i = 0; i<clientHandlers.size(); i++) {
            try {
                ClientHandler clientHandler = clientHandlers.get(i);
                if (clientHandler.getName().equals(name)) {
                    
                    clientHandler.bufferedWriter.write(Printer.stringRenderedHand(current));
                    clientHandler.bufferedWriter.newLine();
                    clientHandler.bufferedWriter.flush();
                } else {
                    clientHandler.bufferedWriter.write(String.format("Waiting for %s to take their turn...", name));
                    clientHandler.bufferedWriter.newLine();
                    clientHandler.bufferedWriter.flush();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }

        if (current == null) {
            return null;
        }

        try {
            for (int i = 0; i < clientHandlers.size(); i++) {
                ClientHandler clientHandler = clientHandlers.get(i);
                if (clientHandler.getName() == name) {
                    int cardSelectedIndex = -1; 
                    String input = null;
                    while (true) {
                        try {
                            // To number the cards the player can play
                            clientHandler.bufferedWriter.write(String.format("Enter a number between 1 to 5 to select a card: "));
                            clientHandler.bufferedWriter.newLine();
                            clientHandler.bufferedWriter.flush();

                            clientHandler.bufferedWriter.write("Your turn");
                            clientHandler.bufferedWriter.newLine();
                            clientHandler.bufferedWriter.flush();

                            input = clientHandler.bufferedReader.readLine();
                            cardSelectedIndex = Integer.parseInt(input) - 1; // the -1 is because number between 1 to 5. Arraylist is 0 indexed
                            if (cardSelectedIndex < 0 || cardSelectedIndex > 4) {
                                throw new IllegalArgumentException(); // catch below
                            }
                            break; //if didnt throw any exception, break
                        } catch (Exception e) {
                            clientHandler.bufferedWriter.write("It is not a valid option");
                            clientHandler.bufferedWriter.newLine();
                            clientHandler.bufferedWriter.flush();
                        } 
                    }
                    return current.getPlayerHand().get(cardSelectedIndex);
                }
            }
        } catch (IOException e) {
            
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        // Smth would be wrong if it ends up here
        return null;
    }

    public static Card promptDiscards(ArrayList<Player> players) {
        try {
            for (int i = 0; i < clientHandlers.size(); i++) {
                ClientHandler clientHandler = clientHandlers.get(i);
                for (Player p : players) {
                    if (p instanceof HumanPlayer h) {
                        if (h.getName().equals(clientHandler.getName())) {
                            clientHandler.bufferedWriter.write(Printer.stringRenderedHand(h));
                            clientHandler.bufferedWriter.newLine();
                            clientHandler.bufferedWriter.flush();
                            

                            //TODO: Write the discard code
                            int count = 0;
                            while (count < 2) {
                                try {
                                    // To number the cards the player can play
                                    clientHandler.bufferedWriter.write("Select a card to discard (1 to " + h.getPlayerHand().size() + "): ");
                                    clientHandler.bufferedWriter.newLine();
                                    clientHandler.bufferedWriter.flush();
                                    clientHandler.bufferedWriter.write("Your turn");
                                    clientHandler.bufferedWriter.newLine();
                                    clientHandler.bufferedWriter.flush();
                                    String input = clientHandler.bufferedReader.readLine();
                                    int cardSelectedIndex = Integer.parseInt(input) - 1; // the -1 is because number between 1 to 5. Arraylist is 0 indexed
                                    if (cardSelectedIndex < 0 || cardSelectedIndex > h.getPlayerHand().size() -1  ) {
                                        throw new IllegalArgumentException(); // catch below
                                    }
                                    Card discarded = h.getPlayerHand().get(cardSelectedIndex);
                                    h.discardCard(cardSelectedIndex);
                                    clientHandler.bufferedWriter.write(Printer.stringDiscard(discarded));
                                    count++;
                                } catch (Exception e) {
                                    clientHandler.bufferedWriter.write("It is not a valid option");
                                    clientHandler.bufferedWriter.newLine();
                                    clientHandler.bufferedWriter.flush();
                                } 
                            }
                            // Read the first input
                            // Validate
                            // Remove if correct

                            // Read the second input
                            // Validate
                            // Remove if correct

                        }
                    }
                }
            }
        } catch (IOException e) {
            
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        // Smth would be wrong if it ends up here
        return null;
    }


    public static void broadcast(String message) {
        // for each clientHandler in the array list client handlers
        for (int i = 0; i<clientHandlers.size(); i++) {
            try {
                ClientHandler clientHandler = clientHandlers.get(i);
                clientHandler.bufferedWriter.write(message); // send the message
                clientHandler.bufferedWriter.newLine();            // create a new line for each client receiving msg
                clientHandler.bufferedWriter.flush();              // In case the buffer is not full, fill up the buffer so message can be sent
            
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
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
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
        
        
    } 

    public void removeClientHander() {
        if (clientHandlers.contains(this)){
            clientHandlers.remove(this);
        }
    }

    public void closeEverything() {
        removeClientHander();

        // prevent NullPointerException
        try {
            if (this.bufferedReader != null) {
                this.bufferedReader.close();
            }
            if (this.bufferedWriter != null) {
                this.bufferedWriter.close();
            }
            if (this.socket != null) {
                this.socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
