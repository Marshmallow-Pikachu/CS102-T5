package app.utilities;

import java.util.ArrayList;
import java.util.Scanner;

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
}
