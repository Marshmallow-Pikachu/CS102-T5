package app.utilities;

import app.resource.*;
import java.util.*; 
import app.entity.*;

public class DisplayPlayerMenu {

    public static final String ANSI_RESET = "\u001B[0m"; 
    // Declaring the color (Red, Blue, Purple, Green, Black, Yellow) 
    public static final String ANSI_RED    = "\u001B[31m";
    public static final String ANSI_BLUE   = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_BRIGHT_GREEN  = "\u001B[92m";
    public static final String ANSI_BLACK  = "\u001B[30m";
    public static final String ANSI_YELLOW = "\u001B[33m";

    public static void displayGameState(Parade parade, ArrayList<Player> players, String name, ArrayList<Card> myHand) {
        // print out the current game state
        
        System.out.println("----------------------------------------------------------");
        for (Player p : players) {
            System.out.printf("%s Scoring Zone%n%n", p.getName());
            printCollectedCards(p.getCollectedParadeCards());
            System.out.println("----------------------------------------------------------\n");
        }

        System.out.println("============ Parade ============");
        printCards(parade.getParadeCards());
        
        // print out myHand
        System.out.printf("%s Hand%n", name);
        //System.out.println(myHand);
        printCards(myHand);
    }


    public static void printCollectedCards(ArrayList<Card> cards){
        if (cards.isEmpty()){
            System.out.println("No Cards Collected");
            return;
        }
        ArrayList<Card> redCards = new ArrayList<>();
        ArrayList<Card> blueCards = new ArrayList<>();
        ArrayList<Card> purpleCards = new ArrayList<>();
        ArrayList<Card> greenCards = new ArrayList<>();
        ArrayList<Card> blackCards = new ArrayList<>();
        ArrayList<Card> yellowCards = new ArrayList<>();
        for (Card c : cards){
            switch (c.getColour()){
                case "Red":
                redCards.add(c);
                break;
                case "Blue":
                blueCards.add(c);
                break;
                case "Purple":
                purpleCards.add(c);
                break;
                case "Green":
                greenCards.add(c);
                break;
                case "Black":
                blackCards.add(c);
                break;
                case "Yellow":
                yellowCards.add(c);
                break;
            }
        }
        printSameColor(redCards, ANSI_RED);
        printSameColor(blueCards, ANSI_BLUE);
        printSameColor(purpleCards, ANSI_PURPLE);
        printSameColor(greenCards, ANSI_BRIGHT_GREEN);
        printSameColor(blackCards, ANSI_BLACK);
        printSameColor(yellowCards, ANSI_YELLOW);
    }

    //Prints all card of same Color in 1 Row
    public static void printSameColor(ArrayList<Card> cards, String cardColor){
        if (cards.isEmpty()){
            return;
        }
        ArrayList<String> printString = new ArrayList<>();
        printString.add("");
        printString.add("");
        printString.add("");
        for (Card c : cards){
            ArrayList<String> string = printCardString(c);
            printString.set(0, printString.get(0) + string.get(0));
            printString.set(1, printString.get(1) + string.get(1));
            printString.set(2, printString.get(2) + string.get(2));
        }
        //Print out all cards that are XXX Color 
        System.out.println(cardColor + printString.get(0));
        System.out.println(printString.get(1));
        System.out.println(printString.get(2) + ANSI_RESET);
    }

    //Gets String to Convert Colour
    public static String linkCardtoColour(Card c){
        String cardColor = null;
        switch (c.getColour()){
            case "Red":
            return ANSI_RED;
            case "Blue":
            return ANSI_BLUE;
            case "Purple":
            return ANSI_PURPLE;
            case "Green":
            return ANSI_BRIGHT_GREEN;
            case "Black":
            return ANSI_BLACK;
            case "Yellow":
            return ANSI_YELLOW;
        }
        System.out.println("Error Color Not Matched!");
        return cardColor;
    }


    public static void printCards(ArrayList<Card> cards){
        ArrayList<String> cardList = new ArrayList<>();
        cardList.add("");         
        cardList.add(""); 
        cardList.add("");
        for (Card c : cards){
            String colorCode = linkCardtoColour(c);
            ArrayList<String> temp = printCardString(c);
            cardList.set(0, cardList.get(0) + colorCode + temp.get(0) + ANSI_RESET);
            cardList.set(1, cardList.get(1) + colorCode + temp.get(1) + ANSI_RESET);
            cardList.set(2, cardList.get(2) + colorCode + temp.get(2) + ANSI_RESET);
            //System.out.println(parade.get(2));
            //System.out.println();
        }
        System.out.println(cardList.get(0));
        System.out.println(cardList.get(1));
        System.out.println(cardList.get(2));
    } 

    //Gets String to append to print everything in same line
    public static ArrayList<String> printCardString(Card c){
        ArrayList<String> returnval = new ArrayList<String>();
        returnval.add(0, "╭───╮");
        String toInsert;
        if (c.getValue() != 10){
            toInsert = "│ "+c.getValue()+" │";
        } else{
            toInsert = "│ T │";
        }
        returnval.add(1, toInsert);
        returnval.add(2, "╰───╯");
        return returnval;
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
        return myHand.get(cardSelectedIndex);
    }

    // compile command: javac -d out -cp "src" src/app/utilities/DisplayPlayerMenu.java
    // public static void main(String[] args) {
    //     // run java app.utilities.DisplayPlayerMenu to test this after compiling
    //     DisplayPlayerMenu menu = new DisplayPlayerMenu();
    //     menu.promptPlayerForCardToPlay();
    // }
}
