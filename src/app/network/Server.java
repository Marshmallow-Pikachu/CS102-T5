package app.network;

import java.io.*;
import java.util.*;
import java.net.ServerSocket;
import java.net.Socket;

import app.entity.*;
import app.game.*;
import app.resource.*;
import app.utilities.AppUtils;
import app.utilities.Printer;

/**
 * This class contains the implementation of a Game server.
 * The logic here is similar to offline game in App, but with a different set of
 * functions that utilises java.net
 */
public class Server implements Runnable {

    // initialise the server socket to listen for cilents
    private ServerSocket serverSocket;
    private int[] playerCount;
    private Game game;

    /**
     * Constructor for the game server
     * @param serverSocket The server's socket so that it is able to listen for other clients.
     * @param playerCount An int array that contains the number of human and bot players.
     */
    public Server(ServerSocket serverSocket, int[] playerCount) {
        this.serverSocket = serverSocket;
        this.playerCount = playerCount;
    }

    /**
     * This method is used to connect the clients to the game server and set up the
     * game.
     * It will listen for clients who are joining, validate their username, and then
     * connect them.
     * 
     * @return true when there are enough players to start the game
     */
    public boolean startServer() {

        try {
            ArrayList<Player> players = new ArrayList<>();
            Deck deck = new Deck();
            deck.shuffle();

            // Reset the client handlers
            ClientHandler.setConnection(true);

            // keep the server up until the socket is closed
            while (!serverSocket.isClosed()) {

                // Listen for a new player
                Socket socket = serverSocket.accept();

                // to allow more than one client to connect on our server
                // Split each cilent to a different thread
                ClientHandler clientHandler = new ClientHandler(socket);

                // The thread will invoke the run() method in clientHandler
                Thread thread = new Thread(clientHandler);
                thread.start();

                if (ClientHandler.getSize() == this.playerCount[0]) {
                    for (String s : ClientHandler.clientHandlers.keySet()) {
                        players.add(new HumanPlayer(deck, s));
                    }
                    ArrayList<Player> botPlayers = AppUtils.generateBotPlayers(playerCount[1], deck);
                    players.addAll(botPlayers);

                    Collections.shuffle(players);

                    this.game = new Game(players, deck);

                    return true;
                }
            }

        } catch (Exception e) {
            System.out.println("Something went wrong with startServer()");
        }

        return true;

    }

    /**
     * Helper function to run one turn in the game
     */
    public void executeTurn() {
        // Print out the current game state
        ClientHandler.broadcast(Printer.stringGameState(game));
        ClientHandler.displayHands(game.getPlayers());

        // Get the current player to play their turn
        Player p = game.getCurrentPlayer();
        if (p instanceof BotPlayer b) {
            Card playedCard = b.determineCardChoice(game);
            game.nextTurn(playedCard);
        } else {
            Card playedCard = ClientHandler.promptPlayers(game.getPlayers());
            game.nextTurn(playedCard);
        }
    }

    /**
     * Helper function to run the last turns in the game.
     * Similar to executeTurn, but will inform each player why the game is on its
     * last round.
     */
    public void executeLastTurn() {
        // Get the last round reason
        Player gameEnder = game.getPlayers().getLast();
        String message = "";
        if (gameEnder.hasSixColors()) {
            message = String.format("%s has collected 6 colors!\n", gameEnder.getName());
        } else {
            message = "There are no more cards in the deck!\n";
        }
        message += "Final Round!\n";

        // Run the last turn for each of the players
        for (int i = 0; i < game.getPlayers().size(); i++) {
            // Show the current game state
            ClientHandler.broadcast(Printer.stringGameState(game));
            ClientHandler.displayHands(game.getPlayers());

            // Get the current Player
            Player p = game.getCurrentPlayer();

            // Check if the player is human or bot
            if (p instanceof BotPlayer b) {
                Card playedCard = b.determineCardChoice(game);
                game.nextTurn(playedCard);
            } else {
                ClientHandler.broadcast(message);
                Card playedCard = ClientHandler.promptPlayers(game.getPlayers());
                game.nextTurn(playedCard);
            }
        }
    }

    /**
     * Helper function to help ask each player to discard their cards.
     */
    public void executeDiscards() {
        ClientHandler.broadcast(Printer.stringGameState(game));
        ClientHandler.promptDiscards(game.getPlayers());
        for (Player p : game.getPlayers()) {
            if (p instanceof HumanPlayer human) {
                human.emptyHandToScoringArea();
            } else {
                BotPlayer bot = (BotPlayer) p;
                bot.discardCards(game.getPlayers());
                bot.emptyHandToScoringArea();
            }
        }
    }

    /**
     * Helper function to display the final game results.
     */
    public void showFinalGameResults() {
        game.flipMajorityCards();
        ClientHandler.broadcast(Printer.stringGameState(game));
        ClientHandler.broadcast(Printer.stringScoreList(game.calculateScore()));
        ClientHandler.broadcast(Printer.stringWinScreen(game));
        ClientHandler.broadcast("Thank you for playing!");

        ClientHandler.setConnection(false);
    }

    /**
     * To shutdown the server.
     */
    public void closeServerSocket() {
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * To override the abstract method in Runnable.
     * Similar to a main method, this is used to run the game server on a different
     * thread,
     * so that we can still allow the user to use the terminal as the server will
     * need to
     * continuously listen and send messages to and fro the clients.
     */
    @Override
    public void run() {
        // Initiate the game server
        if (startServer()) {
            try {
                // Start running the game
                System.out.println("Ready to play game!");
                Game game = this.game;
                while (!game.getGameEnd()) {
                    executeTurn();

                }
                // Initiate final round mechanic
                executeLastTurn();
                executeDiscards();
                showFinalGameResults();
            } catch (Exception e) {
                System.out.println("Something went wrong...");
                e.printStackTrace();
            }
            ClientHandler.broadcast("Something went wrong...");
            closeServerSocket();
        }

    }

}
