package app.utilities;

import app.entity.*;
import app.game.Game;
import app.resource.*;
import java.io.*;
import java.util.*;

public class Printer {
    // To display the current game state
    public static void displayGameState(Game game) {
        // Display each players scoring zone
        ArrayList<Player> players = game.getPlayers();
        System.out.println(" " + "-".repeat(97) + " ");
        for (Player p : players) {
            String leftspace = " ".repeat((84-p.getName().length())/2);
            String rightspace = " ".repeat((84-p.getName().length())/2);
            if (p.getName().length() % 2 == 1) {
                leftspace += " ";
            }
            System.out.printf("|%s%s Scoring Zone%s|%n", leftspace, p.getName(), rightspace);
            System.out.println("|" + " ".repeat(97) + "|");
            Render.renderCollectionZone(p.getCollectedParadeCards());
            System.out.println(" " + "-".repeat(97) + " ");
        }

        // Display the parade
        System.out.println("=".repeat(45) + " Parade " + "=".repeat(45));
        Parade parade = game.getParade();
        int deckSize = game.getDeck().getCards().size();
        printRenderedParade(parade.getParadeCards(), deckSize);
    }

    
    // Player will input a number from 1 to 5, representing the 5 cards in his hand
    // Player input is validated here by my try catch, not sure if there is a need for validateInput.java

    public static Card promptPlayerForCardToPlay(ArrayList<Card> myHand) { //should be left non-static since this 
        Scanner sc = new Scanner(System.in);
        int cardSelectedIndex = -1; 
        String input = null;
        while (true) {
            try {
                System.out.println("    (1)         (2)         (3)         (4)         (5)    "); // To number the cards the player can play
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


    // Print static logo for our game
    public static void printLogo() {
        try (Scanner sc = new Scanner(new File("./image/logo.txt"))) {
            System.out.print(ANSIColor.ANSI_PURPLE);
            while (sc.hasNextLine()) {
                System.out.println(sc.nextLine());
            }
            System.out.println(ANSIColor.ANSI_RESET);
        } catch (FileNotFoundException e) {
            System.out.println("Seems like the image folder is missing...");
        }
    }


    public static void printRenderedCards(ArrayList<Card> cards) {
        if (cards.isEmpty()) {
            return;
        }
        // We print 8 cards per line for better display
        int count = 0;
        PrintList printList = new PrintList();

        for (Card c : cards) {
            // Check if we had hit 8 cards -> if so, flush out the printlist
            if (count == 8) {
                printList.output();
            }

            // Get the lines of the card
            ArrayList<String> renderedCard = Render.renderCard(c);
                
            // Add to the printList
            printList.add(renderedCard);

            // increment count by 1
            count += 1;
        }
        // flush out the last lines
        printList.output();
    }

    // Overloaded function to print the parade
    public static void printRenderedParade(ArrayList<Card> cards, int deckSize) {
        if (cards.isEmpty()) {
            return;
        }

        int count = 1;
        PrintList printList = new PrintList(Render.renderDeck(deckSize));

        for (int i = 0; i < cards.size()-1; i++) {
            // Check if we had hit 10 cards -> if so, flush out the printlist
            if (count == 7) {
                printList.output();
            }

            // Get the lines of the card
            printList.add(Render.renderCard(cards.get(i)));

            // increment count by 1
            count += 1;
        }
        // Print out the last few cards
        if (count == 7) {
            printList.output();
        }
        printList.add(Render.renderPlayedCard(cards.get(cards.size()-1)));
        printList.output();
        
        
    }




    }

