package app;

import app.entity.*;
import app.game.*;
import app.network.Client;
import app.network.Server;
import app.resource.*;
import app.utilities.*;

import java.io.IOException;
import java.net.*;
import java.util.*;

/**
 * Main class to run and manage the game application. It handles game modes,
 * player interactions,
 * and the game loop.
 */
public class App {
    /**
     * Default constructor for initializing the application.
     */
    public App() {

    }

    /**
     * Helper method to execute one turn of the game
     * @param sc Scanner to read inputs from the terminal
     * @param game The parade game instance
     */
    public static void executeTurn(Scanner sc, Game game) {
        System.out.println("\n".repeat(5));
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

    /**
     * Helper method to execute the last turns of the game
     * @param sc Scanner to read inputs from the terminal
     * @param game The parade game instance
     */
    public static void executeLastTurn(Scanner sc, Game game) {
        // Get the message of why game is ending
        String message = Printer.stringGameEndMessage(game);

        // Last round of playing cards
        for (int i = 0; i < game.getPlayers().size(); i++) {
            System.out.println("\n".repeat(5));
            Printer.displayGameState(game);
            System.out.println(message);

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
    }

    /**
     * Helper method to get all players to discard two cards
     * @param sc Scanner to read inputs from the terminal
     * @param game The parade game instance
     */
    public static void executeDiscard(Scanner sc, Game game) {
        ArrayList<Player> players = game.getPlayers();
        for (Player p : players) {
            if (p instanceof HumanPlayer) {
                HumanPlayer human = (HumanPlayer) p;
                for (int i = 0; i < 2; i++) {
                    Card discard = human.discardCard(Input.askForDiscard(sc, human));
                    Printer.printDiscard(discard);
                }

                human.discardCard(Input.askForDiscard(sc, human));
                human.emptyHandToScoringArea();
            } else {
                BotPlayer bot = (BotPlayer) p;
                bot.discardCards(players);
                bot.emptyHandToScoringArea();
            }
        }
    }

    /**
     * Helper method to show the final results of the game
     * @param game the parade game instance
     */
    public static void showFinalGameResults(Game game) {
        game.flipMajorityCards();
        Printer.displayGameState(game);
        Printer.printScoreList(game.calculateScore());
        Printer.printWinScreen(game);
    }

    /**
     * Runs the main game loop for an offline game, handling player turns, game
     * state updates, and end game logic.
     * 
     * @param sc The scanner for reading user inputs.
     */
    public static void offlineGame(Scanner sc) {

        // initialise deck and players to add to the game
        Deck deck = new Deck();
        deck.shuffle();
        ArrayList<Player> players = Input.getPlayerList(sc, deck);
        Game game = new Game(players, deck);

        // Start the game loop
        while (!game.getGameEnd()) {
            executeTurn(sc, game);
        }

        // Initiate final round mechanic
        executeLastTurn(sc, game);
        executeDiscard(sc, game);
        showFinalGameResults(game);

        // To let the user see the final game results before showing the menu again
        sc.nextLine();
    }

    /**
     * Method to create the game server, it will run joinGame with localhost as address
     * after setting up the server
     * @param sc Scanner to read inputs from the terminal
     */
    public static void hostGame(Scanner sc) {
        String name = null;
        try {
            name = Input.getUsername("Enter your username: ", sc, null);

            int[] players = Input.askForNumberOfPlayers(sc);

            ServerSocket serverSocket = new ServerSocket(25102, 0, Inet4Address.getByName("0.0.0.0"));
            Server server = new Server(serverSocket, players);

            Thread thread = new Thread(server);

            thread.start();

        } catch (IOException e) {
            if (e.getMessage().equals("Address already in use")) {
                System.out.println("Found a game being hosted on this device, joining it instead");
            } else {
                System.out.println("Something went wrong with the server");
            }
            

        }
        joinGame(sc, name, "127.0.0.1");
    }

    /**
     * Method for host to join the game server
     * @param sc Scanner to read inputs from the terminal
     * @param ipAddress The ip address of the game server
     */
    public static void joinGame(Scanner sc, String name, String ipAddress) {
        try {
            InetAddress serverAdd = Inet4Address.getByName(ipAddress);
            Socket socket = new Socket(serverAdd, 25102);
            Client client = new Client(socket, name);

            client.listenForMessage();
            client.sendMessage(sc);
        } catch (IOException e) {
            System.out.println("Unable to join server");
        }
    }

    /**
     * Method to join the game server
     * @param sc Scanner to read inputs from the terminal
     * @param ipAddress The ip address of the game server
     */
    public static void joinGame(Scanner sc, String ipAddress) {
        try {
            System.out.print("Enter the server address: ");
            ipAddress = sc.nextLine();

            System.out.print("Enter your name: ");
            String name = sc.nextLine();
            InetAddress serverAdd = Inet4Address.getByName(ipAddress);
            Socket socket = new Socket(serverAdd, 25102);
            Client client = new Client(socket, name);

            client.listenForMessage();
            client.sendMessage(sc);
        } catch (IOException e) {
            System.out.println("Unable to join server");
        }
    }

    /**
     * Main method initialises the application and handles user input in the game's
     * menu.
     * 
     * @param args (not used)
     */
    public static void main(String[] args) {
        // Initialise Scanner to read inputs
        Scanner sc = new Scanner(System.in);

        // Starting message of the program
        Printer.printLogo();

        // Ask the user what option they want
        int option = Input.gameModeOption(sc);

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
                    joinGame(sc, null);
                    break;
            }
            option = Input.gameModeOption(sc);
        }

        // Exiting message once the user is done playing
        System.out.println("\n Bye bye! Hope you had fun playing Parade! See you next time!\n");
    }
}