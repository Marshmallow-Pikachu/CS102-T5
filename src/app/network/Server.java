package app.network;

import java.io.*;
import java.util.*;
import java.net.ServerSocket;
import java.net.Socket;

import app.entity.*;
import app.game.*;
import app.resource.*;
import app.utilities.AppUtils;
import app.utilities.Input;
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
                    for (String s : ClientHandler.playerList) {
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
            Game game = this.game;
            while (!game.getGameEnd()) {
                Player p = game.getCurrentPlayer();
                ClientHandler.broadcast(Printer.stringGameState(game));
                
                if (p instanceof BotPlayer b) {
                    Card playedCard = b.determineCardChoice(game);
                    game.nextTurn(playedCard);
                } else {
                Card playedCard = ClientHandler.promptPlayers(p.getName(), p);
                game.nextTurn(playedCard);
                }
            }
                // Let the game run until it is over

                Player gameEnder = game.getPlayers().getLast();
                if (gameEnder.hasSixColors()) {
                    ClientHandler.broadcast(String.format("%s has collected 6 colors!\n", gameEnder.getName()));
                    System.out.printf("%s has collected 6 colors!\n", gameEnder.getName());
                } else {
                    ClientHandler.broadcast("There are no more cards in the deck!\n");
                }
                for (int i = 0; i<game.getPlayers().size(); i++) {
                    ClientHandler.broadcast(Printer.stringGameState(game));

                    System.out.println("Final round!");
                
                    // Get the current Player
                    Player p = game.getCurrentPlayer();

                    // Check if the player is human or bot
                    if (p instanceof BotPlayer b) {
                        ClientHandler.promptPlayers(b.getName(), null);
                        Card playedCard = b.determineCardChoice(game);
                        game.nextTurn(playedCard);
                    } else {
                        Card playedCard = ClientHandler.promptPlayers(p.getName(), p);
                        game.nextTurn(playedCard);
                    }
                }

                // Initiate final round mechanic
                ClientHandler.promptDiscards(game.getPlayers());
                for (Player p : game.getPlayers()){
                    if (p instanceof HumanPlayer human) {
                        human.emptyHandToScoringArea();
                    } else {
                        BotPlayer bot = (BotPlayer) p;
                        bot.discardCards(game.getPlayers());
                        bot.emptyHandToScoringArea();
                    }
                }

                game.flipMajorityCards();
                String finalResult = Printer.stringGameState(game) + "\n" +  
                                     Printer.stringWinScreen(game) + "\nThank you for playing!\n";
                ClientHandler.broadcast(finalResult);
                
            }
            
            closeServerSocket();

            
        }
    


    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.err.println("Usage: java Server <port number>");
            System.exit(1);
         }
   
        int port = Integer.parseInt(args[0]);
        ServerSocket serverSocket = new ServerSocket(port);
        Server server = new Server(serverSocket, new int[]{2, 0});
        System.out.println("Server is online!");
        if (server.startServer()) {
            System.out.println("Ready to play game!");
        };
    }
}
