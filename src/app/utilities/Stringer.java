package app.utilities;

import java.util.ArrayList;
import java.util.Map;

import app.entity.Player;
import app.game.Game;
import app.resource.*;

public class Stringer {
    
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
     * Prints the player's hand in using text render images.
     * @param player The player's hand.
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
     * Displays the cards in the parade.
     * @param cards The list of cards currently in the parade.
     * @param deckSize The number of cards remaining in the deck.
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
     * Displays the scoring zones for all players.
     * @param players The list of all players in the game.
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

    // For displaying cards in Collection Zone
    /**
     * Displays the collection zone for scoring, segmented by color.
     * @param cards The list of cards in the scoring zone.
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

    public static String stringDiscard(Card card) {
        CardList cardList = new CardList();
        cardList.add(Render.renderCard(card));

        String output = "You have discarded:\n" + cardList.output();
        return output;
    }

    public static String stringScoreList(Map<String, Integer> scoreList) {
        String output = "Final Scores:";
        for (String name : scoreList.keySet()) {
            output += name + ": " + scoreList.get(name) + "\n";
        }
        return output;
    }
    
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

}
