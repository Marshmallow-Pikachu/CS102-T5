package app;

import java.util.*;

import app.game.*;
import app.entity.*;
import app.resource.*;
import app.utilities.*;


public class App {
    // Helper function to check whether the user would like to start the game or stop playing
    public static boolean startGame(Scanner sc) {
        // Prompt the user to start the game
        System.out.print("Start a new game? (Y/N): ");
        String start = sc.nextLine();

        // If the user enters n or N, stop asking the user
        while (!start.toUpperCase().equals("N")) {
            // If the user doesn't enter y or Y, that means that the inputs are invalid
            // and we should we reprompt the user for another input
            if (!start.toUpperCase().equals("Y")) {
                System.out.println("\nInvalid command. Try again please :)\n");
                System.out.print("Start a new game? (Y/N): ");
                start = sc.nextLine();
                continue;
            }

            // if the user enters y or Y, the game will start
            return true;
        }

        // If the user enters n or N, the game will stop
        return false;
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
            Collections.shuffle(paradeBotNames);

            for (int i = 0; i < 6; i++) {
                players.add(new BotPlayer(deck, paradeBotNames.get(i)));
            };
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
                if (humans != 6) {
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
    public static void main(String[] args) {
        // Initialise Scanner to read inputs
        Scanner sc = new Scanner(System.in);

        // Starting message of the program
        System.out.println("Welcome to Parade!\n");

        // This is the main game starting loop, everything inside is to 
        while (startGame(sc)) {
            System.out.println("Play game");

            // initialise deck and players to add to the game
            Deck deck = new Deck();
            deck.shuffle();
            ArrayList<Player> players = getPlayerList(sc, deck);
            Game game = new Game(players, deck);
            

            // Start the game loop 
            while (!game.getGameEnd()) {
                game.initiateRound();
                System.out.printf("%n%n");
            }
            game.initiateFinalRound(players);
            game.displayGameState(game);
            System.out.println("Winner is : " + game.determineWinner().getName());
            
        }

        // Exiting message once the user is done playing
        System.out.println("\n Bye bye! Hope you had fun playing Parade! See you next time!\n");

        

    }
}
