package app.entity;

import java.util.*;

import app.resource.*;

public class BotPlayer extends Player {
        public BotPlayer(Deck deck, String name) {
        super(deck, name);
    }

    // public int countRemovals(Card playedCard, Parade parade) {
    //     int paradeSize = parade.size();

    // }


    //Bot Algorithm
    //This simple algorithm is aimed at collecting the least amount cards from the parade as possible
    // 1. Play the highest-value card first (to protect more cards in the parade).
    // 2. If multiple cards have the same highest value, play the card that results in the fewest removals.

    public Card determineCardChoice(ArrayList<Card> botHand, Parade parade) {
        Card bestCard = null;

        return bestCard;
    }

    public void takeTurn(Deck deck, Parade parade) {
        // Card chosenCard = determineCardChoice(super.getPlayerHand(), parade);
        // collectEligibleCardsFromParade(parade, chosenCard);
        // super.getPlayerHand().add(deck.drawCard());
    }


}
