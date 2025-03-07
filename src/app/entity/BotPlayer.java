package app.entity;

import java.util.*;
import app.resource.*;
import app.utilities.DisplayPlayerMenu;

public class BotPlayer extends Player {
        public BotPlayer(Deck deck, String name) {
        super(deck, name);
    }

    // public int countRemovals(Card playedCard, Parade parade) {
    //     int paradeSize = parade.size();

    // }

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

    //Bot Algorithm
    //This simple algorithm is aimed at collecting the least amount cards from the parade as possible
    // 1. Play the highest-value card first (to protect more cards in the parade).
    // 2. If multiple cards have the same highest value, play the card that results in the fewest removals.
    public Card determineCardChoice(ArrayList<Card> botHand, Parade parade) {
        // Sort the bot’s hand by highest value first
        botHand.sort((a, b) -> Integer.compare(b.getValue(), a.getValue()));

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

    public void takeTurn(Deck deck, Parade parade) {
        Card chosenCard = determineCardChoice(super.getPlayerHand(), parade);
        System.out.println("Bot has chosen to play " + chosenCard.toString());
        collectEligibleCardsFromParade(parade, chosenCard);
        super.getPlayerHand().add(deck.drawCard());
    }

    //Testing and Debugging
    //compile command: javac -d out -cp "src" src/app/entity/BotPlayer.java
    //execute comand: java -cp "out" app.entity.BotPlayer
    public static void main(String[] args) {
        Deck deck = new Deck();
        deck.shuffle();
        Parade parade = new Parade(deck);
        BotPlayer testBotPlayer = new BotPlayer(deck, "TestBotPlayer");
        DisplayPlayerMenu.displayParadeAndMyHand(parade, testBotPlayer.getPlayerHand());
        testBotPlayer.takeTurn(deck, parade);
        DisplayPlayerMenu.displayParadeAndMyHand(parade, testBotPlayer.getPlayerHand());
    }

}
