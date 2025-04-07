package app.utilities;

import app.entity.HumanPlayer;
import app.entity.Player;
import app.resource.Card;
import app.resource.Deck;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/**
 * This class contains utility methods for handling user inputs.
 */
public class Input {

    /**
     * Default Contructor of Input
     */
    public Input() {}
    /**
     * Prompts the user to select a card from their hand by entering a number representing the card's position in playerHand.
     * This method checks for a valid index, and will reprompt the user until a valid number is entered.
     * Numbers outside 1-5 are invalid, as well as other inputs.
     * @param sc The scanner used for input reading.
     * @param myHand An ArrayList of type Card representing the player's current hand.
     * @return The Card selected by the user.
     * 
     */
    public static Card askForCard(Scanner sc, ArrayList<Card> myHand) {
        int cardSelectedIndex = -1; 
        String input = null;
        while (true) {
            try {
                 // To number the cards the player can play
                System.out.print("Enter a number between 1 to 5 to select a card: ");    
                input = sc.nextLine();
                cardSelectedIndex = Integer.parseInt(input) - 1; // the -1 is because number between 1 to 5. Arraylist is 0 indexed
                if (cardSelectedIndex < 0 || cardSelectedIndex > 4) {
                    throw new IllegalArgumentException(); // catch below
                }
                break; //if didnt throw any exception, break
            } catch (Exception e) {
                System.out.println(String.format("%s is not a valid input", input));
            } 
        }
        return myHand.get(cardSelectedIndex);
    }

    /**
     * Prompts the user to discard a card from their hand by entering a number representing the card's position in playerHand.
     * This method checks for a valid index, and will reprompt the user until a valid number is entered.
     * Numbers outside 1-HandSize are invalid, as well as other inputs.
     * @param sc Scanner to read input from terminal
     * @param player The player who we are asking discard from
     * @return the index of the card to be discarded
     */
    public static int askForDiscard(Scanner sc, HumanPlayer player) {
        int index;
        Printer.printRenderedHand(player);
        while (true) {
            System.out.printf("Select a card to discard (1 to " + player.getPlayerHand().size() + "): ");
            if (sc.hasNextInt()) {
                index = sc.nextInt() - 1;
                sc.nextLine();
                if (index >=0 && index < player.getPlayerHand().size()) {
                    break; //exit loop on valid input
                }
            } else {
                sc.nextLine(); //discard invalid input
            }
            System.out.println("Invalid choice. Please enter a number between 1 and " + player.getPlayerHand().size() + ".");
        }
        return index;
    }

    /**
     * Prompt the user to enter how many human and bot players for the game.
     * Used in Server, minimum 2 human players and a max of 6 players total
     * Will keep prompting user until a valid combination of players has been entered
     * @param sc Scanner to read input from terminal
     * @return An int array of [noOfHumanPlayers, noOfBotPlayers]
     */
    public static int[] askForNumberOfPlayers(Scanner sc) {
        // Ask user for the amount of players to add
        boolean valid = false;
        System.out.print("How many human players (2-6): ");
        String input = sc.nextLine();

        while (!valid) {
            try {
                // Check if the user inputted a number
                int humans = Integer.parseInt(input);
                int bots = 0;

                // Check if the user inputted a number between 1 to 6
                if (humans < 2 | humans > 6) {
                    System.out.println("\nInvalid number of players.\n");
                    System.out.print("How many human players (2-6): ");
                    input = sc.nextLine();
                    continue;
                }

                // Now get the number of bots
                if (humans != 6) {
                    System.out.printf("How many bots (0-%d): ", (6 - humans));
                    input = sc.nextLine();
                    // Check if the input is an int
                    bots = Integer.parseInt(input);
                }

                // Check if the number isn't between 2 to 6
                if (humans + bots < 2 | humans + bots > 6 | bots < 0) {
                    System.out.println("\nInvalid number of players.\n");
                    System.out.print("How many human players (2-6): ");
                    input = sc.nextLine();
                    continue;

                } else {
                    return new int[]{humans, bots};
                }

            
            } catch (NumberFormatException e) {
                System.out.println("\nInvalid number of players.\n");
                System.out.print("How many human players (1-6): ");
                input = sc.nextLine();
            }

        }
        // This shouldn't run
        return null;
    }

    /**
     * Method to ask user to select an option to run in our App
     * Will keep reprompting the user until a valid input is entered
     * @param sc Scanner to read input from terminal
     * @return the corresponding int to the function of the app they selected
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

    /**
     * Create the players to add into the game. This is used for offline game
     * Will validate the number of players. Minimum 1 human player and a max of 6 players.
     * @param sc Scanner to read input from the terminal
     * @param deck Deck to deal cards to each player when creating them
     * @return the players to be added into the game
     */
    public static ArrayList<Player> getPlayerList(Scanner sc, Deck deck) {
    // initialise ArrayList<Player> to return players
    ArrayList<Player> players = new ArrayList<>();

    // Ask user for the amount of players to add
    boolean valid = false;
    System.out.print("How many human players (1-6): ");
    String input = sc.nextLine();

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
                    String message = String.format("Name of player %d: ", i);
                    String name = getUsername(message, sc, players);
                    players.add(new HumanPlayer(deck, name));
                }

                // Add the bots in and shuffle the players
                players.addAll(AppUtils.generateBotPlayers(bots, deck));
                Collections.shuffle(players);
            
            }

        
        } catch (NumberFormatException e) {
            System.out.println("\nInvalid number of players.\n");
            System.out.print("How many human players (1-6): ");
            input = sc.nextLine();
        }

        }
    return players;
    } 

    /**
     * Method used to get the username for the players
     * Will check if the name is between 1 to 80 characters, and is not a duplicate
     * between existing players and bot players.
     * @param message The message to display to ask user for username
     * @param sc Scanner to read input from terminal
     * @param players The existing players, if null is passed, it will not check this condition
     * @return validated username of the player
     */
    public static String getUsername(String message, Scanner sc, ArrayList<Player> players) {
        String username = null;
        List<String> paradeBotNames = List.of("Alice", "Mad Hatter", "White Rabbit", 
        "Humpty Dumpty", "Cheshire Cat", "Dodo Bird");

        outer:
        while (username == null) {
            System.out.print(message);
            username = sc.nextLine();

            if (players != null && players.size() != 0) {
                for (Player p : players) {
                    if (p.getName().equals(username)) {
                        System.out.println("Sorry this username is taken :(");
                        username = null;
                        continue outer;
                    }
                }
            }

            if (paradeBotNames.contains(username)) {
                System.out.println("Sorry the bots have taken this username");
                username = null;
                continue;
            }

            if (username.length() < 1 || username.length() > 80) {
                System.out.println("Username must be between 1 to 80 characters long.");
                username = null;
                continue;
            }
        }

        return username;
    }
}
