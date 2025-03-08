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

    //Simulates the removal of eligible cards without modifying the actual parade.
    //Returns a list of cards that would be removed if the given card were played.
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
    // The bot evaluates each card in its hand and selects the one that results in the fewest removals from the parade.
    public Card determineCardChoice(ArrayList<Card> botHand, Parade parade) {
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
    // public static void main(String[] args) {
    //     Deck deck = new Deck();
    //     deck.shuffle();
    //     Parade parade = new Parade(deck);
    //     BotPlayer testBotPlayer = new BotPlayer(deck, "TestBotPlayer");
    //     DisplayPlayerMenu.displayParadeAndMyHand(parade, testBotPlayer.getPlayerHand());
    //     testBotPlayer.takeTurn(deck, parade);
    //     DisplayPlayerMenu.displayParadeAndMyHand(parade, testBotPlayer.getPlayerHand());
    // }

}
