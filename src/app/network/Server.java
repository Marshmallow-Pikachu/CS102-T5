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


public class Server implements Runnable {
    
    // initialise the server socket to listen for cilents
    private ServerSocket serverSocket;
    private int[] playerCount;
    private Game game;

    // constructor for Server
    public Server(ServerSocket serverSocket, int[] playerCount) {
        this.serverSocket = serverSocket;
        this.playerCount = playerCount;
    }

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
            System.out.println(e.getMessage());
        }

        return true;

        

    }

    public void executeTurn() {
        Player p = game.getCurrentPlayer();
        ClientHandler.broadcast(Printer.stringGameState(game));
        ClientHandler.displayHands(game.getPlayers());

        if (p instanceof BotPlayer b) {
            Card playedCard = b.determineCardChoice(game);
            game.nextTurn(playedCard);
        } else {
        Card playedCard = ClientHandler.promptPlayers(game.getPlayers());
        game.nextTurn(playedCard);
        }
    }

    public void executeLastTurn() {
        // Let the game run until it is over
        Player gameEnder = game.getPlayers().getLast();
        String message = "";
        if (gameEnder.hasSixColors()) {
            message = String.format("%s has collected 6 colors!\n", gameEnder.getName());
        } else {
            message = "There are no more cards in the deck!\n";
        }
        message += "Final Round!\n";

        for (int i = 0; i<game.getPlayers().size(); i++) {
            ClientHandler.broadcast(Printer.stringGameState(game));
            ClientHandler.displayHands(game.getPlayers());

            // Get the current Player
            Player p = game.getCurrentPlayer();

            // Check if the player is human or bot
            if (p instanceof BotPlayer b) {
                ClientHandler.promptPlayers(null);
                Card playedCard = b.determineCardChoice(game);
                game.nextTurn(playedCard);
            } else {
                ClientHandler.broadcast(message);
                Card playedCard = ClientHandler.promptPlayers(game.getPlayers());
                game.nextTurn(playedCard);
            }
        }
    }

    public void executeDiscards() {
        ClientHandler.broadcast(Printer.stringGameState(game));
        ClientHandler.promptDiscards(game.getPlayers());
        for (Player p : game.getPlayers()){
            if (p instanceof HumanPlayer human) {
                human.emptyHandToScoringArea();
            } else{
                BotPlayer bot = (BotPlayer) p;
                bot.discardCards(game.getPlayers());
                bot.emptyHandToScoringArea();
            }
        }
    }

    public void showFinalGameResults() {
        game.flipMajorityCards();
        ClientHandler.broadcast(Printer.stringGameState(game));
        ClientHandler.broadcast(Printer.stringScoreList(game.calculateScore()));
        ClientHandler.broadcast(Printer.stringWinScreen(game));
        ClientHandler.broadcast("Thank you for playing!");

        ClientHandler.setConnection(false);
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
            try {
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
