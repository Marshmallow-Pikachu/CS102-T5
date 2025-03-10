package app.utilities;

import java.util.ArrayList;

import app.entity.*;
import app.resource.*;

public class Printer {
    // Declaring ANSI_RESET so that we can reset the color 
    public static final String ANSI_RESET = "\u001B[0m"; 
  
    // Declaring the color (Red, Blue, Purple, Green, Grey, Orange) 
    public static final String ANSI_BLACK  = "\u001B[30m";
    public static final String ANSI_RED    = "\u001B[31m";
    public static final String ANSI_BLUE   = "\u001B[34m";
    public static final String ANSI_YELLOW = "\u001B[33m"; 
    public static final String ANSI_BRIGHT_GREEN  = "\u001B[92m";
    public static final String ANSI_PURPLE = "\u001B[35m";

    // Feel free to add to psvm to check if your parts integrate
    private ArrayList<Player> players;
    private Parade parade;
    public static Deck deck;
    private boolean gameEnd;
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

    public void printScoringZone(ArrayList<Card> cards){
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
    public void printSameColor(ArrayList<Card> cards, String cardColor){
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

    //Gets String to append to print everything in same line
    public static ArrayList<String> printCardString(Card c){
        ArrayList<String> returnval = new ArrayList<String>();
        returnval.add(0, "╭───╮");
        String toInsert;
        if (c.getValue() != 10){
            toInsert = "│ "+c.getValue()+" │";
        } else{
            toInsert = "│ "+c.getValue()+"│";
        }
        returnval.add(1, toInsert);
        returnval.add(2, "╰───╯");
        return returnval;
    }
    //Prints 1 Card (For Testing)
    public void printCard(Card c){
        System.out.println(linkCardtoColour(c) + "╭───╮");
        System.out.printf("│ %d │ %n", c.getValue());
        System.out.println("╰───╯" + ANSI_RESET);
    }

    public static void printHand(ArrayList<Card> cards){ //to replace with Parade parade
        ArrayList<String> parade = new ArrayList<>();
        parade.add("");         
        parade.add(""); 
        parade.add("");
        for (Card c : cards){
            String colorCode = linkCardtoColour(c);
            ArrayList<String> temp = printCardString(c);
            parade.set(0, parade.get(0) + colorCode + temp.get(0) + ANSI_RESET);
            parade.set(1, parade.get(1) + colorCode + temp.get(1) + ANSI_RESET);
            parade.set(2, parade.get(2) + colorCode + temp.get(2) + ANSI_RESET);
            //System.out.println(parade.get(2));
            //System.out.println();
        }
        System.out.println(parade.get(0));
        System.out.println(parade.get(1));
        System.out.println(parade.get(2));
    } 


    public void printParade(Parade p){
        ArrayList<String> parade = new ArrayList<>();
        parade.add("");         
        parade.add(""); 
        parade.add("");
        for (Card c : p.getParadeCards()){
            String colorCode = linkCardtoColour(c);
            ArrayList<String> temp = printCardString(c);
            parade.set(0, parade.get(0) + colorCode + temp.get(0) + ANSI_RESET);
            parade.set(1, parade.get(1) + colorCode + temp.get(1) + ANSI_RESET);
            parade.set(2, parade.get(2) + colorCode + temp.get(2) + ANSI_RESET);
            //System.out.println(parade.get(2));
            //System.out.println();
        }
        System.out.println(parade.get(0));
        System.out.println(parade.get(1));
        System.out.println(parade.get(2));
    } 
}
