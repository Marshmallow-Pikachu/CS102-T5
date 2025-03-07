package app.utilities;

import java.util.*;
import app.resource.*; 

public class DisplayPlayerMenu {
    public static void displayParadeAndMyHand(Parade parade, ArrayList<Card> myHand) {
        // print out the current Parade state
        parade.displayParade();

        // print out myHand
        System.out.println("Player Hand");
        System.out.println(myHand);
    }

    // Player will input a number from 1 to 5, representing the 5 cards in his hand
    // Player input is validated here by my try catch, not sure if there is a need for validateInput.java

    public static Card promptPlayerForCardToPlay(ArrayList<Card> myHand) { //should be left non-static since this 
        Scanner sc = new Scanner(System.in);
        int cardSelectedIndex = -1; 
        String input = null;
        while (true) {
            try {
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
        sc.close(); //not sure if i need to close here
        return myHand.get(cardSelectedIndex);
    }

    // compile command: javac -d out -cp "out" app/utilities/DisplayPlayerMenu.java
    // public static void main(String[] args) {
    //     // run java app.utilities.DisplayPlayerMenu to test this after compiling
    //     DisplayPlayerMenu menu = new DisplayPlayerMenu();
    //     menu.promptPlayerForCardToPlay();
    // }
}
