package app.resource;

import java.util.*;

public class Parade {
    private ArrayList<Card> paradeCards = new ArrayList<Card>();

    /**
     * Initializes a new parade with the first six cards drawn from the given deck.
     * @param deck The deck from which cards are drawn to form the initial parade.
     */
    public Parade(Deck deck) { //This constructor should only ever be called once in a usual game of Parade
        // draw 6 cards from deck and add it to parade
        for (int i = 0; i < 6; i++) {
            Card drawnCard = deck.drawCard();
            paradeCards.add(drawnCard);
        }
    }
    /**
     * Gets the current list of cards in the parade.
     * @return The list of parade cards.
     */
    public ArrayList<Card> getParadeCards() {
        return this.paradeCards;
    }
    /**
     * Removes cards from the parade that are eligible based on the played card's color and face value.
     * @param playedCard The card played by the current player.
     * @return A list of cards that have been removed from the common parade, to be added to the player's collected parade cards pool
     */
    public ArrayList<Card> removeEligibleCards(Card playedCard) {
        ArrayList<Card> removableCardsFromParadeList = new ArrayList<>(); 
        if (playedCard.getValue() >= paradeCards.size()) {
            return removableCardsFromParadeList;
        }

        for(int i = 0; i <= paradeCards.size() - playedCard.getValue() - 1; i++) {
            Card currentCard = paradeCards.get(i);
            currentCard.setRemovalMode();
        }

        for (Card currentParadeCard:paradeCards) {
            if (currentParadeCard.getRemovalMode() == true && 
               (currentParadeCard.getValue() <= playedCard.getValue() || 
                currentParadeCard.getColour().equals(playedCard.getColour()))) {

                removableCardsFromParadeList.add(currentParadeCard); 
            }
        }
        for(Card removingCard : removableCardsFromParadeList) {
            paradeCards.remove(removingCard);
        }
        for (Card c : paradeCards){
            c.resetRemovalMode();
        }
        return removableCardsFromParadeList;
    }
    /**
     * Adds the played card to the end of the parade.
     * @param playedCard The card that was played and added to the parade.
     */
    public void addToParade(Card playedCard) {
        paradeCards.add(playedCard); // add playedCard to paradeCards
    }

}