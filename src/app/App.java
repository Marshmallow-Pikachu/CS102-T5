package app;

import app.entity.*;
import app.game.*;
import app.resource.*;
import app.utilities.*;
import java.util.*;


public class App {
    // Menu for selecting the game mode
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
        game.initiateFinalRound(players);
        Printer.displayGameState(game);
        game.printWinScreen();
        System.out.println("enter anything to continue!");
        sc.nextLine();
        //Printer.clearScreen();
    }

    // Helper function for hosting an online game
    public static void hostGame(Scanner sc) {
        
    }

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
                    System.out.printf("%nHave yet to implement%n%n");
                    break;
                case 3:
                    System.out.printf("%nHave yet to implement%n%n");
                    break;
            }
            option = gameModeOption(sc);
        }

        // Exiting message once the user is done playing
        System.out.println("\n Bye bye! Hope you had fun playing Parade! See you next time!\n");

        

    }
}
