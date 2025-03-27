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
        printAllScoringZones(players);

        // Display the parade
        System.out.printf("%n" + "=".repeat(45) + " Parade " + "=".repeat(45) + "%n%n");
        Parade parade = game.getParade();
        int deckSize = game.getDeck().getCards().size();
        printRenderedParade(parade.getParadeCards(), deckSize);
        System.out.printf("%n%n");

    }

    
    // Player will input a number from 1 to 5, representing the 5 cards in his hand
    // Player input is validated here by my try catch, not sure if there is a need for validateInput.java

    public static void displayPromptForCard(ArrayList<Card> myHand) {
        
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
    public static void printRenderedHand(Player player) {
        System.out.printf("%n   %s's Hand%n", player.getName());
        CardList printList = new CardList();

        ArrayList<Card> cards = player.getPlayerHand();
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

    // For displaying all Collection Zones of every Player
    public static void printAllScoringZones(ArrayList<Player> players) {
        // Prints the starting" ------"
        System.out.println(" " + "-".repeat(97) + " ");
        for (Player p : players) {
            // Create the whitespace for the Scoring Zone Title
            String leftspace = " ".repeat((84-p.getName().length())/2);
            String rightspace = " ".repeat((84-p.getName().length())/2);
            if (p.getName().length() % 2 == 1) {
                leftspace += " ";
            }
            // Print "|     Player's Scoring Zone     |"
            System.out.printf("|%s%s Scoring Zone%s|%n", leftspace, p.getName(), rightspace);

            // Print the cards in the Scoring Zone
            System.out.println("|" + " ".repeat(97) + "|");
            printRenderedScoringZone(p.getCollectedParadeCards());
            System.out.println(" " + "-".repeat(97) + " ");
        }
    }

    // For displaying cards in Collection Zone
    public static void printRenderedScoringZone(ArrayList<Card> cards) {
                // Get the rendered versions of each color
                ArrayList<CardList> colorList = Render.renderScoringZone(cards);

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

