package app.resource;

import java.util.*;

/**
 * Represents a deck of cards used in the game. The deck is initialized with a fixed set of cards. 
 * Each deck contains 66 total cards, 11 of each color, with values 0-10.
 */
public class Deck {
    private ArrayList<Card> cards = new ArrayList<>();
    private boolean isEmpty;

    /*
     * 66 unique cards
     * 6 different colors [Black|Blue|Red|Green|Yellow|Purple]
     * each color [0-10]
     * total 11 cards per colour
     */

    /**
     * Constructor for a new deck of 66 unique cards, with 11 cards for each of the 6 colors from 0-10.
     */
    public Deck() {
        String[] colours = { "Black", "Blue", "Red", "Green", "Yellow", "Purple" };

        // initialise ArrayList<Card>
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j <= 10; j++) {
                cards.add(new Card(j, colours[i]));
            }
        }
    }
    /**
     * Checks if the deck is empty.
     * @return true if there are no cards left in the deck, false otherwise.
     */
    public boolean getIsEmpty() {
        return isEmpty;
    }
    /**
     * Shuffles the cards in the deck.
     */
    public void shuffle() {
        Collections.shuffle(cards);
    }
    /**
     * Draws the "top" card from the deck and removes it from the deck.
     * For simplicity, consider index 0 the top card. 
     * @return The "top" card from the deck.
     */
    public Card drawCard() {
        // It should return a Card and remove that from its own array list
        // Card at index(0) is considered top of deck
        Card toReturn = cards.get(0);
        cards.remove(0);
        if (cards.isEmpty()) isEmpty = true;
        return toReturn;
    }
    /**
     * Getter for the current List of cards in the deck.
     * @return A list of cards currently in the deck.
     */
    public ArrayList<Card> getCards() {
        return cards;
    }
    
    /*
     * uncomment the code below to test Deck class
     * in terminal:
     * javac -d out app/resource/Card.java
     * javac -d out app/resource/Deck.java
     * java -cp "out" app.resource.Deck
     */

    // public static void main(String[] args) {
    //     Deck testDeck = new Deck();
    //     ArrayList<Card> testCards = testDeck.getCards();

    //     System.out.println("First ten cards before shuffling");
    //     for (int i = 0; i < 10; i++) {
    //         System.out.println(testCards.get(i).toString());
    //     }
    //     System.out.println();

    //     System.out.println("First ten cards after shuffling");
    //     testDeck.shuffle();
    //     for (int i = 0; i < 10; i++) {
    //         System.out.println(testCards.get(i).toString());
    //     }
    //     System.out.println();

    //     System.out.println("First ten cards after drawing a card");
    //     Card drawnCard = testDeck.drawCard();
    //     System.out.println("Drawn card: " + drawnCard.toString());
    //     for (int i = 0; i < 10; i++) {
    //         System.out.println(testCards.get(i).toString());
    //     }
    // }
}
