package app.network;

import java.util.*;

import app.entity.Player;
import app.entity.HumanPlayer;
import app.resource.Card;
import app.utilities.Stringer;

import java.net.Socket;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.InputStreamReader;
import java.io.IOException;

/**
 * This class is used to handle all the clients input and outputs.
 * The static methods are used to send and recieve messages to all clients.
 * The instance methods are used to send and recieve messages to just one client.
 */
public class ClientHandler implements Runnable {
    
    // To hold all the clients
    public static Map<String, ClientHandler> clientHandlers = new HashMap<>();
    public static boolean gameOngoing = false;
    
    private Socket socket;                  // socket to connect
    private BufferedReader bufferedReader; // for reading client inputs
    private BufferedWriter bufferedWriter; // for sending data to client
    private String clientUsername;
    
    /**
     * The constructor for the ClientHandler
     * @param socket the socket for the client
     */
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
                this.socket.close();
                return;
            }
            
            // To send message to all clients
            String playerListMsg = "Players:\n";
            for (ClientHandler c : clientHandlers.values()) {
                playerListMsg += String.format("%s\n", c.getName());
            }
            broadcast(playerListMsg);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Getter method to return the number of clients connected
     * @return the number of clients connected
     */
    public static int getSize() {
        return clientHandlers.size();
    }

    /**
     * Getter method to return the client's username
     * @return the client's username
     */
    public String getName() {
        return this.clientUsername;
    }
    
    /**
     * The main function to allow the clientHandler code to keep functioning
     * while the game is ongoing.
     */
    @Override
    public void run() {
        while (socket.isConnected()) {
            if (!ClientHandler.gameOngoing) {
                break;
            }
        }
        closeEverything();
    }
    
    /**
     * Static method for ClientHandler to validate the username of a client before adding them into the server
     * @param clientHandler The clientHandler to be added
     * @param username The client's username
     * @return whether the client's username is valid or not
     * @throws IOException
     */
    public static boolean addClientHandler(ClientHandler clientHandler, String username) throws IOException {
        List<String> paradeBotNames = List.of("Alice", "Mad Hatter", "White Rabbit", 
        "Humpty Dumpty", "Cheshire Cat", "Dodo Bird");
        if (username.length() > 80 || username.length() < 1) {
            clientHandler.sendMessage("username must be between 1 to 80 characters long."); 
            return false;
        }
        if (!clientHandlers.keySet().contains(username) && paradeBotNames.indexOf(username) == -1) {
            clientHandlers.put(username, clientHandler);
            return true;
        }
        clientHandler.sendMessage("Sorry this username has been taken :("); 
        return false;
    }
    
    /**
     * To remove the client handler from ClientHandlers.
     * It will be used when our game is finished
     */
    public void removeClientHander() {
        if (clientHandlers.keySet().contains(this.clientUsername)){
            clientHandlers.remove(this.clientUsername);
        }
    }

    /**
     * Method used to display each player's hand
     * @param players The list of players currently in the game
     */
    public static void displayHands(ArrayList<Player> players) {
        for (Player player : players) {
            try {
                if (player instanceof HumanPlayer) {
                    ClientHandler clientHandler = clientHandlers.get(player.getName());
                    clientHandler.sendMessage(Stringer.stringRenderedHand(player));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Method to return the clientHandler of the current player,
     * It will also let other players know whose turn is it
     * @param players the list of players in the game
     * @return The clientHandler of the current player
     * @throws IOException
     */
    public static ClientHandler getCurrentClientToTakeTurn(ArrayList<Player> players) throws IOException {
        // Get the name of the current player
        String currentName = players.get(0).getName();
        
        // Let everyone else know whose turn is it
        for (String name : clientHandlers.keySet()) {
            if (!currentName.equals(name)) {
                ClientHandler clientHandler = clientHandlers.get(name);
                clientHandler.sendMessage(String.format("Waiting for %s to take their turn...", currentName));
            }
        }
        return clientHandlers.get(currentName);
    }

    /**
     * The method is to ask the current player to play a card
     * @param players the list of players playing the game
     * @return the card the current player wants to play
     */
    public static Card promptPlayers(ArrayList<Player> players) {
        // This for loop is to display either the player's hand or the waiting command
        try {
            ClientHandler currentHandler = getCurrentClientToTakeTurn(players);
            Player currentPlayer = players.get(0);
            
            int cardSelectedIndex = -1; 
            String input = null;
            while (true) {
                try {
                    // To number the cards the player can play
                    currentHandler.sendMessage("Enter a number between 1 to 5 to select a card: ");

                    // To trigger the client to be able to send us a message
                    currentHandler.activate();
                    input = currentHandler.bufferedReader.readLine();
                    
                    cardSelectedIndex = Integer.parseInt(input) - 1; // the -1 is because number between 1 to 5. Arraylist is 0 indexed
                    if (cardSelectedIndex < 0 || cardSelectedIndex > 4) {
                        throw new IllegalArgumentException(); // catch below
                    }
                    break; //if didnt throw any exception, break
                } catch (IOException e) {
                    System.out.println("Sockets went wrong");
                    e.printStackTrace();
                } catch (Exception e) {
                    currentHandler.sendMessage("It is not a valid option");
                } 
            }
            return currentPlayer.getPlayerHand().get(cardSelectedIndex);

        } catch (IOException e) {
            System.out.println("Something went wrong with promptPlayers");
        } catch (NullPointerException e) {
            System.out.println("Unable to find the clientHandler");
        }

        // Smth would be wrong if it ends up here
        return null;
    }

    /**
     * This method prompts each user to discard two cards
     * @param players the list of players in the game
     */
    public static void promptDiscards(ArrayList<Player> players) {
        try {
            ClientHandler.displayHands(players);
            for (int i = 0; i < players.size(); i++) {
                Player player = players.get(0);
                if (player instanceof HumanPlayer human) {
                    ClientHandler currentHandler = getCurrentClientToTakeTurn(players);
                    int count = 0;
                    while (count < 2) {
                        try {
                            if (count != 0) {
                                currentHandler.sendMessage(Stringer.stringRenderedHand(player));
                            }
                            currentHandler.sendMessage("Select a card to discard (1 to " + human.getPlayerHand().size() + "): ");
                            currentHandler.activate();
                            String input = currentHandler.bufferedReader.readLine();
                            int cardSelectedIndex = Integer.parseInt(input) - 1; // the -1 is because number between 1 to 5. Arraylist is 0 indexed
                            Card discarded = human.getPlayerHand().get(cardSelectedIndex);
                            human.discardCard(cardSelectedIndex);
                            currentHandler.sendMessage(Stringer.stringDiscard(discarded));
                            count++;
                        } catch (IllegalArgumentException e) {
                            currentHandler.sendMessage("It is not a valid option");
                        }
                    }
                }
                // rotate the players
                players.remove(player);
                players.add(player);
            }
        } catch (IOException e) {
            System.out.println("Something went wrong with promptDiscards");
            e.printStackTrace();
        } catch (NullPointerException e) {
            System.out.println("Something went wrong with finding a clientHandler or player");
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Something else went wrong with promptDiscards");
            e.printStackTrace();
        }
    }

    /**
     * This method is for sending a message to all players
     * @param message the message to be sent
     */
    public static void broadcast(String message) {
        // for each clientHandler in the array list client handlers
        for (ClientHandler clientHandler : clientHandlers.values()) {
            try {
                clientHandler.sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * This method is to reset the class ClientHandler so that it is able to create a new game server
     * @param bool whether to switch on the ClientHandler for game server or off to reset
     */
    public static void setConnection(boolean bool) {
        if (bool) {
            gameOngoing = true;
        } else {
            gameOngoing = false;
            clientHandlers = new HashMap<>();
        }
        
    }

    /**
     * Instance method for clientHandler to send message to a client
     * @param message the message to be sent
     * @throws IOException
     */
    public void sendMessage(String message) throws IOException {
        this.bufferedWriter.write(message);
        this.bufferedWriter.newLine();
        this.bufferedWriter.flush();
    }

    /**
     * Instance method to activate a client to send a response to us
     * @throws IOException
     */
    public void activate() throws IOException {
        this.sendMessage("Your turn");
    }

    /**
     * Instance method to shutdown the clientHandler
     */
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
