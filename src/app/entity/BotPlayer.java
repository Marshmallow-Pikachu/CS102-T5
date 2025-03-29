package app.entity;

import java.util.*;

import app.game.Game;
import app.resource.*;
import app.utilities.Printer;

/**
 * This class contains the implementation of a Bot Player.
 * BotPlayer class simulates a player and makes moves based on game state to simulate an AI opponent.
 */
public class BotPlayer extends Player {

    /**
     * Constructs a new BotPlayer with a specified deck and name.
     * @param deck The shuffled instance of deck that everyone will be using that game.
     * @param name The name of the bot player.
     */
    public BotPlayer(Deck deck, String name) {
        super(deck, name);
    }

    // public int countRemovals(Card playedCard, Parade parade) {
    //     int paradeSize = parade.size();

    // }

     /**
     * Simulates the removal of eligible cards without modifying the actual parade.
     * Returns a list of cards that would be removed if the given card were played.
     * @param playedCard The card played by the bot.
     * @param simulatedParade A simulation to see the effect of playing a card on the parade.
     * @return An ArrayList of type Card that would be removable based on the played card.
     */
    public ArrayList<Card> simulateRemoval (Card playedCard, ArrayList<Card> simulatedParade) {
        ArrayList<Card> removableCards = new ArrayList<>();
        for (Card currentParadeCard : simulatedParade) {
            if (currentParadeCard.getValue() <= playedCard.getValue() || 
                currentParadeCard.getColour().equals(playedCard.getColour())) {
                removableCards.add(currentParadeCard);
            }
        }
        return removableCards;
    }

    /**Bot Algorithm:
    * This simple algorithm is aimed at collecting the least amount cards from the parade as possible
    * The bot evaluates each card in its hand and selects the one that results in the fewest removals from the parade.
    * @param game The current state of the game, used to access the parade and other game elements.
    * @return Card The card selected by the bot to play.
    */
    public Card determineCardChoice(Game game) {
        ArrayList<Card> botHand = super.getPlayerHand();
        Parade parade = game.getParade();
        Card bestCard = null;
        int minCardsRemoved = Integer.MAX_VALUE;

        for (Card card : botHand) {
            ArrayList<Card> simulatedParade = new ArrayList<>(parade.getParadeCards());
            int cardsRemoved = simulateRemoval(card, simulatedParade).size();

            if (cardsRemoved < minCardsRemoved) {
                minCardsRemoved = cardsRemoved;
                bestCard = card;
            }
        }
        return bestCard;
    }

    /**
     * This method helps the bot to discard cards.
     * The bot will try to keep up to two cards where it leads in colour, otherwise it chooses the cards with the lowest value.
     * @param gamePlayers A list of all players in the game, used to compare who's collected what colours.
     */
    public void discardCards(ArrayList<Player> gamePlayers) {
        ArrayList<Integer> highestColourCounts = obtainHighestColourCounts(gamePlayers);
        ArrayList<Integer> botCollectedColourCounts = obtainPlayerColourCounts(getCollectedParadeCards()); // removed super.obtain
        ArrayList<String> colourList = new ArrayList<>(Arrays.asList("Red", "Blue", "Purple", "Green", "Black", "Yellow"));

        ArrayList<Card> botHand = getPlayerHand();
        ArrayList<Card> colourCardsToKeep = new ArrayList<>();
        ArrayList<Card> cardsToKeep = new ArrayList<>();

        //Find the cards that belong to colours the bot has a majority off in the game
        for (Card c : botHand) { 
            int index = colourList.indexOf(c.getColour());
            if (botCollectedColourCounts.get(index) >= highestColourCounts.get(index)) {
                colourCardsToKeep.add(c);
            }
        }
        // System.out.println("debug here:" + colourCardsToKeep);

        //sort coloured cards to keep by value
        colourCardsToKeep.sort(Comparator.comparingInt(Card::getValue));

        //keep up to 2 of those coloured cards
        for (Card c: colourCardsToKeep) {
            cardsToKeep.add(c);
            if (cardsToKeep.size() == 2) {
                break;
            }
        }

        //If needed, fill the remaining slots with lowest-value cards from hand (excluding those who are already kept)
        if (cardsToKeep.size() < 2) {
            ArrayList<Card> remainingCards = new ArrayList<>(botHand);
            remainingCards.removeAll(cardsToKeep);

            //sort remaining cards by value
            remainingCards.sort(Comparator.comparingInt(Card::getValue)); //calls card.getvalue() and then sorts remaining cards based on the value

            for (Card c : remainingCards) {
                cardsToKeep.add(c);
                if (cardsToKeep.size() == 2) break;
            }
        }

        botHand.clear();
        botHand.addAll(cardsToKeep);
    }
    /**
     * Calculates the highest counts of each colour collected by any player in the game.
     * @param gamePlayers List of all players in the game.
     * @return An ArrayList of type integer, with the highest counts for each colour among the players.
     */
    public ArrayList<Integer> obtainHighestColourCounts(ArrayList<Player> gamePlayers) {
        ArrayList<Integer> highestColourCounts = new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0, 0));
        for (Player p : gamePlayers){
            ArrayList<Integer> colourCount = obtainPlayerColourCounts(p.getCollectedParadeCards());
                for (int i = 0; i < 6; i++) {
                    if (colourCount.get(i) > highestColourCounts.get(i)) {
                        highestColourCounts.set(i, colourCount.get(i));
                    }
                }
            }
            return highestColourCounts;
        }
    // Chris - This method is implemented in abstract player already
    // public ArrayList<Integer> obtainPlayerColourCounts (ArrayList<Card> playerHand) {
    //     ArrayList<String> colourList = new ArrayList<>(Arrays.asList("Red", "Blue", "Purple", "Green", "Black", "Yellow"));
    //     ArrayList<Integer> colorCount = new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0, 0));
    //     for (Card c : playerHand) {
    //         for (int i = 0; i < 6; i++) {
    //             if (c.getColour().equals(colourList.get(i))) {
    //                 colorCount.set(i, colorCount.get(i) + 1);
    //             }
    //         }
    //     }
    //     return colorCount;
    // }
    
    /**
     * Performs the actions for a bot player's turn in the game.
     * This includes playing a card, collecting eligible cards, and drawing a new card if the game hasn't ended.
     * @param game The current game instance.
     * @param chosenCard The card chosen by the bot to play.
     */
    public void takeTurn(Game game, Card chosenCard) {
        Parade parade = game.getParade();
        Printer.displayGameState(game);
        System.out.println("Bot has chosen to play " + chosenCard.toString());
        collectEligibleCardsFromParade(parade, chosenCard);
        if (!game.getGameEnd()) {
            Deck deck = game.getDeck();
            super.getPlayerHand().add(deck.drawCard());
        }
    }
}
