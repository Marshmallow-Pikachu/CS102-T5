package app.utilities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

import app.entity.HumanPlayer;
import app.resource.Card;

/**
 * This class contains utility methods for handling user inputs.
 */
public class Input {
    /**
     * Prompts the user to select a card from their hand by entering a number representing the card's position in playerHand.
     * 
     * @param sc The scanner used for input reading.
     * @param myHand An ArrayList of type Card representing the player's current hand.
     * @return The Card selected by the user.
     * 
     * This method checks for a valid index, and will reprompt the user until a valid number is entered.
     * Numbers outside 1-5 are invalid, as well as other inputs.
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

    // Returns an int array of how many human and bot players
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
}
