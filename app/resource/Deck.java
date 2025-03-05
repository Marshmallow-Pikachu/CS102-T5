package app.resource;

import java.util.*;

public class Deck {
    private ArrayList<Card> cards = new ArrayList<>();
    private boolean isEmpty;

    /*
     * 66 unique cards
     * 6 different colors [Black|Blue|Red|Green|Yellow|Purple]
     * each color [0-10]
     * total 11 cards per colour
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

    public boolean getIsEmpty() {
        return isEmpty;
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public Card drawCard() {
        // It should return a Card and remove that from its own array list

        // Card at index(0) is considered top of deck
        Card toReturn = cards.get(0);
        cards.remove(0);

        return toReturn;
    }

    /*
     * uncomment the code below to test Deck class
     * in terminal:
     * javac app/resource/Card.java
     * javac app/resource/Deck.java
     * java app.resource.Deck
     */
    // public ArrayList<Card> getCards() {
    //     return cards;
    // }

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
