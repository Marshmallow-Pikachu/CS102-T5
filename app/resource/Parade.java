package app.resource;

import java.util.*;

public class Parade {
    private ArrayList<Card> paradeCards;

    public Parade(ArrayList<Card> paradeCards) {
        this.paradeCards = paradeCards;
    }

    public ArrayList<Card> removeEligibleCards(Card playedCard) {
        ArrayList<Card> removableCardsFromParadeList = new ArrayList<>(); //list of cards that are removed from parade
        for (Card currentParadeCard:paradeCards) {
            // checking eligibility of cards to be removed from Parade to Players' Hand
            if (currentParadeCard.getValue() <= playedCard.getValue() || currentParadeCard.getColour().equals(playedCard.getColour())) {
                removableCardsFromParadeList.add(currentParadeCard); 
                paradeCards.remove(currentParadeCard);  
            }
        }
        return removableCardsFromParadeList;
    }

    public void addToParade(Card playedCard) {
        paradeCards.add(playedCard); // add playedCard to paradeCards
    }

    public void displayParade() {
        for(Card card:paradeCards) {
            System.out.println(card.toString());
        }
    }
}