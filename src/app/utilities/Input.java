package app.utilities;

import java.util.ArrayList;
import java.util.Scanner;

import app.resource.Card;

public class Input {
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
