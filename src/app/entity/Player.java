package app.entity;

import java.util.*;
import app.resource.*; //imports card, deck and parade.java

public abstract class Player {
    private ArrayList<Card> playerHand = new ArrayList<Card>();
    private ArrayList<Card> collectedParadeCards = new ArrayList<Card>();
    private String name;
    // private boolean hasSixColors;

    public Player(Deck deck, String name) {
        for (int i = 0; i < 5; i++) {
            Card drawnCard = deck.drawCard();
            playerHand.add(drawnCard);
        }
        this.name  = name;
    }

    public ArrayList<Card> getPlayerHand()  {
        return this.playerHand;
    }
    
    public ArrayList<Card> getCollectedParadeCards() {
        return this.collectedParadeCards;
    }

    public String getName() {
        return this.name;
    }

    public boolean HasSixColors() {

        if (getCollectedParadeCards() == null || getCollectedParadeCards().size() < 6) {
            return false;
        }

        int numberOfCollectedCards = getCollectedParadeCards().size();
        Map<String, Integer> sixColours = new HashMap<>();
        for (int i = 0; i < numberOfCollectedCards; i++) {
            Card tempCard = getCollectedParadeCards().get(i);
            String cardColour = tempCard.getColour(); 
            // checks if cardColour exists in the map
            // if cardColour doesnt exist 
            // add the card color to the map + 1
            // else just add one to existing
            sixColours.put(cardColour, sixColours.getOrDefault(cardColour,0) + 1);
        }
        // if there are 6 entries, then return true. Shouldnt get more than 6 entries since only 6 unique colors
        if (sixColours.size() == 6) {
            // hasSixColors = true; //set hasSixColor to true? <----------------------------------------------------------
            return true;
        }
        return false;
    }

    public int countNumOfColouredCards(String colour) {
        if (getCollectedParadeCards() == null || getCollectedParadeCards().size() == 0) {
            return 0;
        }
        int count = 0;
        int numberOfCollectedCards = getCollectedParadeCards().size();

        for (int i = 0; i < numberOfCollectedCards; i++) {
            Card tempCard = getCollectedParadeCards().get(i);
            String cardColor = tempCard.getColour();
            if (cardColor.equals(colour)) {
                count++;
            }
        }
        return count;
    }

    //Marc
    public void collectEligibleCardsFromParade(Parade parade, Card playedCard) {
        ArrayList<Card> collectedCards = parade.removeEligibleCards(playedCard); //get the collected cards from parade as an array
        parade.addToParade(playedCard); //add the card played to the parade
        for (Card card : collectedCards) {
            collectedParadeCards.add(card);
        }
        playerHand.remove(playedCard);

        // // take cards from the parade on the following conditions
        // // take face value of the card
        // // if card value is 3, remove the last 3 cards from the available pool of cards
        // // iterate through the list and stop i < index size - X, 3 in this case
        // int numberOfCardsInParade = parade.getNumberOfCardsInParade();
        // // if parade has less cards than the facevalue, set eligible cards to 0 ie nothing. Else, check until the index
        // // 8 cards in parade
        // // play a 3
        // // 5 cards available, so go from index i = 0, i < 5; i++;

        // int eligibleCardIndex = numberOfCardsInParade - playedCard.getValue() <= 0 ? 0 : numberOfCardsInParade - playedCard.getValue(); 
        // for (int i = 0; i < eligibleCardIndex; i++) {
        //     Card currentParadeCard = parade.getParadeCards().get(i);
        //     if (currentParadeCard.getValue() <= playedCard.getValue() || currentParadeCard.getColour().equals(playedCard.getColour())) {
        //         this.collectedParadeCards.add(currentParadeCard);
        //         parade.removeEligibleCard(currentParadeCard);
        //     }
        // }
        // // if ((card.value <= placedCard.value) || (card.color.equals(placedCard.color))) {, yes its value <= value
        // //        remove from parade and add to players card list
        // //} 
        // // cards removed from the parade will be placed OPEN UP infront of the player, perhaps another attribute
        // // ArrayList<Cards> collectedParadeCards
        // // fill up the empty gaps, reorganise the board ArrayList 
        // // Finally, draw +1 card into player's hand CLOSED 
        // // redraws up to a hand of 5
    }
    public abstract void takeTurn(Deck deck, Parade parade);

    //Testing and Debugging
    //all commands run from parent folder of "app"
    //compile command: javac -d out -cp "out" src/app/entity/Player.java
}   
