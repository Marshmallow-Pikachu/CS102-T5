package app.entity;

import java.util.*;
import app.resource.*; //imports card, deck and parade.java

public class Player {
    private ArrayList<Card> playerHand;
    private ArrayList<Card> collectedParadeCards;
    private boolean haveSixColors;

    public void playCard() {
        // Players take turns
        // add 1 card to the end of the parade
        // then, take cards from the parade on the following conditions
        // take face value of the card
        // if card value is 3, remove the last 3 cards from the available pool of cards
        // OR
        // iterate through the list and stop i < index size - X, 3 in this case
        // if ((card.value <= placedCard.value) || (card.color.equals(placedCard.color))) {, yes its value <= value
        //        remove from parade and add to players card list
        //} 
        // cards removed from the parade will be placed OPEN UP infront of the player
        // ArrayList<Cards> collectedParadeCards
        // The parade will automatically left align
        // Finally, draw +1 card into player's hand CLOSED 
        // redraws up to a hand of 5
    }
    
}
