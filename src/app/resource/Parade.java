package app.resource;

import java.util.*;

public class Parade {
    private ArrayList<Card> paradeCards = new ArrayList<Card>();

    public Parade(Deck deck) { //This constructor should only ever be called once in a usual game of Parade
        // draw 6 cards from deck and add it to parade
        for (int i = 0; i < 6; i++) {
            Card drawnCard = deck.drawCard();
            paradeCards.add(drawnCard);
        }
    }

    public ArrayList<Card> getParadeCards() {
        return this.paradeCards;
    }

    public ArrayList<Card> removeEligibleCards(Card playedCard) {
        ArrayList<Card> removableCardsFromParadeList = new ArrayList<>(); 
        if (playedCard.getValue() < paradeCards.size()) {
            for(int i = 0; i <= paradeCards.size() - playedCard.getValue() - 1; i++) {
                Card currentCard = paradeCards.get(i);
                currentCard.setRemovalMode();
            }
        }


        for (Card currentParadeCard:paradeCards) {
            if (currentParadeCard.getRemovalMode() == true && (currentParadeCard.getValue() <= playedCard.getValue() || currentParadeCard.getColour().equals(playedCard.getColour()))) {
                removableCardsFromParadeList.add(currentParadeCard); 
            }
        }
        for(Card removingCard:removableCardsFromParadeList) {
            paradeCards.remove(removingCard);
        }
        return removableCardsFromParadeList;
    }

    //public ArrayList<Card> removeEligibleCards(Card playedCard) {
    //     ArrayList<Card> removableCardsFromParadeList = new ArrayList<>(); //list of cards that are removed from parade

    //     for (Card currentParadeCard : paradeCards) {
    //         // checking eligibility of cards to be removed from Parade to Players' Hand
    //         if (currentParadeCard.getValue() <= playedCard.getValue() || currentParadeCard.getColour().equals(playedCard.getColour())) {
    //             removableCardsFromParadeList.add(currentParadeCard);  
    //         }
    //     }
    //     paradeCards.removeAll(removableCardsFromParadeList);
    //     return removableCardsFromParadeList;
    // }

    public void addToParade(Card playedCard) {
        paradeCards.add(playedCard); // add playedCard to paradeCards
    }

    public void displayParade() {
        System.out.println("Current Parade");
        for(Card card:paradeCards) {
            System.out.println(card.toString());
        }
    }

    //testing and debug
    //commands are run from parent folder of "app", remember to go compile Deck and Card First
    //compile command: javac -d out -cp "src" src/app/resource/parade.java
    //execute command: java -cp "out" app.resource.Parade 

    // public static void main(String[] args) {
    //     Deck deck = new Deck();
    //     deck.shuffle();
    //     Parade testParade = new Parade(deck);
    //     testParade.displayParade();
    // }
}