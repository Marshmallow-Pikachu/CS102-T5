package app;

import app.entity.*;
import app.game.*;
import app.network.Client;
import app.network.Server;
import app.resource.*;
import app.utilities.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

/**
 * Main class to run and manage the game application. It handles game modes, player interactions,
 * and the game loop.
 */
public class App {
    /**
     * Default constructor for initializing the application.
     */
    public App() {

    }
    // Menu for selecting the game mode
    /**
     * Prompts the user's for the game mode. Offline, Create Online game, Join Online Game, or Quit
     * @param sc The scanner to read user input.
     * @return The chosen game mode as int.
     */
    public static int gameModeOption(Scanner sc) {
        boolean valid = false;
        int input = 0;
        while (!valid) {
            try {
                Printer.printMenu();
                System.out.printf("%nSelect on option > ");
                input = Integer.parseInt(sc.nextLine());
                if (input < 0 || input > 4) {
                    System.out.printf("%nInvalid Option :(%n%n");
                    continue;
                }
                valid = true;
            } catch (NumberFormatException e){
                System.out.printf("%nInvalid Option :(%n%n");
            }
        }
        return input;
    }

    // Helper function to create the arraylist of players to add to the game
    /**
     * Creates an ArrayList of players for the game, containing humans and/or bots players. Minimum 2 to start.
     * @param sc The scanner for user input.
     * @param deck The common deck instance.
     * @return An ArrayList of players that is initialised based on the input from scanner.
     */
    public static ArrayList<Player> getPlayerList(Scanner sc, Deck deck) {
        // initialise ArrayList<Player> to return players
        ArrayList<Player> players = new ArrayList<>();

        // Ask user for the amount of players to add
        boolean valid = false;
        System.out.print("How many human players (1-6): ");
        String input = sc.nextLine();
        
        if (input.equals("auto")) {
            // Default list of bot names to randomise
            ArrayList<String> paradeBotNames = new ArrayList<>();
            paradeBotNames.add("Alice");
            paradeBotNames.add("Mad Hatter");
            paradeBotNames.add("White Rabbit");
            paradeBotNames.add("Humpty Dumpty");
            paradeBotNames.add("Cheshire Cat");
            paradeBotNames.add("Dodo Bird");

            for (int i = 0; i < 6; i++) {
                players.add(new BotPlayer(deck, paradeBotNames.get(i)));
            };

            Collections.shuffle(players);
            return players;
        }

        while (!valid) {
            try {
                // Check if the user inputted a number
                int humans = Integer.parseInt(input);
                int bots = 0;

                // Check if the user inputted a number between 1 to 6
                if (humans < 1 | humans > 6) {
                    System.out.println("\nInvalid number of players.\n");
                    System.out.print("How many human players (1-6): ");
                    input = sc.nextLine();
                    continue;
                }

                // Now ask the user for number of bots if it is not 6 human players
                if (humans == 1) {
                    System.out.printf("How many bots (1-5): ");
                    input = sc.nextLine();
                    // Check if the input is an int
                    bots = Integer.parseInt(input);
                }

                else if (humans != 6) {
                    System.out.printf("How many bots (0-%d): ", (6 - humans));
                    input = sc.nextLine();
                    // Check if the input is an int
                    bots = Integer.parseInt(input);
                }

                // Check if the number isn't between 2 to 6
                if (humans + bots < 2 | humans + bots > 6 | bots < 0) {
                    System.out.println("\nInvalid number of players.\n");
                    System.out.print("How many human players (1-6): ");
                    input = sc.nextLine();
                    continue;
                } else {
                    // Once number is correct, can start creating players
                    valid = true;
                    System.out.println();

                    for (int i = 1; i < humans + 1; i++) {
                        System.out.printf("Name of player %d: ", i);
                        input = sc.nextLine();
                        players.add(new HumanPlayer(deck, input));
                    }

                    // Default list of bot names to randomise
                    ArrayList<String> paradeBotNames = new ArrayList<>();
                    paradeBotNames.add("Alice");
                    paradeBotNames.add("Mad Hatter");
                    paradeBotNames.add("White Rabbit");
                    paradeBotNames.add("Humpty Dumpty");
                    paradeBotNames.add("Cheshire Cat");
                    paradeBotNames.add("Dodo Bird");
                    Collections.shuffle(paradeBotNames);

                    for (int i = 0; i < bots; i++) {
                        players.add(new BotPlayer(deck, paradeBotNames.get(i)));
                    };
                }

            
            } catch (NumberFormatException e) {
                System.out.println("\nInvalid number of players.\n");
                System.out.print("How many human players (1-6): ");
                input = sc.nextLine();
            }

        }

        return players;
    } 

    // Helper function for running an offline game
    /**
     * Runs the main game loop for an offline game, handling player turns, game state updates, and end game logic.
     * @param sc The scanner for reading user inputs.
     */
    public static void offlineGame(Scanner sc) {
        
        // initialise deck and players to add to the game
        Deck deck = new Deck();
        deck.shuffle();
        ArrayList<Player> players = getPlayerList(sc, deck);
        Game game = new Game(players, deck);


        // Start the game loop 
        while (!game.getGameEnd()) {
            Printer.displayGameState(game);

            // Get the current Player
            Player player = game.getCurrentPlayer();

            // Check if the player is human or bot
            if (player instanceof BotPlayer b) {
                Card playedCard = b.determineCardChoice(game);
                game.nextTurn(playedCard);
            } else {
                ArrayList<Card> hand = player.getPlayerHand();
                Printer.printRenderedHand(player);
                Card playedCard = Input.askForCard(sc, hand);
                game.nextTurn(playedCard);
            }
        }  
        // Player that triggered game end
        Player gameEnder = game.getPlayers().getLast();
        if (gameEnder.hasSixColors()) {
            System.out.printf("%s has collected 6 colors!\n", gameEnder.getName());
        } else {
            System.out.println("There are no more cards in the deck!");
        }
        for (int i = 0; i<game.getPlayers().size(); i++) {
            Printer.displayGameState(game);

        System.out.println("Final round!");

            // Get the current Player
            Player player = game.getCurrentPlayer();

            // Check if the player is human or bot
            if (player instanceof BotPlayer b) {
                Card playedCard = b.determineCardChoice(game);
                game.nextTurn(playedCard);
            } else {
                ArrayList<Card> hand = player.getPlayerHand();
                Printer.printRenderedHand(player);
                Card playedCard = Input.askForCard(sc, hand);
                game.nextTurn(playedCard);
            }
        }

        // To do the discarding
        game.initiateFinalRound(players);

        // To print the last game state
        Printer.displayGameState(game);
        game.printWinScreen();
        System.out.println("enter anything to continue!");
        sc.nextLine();
        //Printer.clearScreen();
    }

    // Helper function for hosting an online game
    
    public static void hostGame(Scanner sc) {
        try {
            ServerSocket serverSocket = new ServerSocket(25000);
            Server server = new Server(serverSocket, 2);

            Thread thread = new Thread(server);

            thread.run();
            
        } catch (IOException e) {
            System.out.println("Something went wrong with the server");
            e.printStackTrace();
        }
    }

    public static void joinGame(Scanner sc) {
        try {
            Socket socket = new Socket("127.0.0.1", 25000);
            Client client = new Client(socket, "username");

            client.listenForMessage();
            client.sendMessage();
        } catch (IOException e) {
            System.out.println("Unable to join server");
            e.printStackTrace();
        }
    }

    /**
     * Main method initialises the application and handles user input in the game's menu.
     * @param args (not used
     * )
     */
    public static void main(String[] args) {
        // Initialise Scanner to read inputs
        Scanner sc = new Scanner(System.in);

        // Starting message of the program
        Printer.printLogo();

        // Ask the user what option they want
        int option = gameModeOption(sc);

        // This is the main game starting loop, everything inside is to 
        while (option != 4) {
            switch (option) {
                // Offline game
                case 1:
                    offlineGame(sc);
                    break;
                case 2:
                    hostGame(sc);
                    break;
                case 3:
                    joinGame(sc);
                    break;
            }
            option = gameModeOption(sc);
        }

        // Exiting message once the user is done playing
        System.out.println("\n Bye bye! Hope you had fun playing Parade! See you next time!\n");

        

    }
}
