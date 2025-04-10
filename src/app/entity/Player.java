package app.entity;

import java.util.*;
import app.resource.*; //imports card, deck and parade.java
import app.game.*; // imports game
/**
 * This class contains the skeleton that will be common between HumanPlayer and BotPlayer
 * It holds 1 abstract method, which is takeTurn() 
 */
public abstract class Player {
    private ArrayList<Card> playerHand = new ArrayList<Card>();
    private ArrayList<Card> collectedParadeCards = new ArrayList<Card>();
    private String name;

    /**
     * Player constructor, called by BotPlayer or HumanPlayer. Cannot be directly initialised. 
     *
     * @param deck The shuffled instance of deck that everyone will be using that game
     * @param name The name of the player (Either entered by human player or insert default bot name)
     */
    public Player(Deck deck, String name) {
        for (int i = 0; i < 5; i++) {
            Card drawnCard = deck.drawCard();
            playerHand.add(drawnCard);
        }
        this.name  = name;
    }
    /**
     * PlayerHand getter
     * @return Returns the player's current hand
     */
    public ArrayList<Card> getPlayerHand()  {
        return this.playerHand;
    }
    /**
     * Player's collected parade pool cards
     * @return Returns the cards that player has collected from the parade
     */
    public ArrayList<Card> getCollectedParadeCards() {
        return this.collectedParadeCards;
    }
    /**
     * Player Name getter
     * @return Returns the player's name
     */
    public String getName() {
        return this.name;
    }
    /**
     * Uses a hashmap to check if the player instance has cards of the 6 colors in his collected parade pool.
     * If true, this will trigger game end final round. 
     * @return returns true of player has 6 colors in his parade
     */
    public boolean hasSixColors() {

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
            return true;
        }
        return false;
    }
    /**
     * Returns the number of cards of a certain color that the player has in their collected zone
     * @param collectedCards The cards in the collection zone to count the colors for
     * @return colourCount Returns an ArrayList of type Integer with the card counts in order of "Red", "Blue", "Purple", "Green", "Black", "Yellow"
     */
    public ArrayList<Integer> obtainPlayerColourCounts (ArrayList<Card> collectedCards){ 
        ArrayList<String> colourList = new ArrayList<>(Arrays.asList("Red", "Blue", "Purple", "Green", "Black", "Yellow"));
        ArrayList<Integer> colourCount = new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0, 0));
        for (Card c : collectedCards) {
            for (int i = 0; i < 6; i++) {
                if (c.getColour().equals(colourList.get(i))) {
                    colourCount.set(i, colourCount.get(i) + 1);
                }
            }
        }
        return colourCount;
    }

    /**
     * Returns the number of cards of color X that the player has in his collected parade cards
     * @param colour The color of which you want to find the number of cards for
     * @return count The number of cards of that color
     */

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

    /**
     * This method is used for the final round only.
     * This method will add a card to the player's collected parade card pool if the player has cards remaining
     */
    public void emptyHandToScoringArea() {
        while (!playerHand.isEmpty()){
            collectedParadeCards.add(playerHand.remove(0));
        }
    }
    
    /**
     * This method collects eligible cards from the parade, which is dependent on the card the player has played
     * @param parade The common parade that everyone is using
     * @param playedCard The card the player plays, which determines what cards he draws, if any
     */
    public void collectEligibleCardsFromParade(Parade parade, Card playedCard) {
        ArrayList<Card> collectedCards = parade.removeEligibleCards(playedCard); //get the collected cards from parade as an array
        parade.addToParade(playedCard); //add the card played to the parade
        for (Card card : collectedCards) {
            collectedParadeCards.add(card);
        }
        playerHand.remove(playedCard);

    }
    /**
     * Abstract method to be implemented differently for BotPlayer and HumanPlayer
     * @param game Takes in an instance of the current game
     * @param playedCard The card that the player/bot plays
     */
    public abstract void takeTurn(Game game, Card playedCard);
}   
