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
            printRenderedCollectionZone(p.getCollectedParadeCards());
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


    // For printing the logo for our game on startup
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

    // For printing the App menu for our game
    public static void printMenu() {
        System.out.printf("Select on Option:%n");
        System.out.printf("1. Play Offline%n");
        System.out.printf("2. Create an Online Game%n");
        System.out.printf("3. Join an Online Game%n");
        System.out.printf("4. Quit%n");
    }

    // For displaying cards in hand
    public static void printRenderedHand(ArrayList<Card> cards) {
        CardList printList = new CardList();

        for (Card c : cards) {
            // Get the lines of the card
            ArrayList<String> renderedCard = Render.renderCard(c);
                
            // Add to the printList
            printList.add(renderedCard);

        }
        // output the hand
        printList.output();

        // Print out the card options
        String cardSpace = " ".repeat(11);
        String options = "";
        switch (cards.size()) {
            case 5:
                options = "(5)       ";
            case 4:
                options = "(4)" + cardSpace + options;
            default:
                options = "       (1)" + cardSpace + "(2)" + cardSpace + "(3)" + cardSpace + options;
        }
        System.out.println(options);
    }

    // For displaying cards in Parade
    public static void printRenderedParade(ArrayList<Card> cards, int deckSize) {
        if (cards.isEmpty()) {
            return;
        }

        int count = 1;
        CardList printList = new CardList(Render.renderDeck(deckSize));

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

    // For displaying cards in Collection Zone
    public static void printRenderedCollectionZone(ArrayList<Card> cards) {
                // Get the rendered versions of each color
                ArrayList<CardList> colorList = Render.renderCollectionZone(cards);

                // To keep track of whether there is already 1 row in the printList
                // It is false when there is either 0 or 2 rows in the printList
                CardList printList = new CardList();
                boolean isSingleRow = false;
        
                for (CardList nextList : colorList) {
                    if (!nextList.isNull()) {
                        printList.add(nextList);
                        if (isSingleRow) {
                            isSingleRow = false;
                            printList.outputSpaced(isSingleRow);
                        } else {
                            isSingleRow = true;
                        }
                    }
                }
        
                // To clear the last colors if any
                if (isSingleRow) {
                    printList.outputSpaced(isSingleRow);
                }
    }

}

