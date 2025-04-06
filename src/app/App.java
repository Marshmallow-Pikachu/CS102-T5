package app;

import app.entity.*;
import app.game.*;
import app.network.Client;
import app.network.Server;
import app.resource.*;
import app.utilities.*;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.*;
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

    // Helper function for running an offline game
    /**
     * Runs the main game loop for an offline game, handling player turns, game state updates, and end game logic.
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
        String message = "";
        if (gameEnder.hasSixColors()) {
            message = String.format("%s has collected 6 colors!\n", gameEnder.getName());
        } else {
            message = "There are no more cards in the deck!";
        }
        message += "Final Round!\n";
        // Last round of playing cards
        for (int i = 0; i<game.getPlayers().size(); i++) {
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

        // Initiate final round mechanic
        for (Player p : players){
            if (p instanceof HumanPlayer) {
                HumanPlayer human = (HumanPlayer) p;
                for (int i = 0; i < 2; i ++) {
                    Card discard = human.discardCard(Input.askForDiscard(sc, human));
                    Printer.printDiscard(discard);
                }
                
                human.discardCard(Input.askForDiscard(sc, human));
                human.emptyHandToScoringArea();
            } else{
                BotPlayer bot = (BotPlayer)p;
                bot.discardCards(players);
                bot.emptyHandToScoringArea();
            }
        }

        game.flipMajorityCards();
        Printer.displayGameState(game);
        Printer.printScoreList(game.calculateScore());
        Printer.printWinScreen(game);
        sc.nextLine();
    }

    // Helper function for hosting an online game
    
    public static void hostGame(Scanner sc) {
        try {
            int[] players = Input.askForNumberOfPlayers(sc);

            ServerSocket serverSocket = new ServerSocket(25102, 0, Inet4Address.getByName("0.0.0.0"));
            Server server = new Server(serverSocket, players);

            Thread thread = new Thread(server);

            thread.start();
            
        } catch (IOException e) {
            System.out.println("Something went wrong with the server");
            e.printStackTrace();
        }
        joinGame(sc, "127.0.0.1");
    }

    public static void joinGame(Scanner sc, String ipAddress) {
        try {
            if (ipAddress == null) {
                System.out.print("Enter the server address: ");
                ipAddress = sc.nextLine();
            }
            

            System.out.print("Enter your name: ");
            String name = sc.nextLine();
            InetAddress serverAdd = Inet4Address.getByName(ipAddress);
            Socket socket = new Socket(serverAdd, 25102);
            Client client = new Client(socket, name);

            client.listenForMessage();
            client.sendMessage(sc);
            System.out.println("Press anything to continue");
        } catch (IOException e) {
            System.out.println("Unable to join server");
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