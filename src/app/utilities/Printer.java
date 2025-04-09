package app.utilities;

import app.entity.*;
import app.game.Game;
import app.resource.*;
import java.io.*;
import java.util.*;

/**
 * This class contains static methods that display the game state,
 * player hands, the parade, the cards, scoring zones, and the game splashscreen.
 */
public class Printer {

    /**
     * Default constructor of Printer
     */
    public Printer(){}
    /**
     * Prints the game logo from a text file.
     */
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

    /**
     * Prints the main menu options for the game.
     */
    public static void printMenu() {
        System.out.printf("Select on Option:%n");
        System.out.printf("1. Play Offline%n");
        System.out.printf("2. Create an Online Game%n");
        System.out.printf("3. Join an Online Game%n");
        System.out.printf("4. Quit%n");
    }

    /**
     * Displays the current game state, including each player's scoring zone and the common parade.
     * @param game The current game instance.
     */
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

    /**
     * Prints the player's hand in using text render images.
     * @param player The player's hand.
     */
    public static void printRenderedHand(Player player) {
        System.out.printf("%n   %s's Hand%n", player.getName());
        CardList cardList = new CardList();

        ArrayList<Card> cards = player.getPlayerHand();
        for (Card c : cards) {
            // Get the lines of the card
            ArrayList<String> renderedCard = Render.renderCard(c);
                
            // Add to the printList
            cardList.add(renderedCard);

        }
        // output the hand
        String output = cardList.output();
        System.out.print(output);

        // Print out the card options
        String cardSpace = " ".repeat(11);
        String options = "";
        switch (cards.size()) {
            case 5:
                options = "(5)       ";
            case 4:
                options = "(4)" + cardSpace + options;
            case 3:
                options = "(3)" + cardSpace + options;
            default:
                options = "       (1)" + cardSpace + "(2)" + cardSpace + options;
        }
        System.out.println(options);
    }

    /**
     * Displays the cards in the parade.
     * @param cards The list of cards currently in the parade.
     * @param deckSize The number of cards remaining in the deck.
     */
    public static void printRenderedParade(ArrayList<Card> cards, int deckSize) {
        if (cards.isEmpty()) {
            return;
        }

        int count = 1;
        CardList cardList = new CardList(Render.renderDeck(deckSize));

        for (int i = 0; i < cards.size()-1; i++) {
            // Check if we had hit 10 cards -> if so, flush out the printlist
            if (count == 7) {
                String output = cardList.output();
                System.out.print(output);
            }

            // Get the lines of the card
            cardList.add(Render.renderCard(cards.get(i)));

            // increment count by 1
            count += 1;
        }
        // Print out the last few cards
        if (count == 7) {
            String output = cardList.output();
            System.out.print(output);
        }
        cardList.add(Render.renderPlayedCard(cards.get(cards.size()-1)));
        String output = cardList.output();
        System.out.print(output);
        
        
    }

    /**
     * Displays the scoring zones for all players.
     * @param players The list of all players in the game.
     */
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

    /**
     * Displays the scoring zone for scoring, segmented by color.
     * @param cards The list of cards in the scoring zone.
     */
    public static void printRenderedScoringZone(ArrayList<Card> cards) {
                // Get the rendered versions of each color
                ArrayList<CardList> colorList = Render.renderScoringZone(cards);

                // To keep track of whether there is already 1 row in the printList
                // It is false when there is either 0 or 2 rows in the printList
                CardList cardList = new CardList();
                boolean isSingleRow = false;
        
                for (CardList nextList : colorList) {
                    if (!nextList.isNull()) {
                        cardList.add(nextList);
                        if (isSingleRow) {
                            isSingleRow = false;
                            String output = cardList.outputSpaced(isSingleRow);
                            System.out.print(output);
                        } else {
                            isSingleRow = true;
                        }
                    }
                }
        
                // To clear the last colors if any
                if (isSingleRow) {
                    String output = cardList.outputSpaced(isSingleRow);
                    System.out.print(output);
                }
    }

    /**
     * Displays the discarded card
     * @param card The card discarded
     */
    public static void printDiscard(Card card) {
        CardList cardList = new CardList();
        cardList.add(Render.renderCard(card));

        String output = "You have discarded:\n" + cardList.output();
        System.out.println(output);
    }
    
    /**
     * Displays the list of scores of all players
     * @param scoreList A map of name : score of all players
     */
    public static void printScoreList(Map<String, Integer> scoreList) {
        System.out.println("Final Scores:");
        for (String name : scoreList.keySet()) {
            System.out.println(name + ": " + scoreList.get(name));
        }
    }

    /**
     * Displays who has won the game
     * @param game The game instance
     */
    public static void printWinScreen(Game game){
        ArrayList<Player> winnerList = game.determineWinner();
        if (winnerList.size() > 1){ //If Draw
            System.out.print("Draw between ");
            for (int i = 0; i < winnerList.size(); i++){
                System.out.printf(winnerList.get(i).getName());
                if (i != winnerList.size()-1){
                    System.out.printf(" and ");
                }
            }
            System.out.println(".");
        }else{
            System.out.println("Winner is : " + winnerList.get(0).getName());
        }
    }

    /**
     * Returns the current game state, including each player's scoring zone and the common parade
     * as a string.
     * @param game The current game instance.
     * @return the game state as a string
     */
    public static String stringGameState(Game game) {
        String output = "";
        // Display each players scoring zone
        ArrayList<Player> players = game.getPlayers();
        output += stringAllScoringZones(players);

        // Display the parade
        output += "\n" + "=".repeat(45) + " Parade " + "=".repeat(45) + "\n\n";
        Parade parade = game.getParade();
        int deckSize = game.getDeck().getCards().size();
        output += stringRenderedParade(parade.getParadeCards(), deckSize) + "\n\n";

        return output;
    }

    /**
     * Returns the player's hand in using text render images as a string.
     * @param player The player's hand.
     * @return the rendered hand as a string
     */
    public static String stringRenderedHand(Player player) {
        String output = "";
        output += String.format("%n   %s's Hand%n", player.getName());
        CardList cardList = new CardList();

        ArrayList<Card> cards = player.getPlayerHand();
        for (Card c : cards) {
            // Get the lines of the card
            ArrayList<String> renderedCard = Render.renderCard(c);
                
            // Add to the printList
            cardList.add(renderedCard);

        }
        // output the hand
        output += cardList.output();

        // Print out the card options
        String cardSpace = " ".repeat(11);
        String options = "";
        switch (cards.size()) {
            case 5:
                options = "(5)       ";
            case 4:
                options = "(4)" + cardSpace + options;
            case 3:
                options = "(3)" + cardSpace + options;
            default:
                options = "       (1)" + cardSpace + "(2)" + cardSpace + options;
        }
        output += options;

        return output;
    }

    /**
     * Returns the cards in the parade as a string.
     * @param cards The list of cards currently in the parade.
     * @param deckSize The number of cards remaining in the deck.
     * @return the parade as a string
     */
    public static String stringRenderedParade(ArrayList<Card> cards, int deckSize) {
        String output = "";
        if (cards.isEmpty()) {
            return output;
        }

        int count = 1;
        CardList cardList = new CardList(Render.renderDeck(deckSize));

        for (int i = 0; i < cards.size()-1; i++) {
            // Check if we had hit 10 cards -> if so, flush out the printlist
            if (count == 7) {
                output += cardList.output();
            }

            // Get the lines of the card
            cardList.add(Render.renderCard(cards.get(i)));

            // increment count by 1
            count += 1;
        }
        // Print out the last few cards
        if (count == 7) {
            output += cardList.output();
        }
        cardList.add(Render.renderPlayedCard(cards.get(cards.size()-1)));
        output += cardList.output();
        
        return output;
        
    }

    /**
     * Returns the scoring zones for all players as a string.
     * @param players The list of all players in the game.
     * @return the scoring zones as a string
     */
    public static String stringAllScoringZones(ArrayList<Player> players) {
        String output = "";
        // Prints the starting" ------"
        output += " " + "-".repeat(97) + " \n";
        for (Player p : players) {
            // Create the whitespace for the Scoring Zone Title
            String leftspace = " ".repeat((84-p.getName().length())/2);
            String rightspace = " ".repeat((84-p.getName().length())/2);
            if (p.getName().length() % 2 == 1) {
                leftspace += " ";
            }
            // Print "|     Player's Scoring Zone     |"
            output += String.format("|%s%s Scoring Zone%s|%n", leftspace, p.getName(), rightspace);

            // Print the cards in the Scoring Zone
            output += "|" + " ".repeat(97) + "|\n";
            output += stringRenderedScoringZone(p.getCollectedParadeCards());
            output += " " + "-".repeat(97) + " \n";
        }

        return output;
    }

    /**
     * Returns the scoring zone for scoring, segmented by color, as a string.
     * @param cards The list of cards in the scoring zone.
     * @return the scoring zone as a string
     */
    public static String stringRenderedScoringZone(ArrayList<Card> cards) {
        String output = "";

        // Get the rendered versions of each color
        ArrayList<CardList> colorList = Render.renderScoringZone(cards);

        // To keep track of whether there is already 1 row in the printList
        // It is false when there is either 0 or 2 rows in the printList
        CardList cardList = new CardList();
        boolean isSingleRow = false;

        for (CardList nextList : colorList) {
            if (!nextList.isNull()) {
                cardList.add(nextList);
                if (isSingleRow) {
                    isSingleRow = false;
                    output += cardList.outputSpaced(isSingleRow);
                } else {
                    isSingleRow = true;
                }
            }
        }

        // To clear the last colors if any
        if (isSingleRow) {
            output += cardList.outputSpaced(isSingleRow);
        }
        return output;
    }

    /**
     * Returns a string of why the game has entered its last round
     * @param game the instance of game
     * @return The message of why the game has entered its last round
     */
    public static String stringGameEndMessage(Game game) {
        // Get the last round reason
        Player gameEnder = game.getPlayers().getLast();
        String message = "";
        if (gameEnder.hasSixColors()) {
            message = String.format("%s has collected 6 colors!\n", gameEnder.getName());
        } else {
            message = "There are no more cards in the deck!\n";
        }
        message += "Final Round!\n";

        return message;
    }
    
    /**
     * return the discarded card as a string
     * @param card The card discarded
     * @return A string of the discarded card
     */
    public static String stringDiscard(Card card) {
        CardList cardList = new CardList();
        cardList.add(Render.renderCard(card));

        String output = "You have discarded:\n" + cardList.output();
        return output;
    }

    /**
     * Returns the list of scores of all players as a string
     * @param scoreList A map of name : score of all players
     * @return a string of all the scores of all the players
     */
    public static String stringScoreList(Map<String, Integer> scoreList) {
        String output = "Final Scores:";
        for (String name : scoreList.keySet()) {
            output += name + ": " + scoreList.get(name) + "\n";
        }
        return output;
    }
    
    /**
     * Return who has won the game as a string
     * @param game The game instance
     * @return a string of who has won the game
     */
    public static String stringWinScreen(Game game){
        String output = "";
        ArrayList<Player> winnerList = game.determineWinner();
        if (winnerList.size() > 1){ //If Draw
            output += "Draw between ";
            for (int i = 0; i < winnerList.size(); i++){
                output += winnerList.get(i).getName();
                if (i != winnerList.size()-1){
                    output += " and ";
                }
            }
            output += ".\n";
        }else{
            output += "Winner is : " + winnerList.get(0).getName();
        }
        return output;
    }

    public static void clearScreen() {
        System.out.println("\033[1J\033[3J");
    }

    public static String clearScreenString() {
        return "\033[1J\033[3J";
    }
}

