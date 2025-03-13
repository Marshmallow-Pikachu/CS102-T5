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
    public void discardCard() {
        int index;
        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.println("Select card to discard by entering its number (1 to " + playerHand.size() + "):");
                if (scanner.hasNextInt()) {
                    index = scanner.nextInt() - 1;
                    if (index >=0 && index < playerHand.size()) {
                        break; //exit loop on valid input
                    }
                } else {
                    scanner.nextLine(); //discard invalid input
                }
                System.out.println("Invalid choice. Please enter a number between 1 and " + playerHand.size() + ".");
            }
            playerHand.remove(index);
        }
    }

    public void emptyHandToScoringArea() {
        while (!playerHand.isEmpty()){
            collectedParadeCards.add(playerHand.remove(0));
        }
    }
    

    public void collectEligibleCardsFromParade(Parade parade, Card playedCard) {
        ArrayList<Card> collectedCards = parade.removeEligibleCards(playedCard); //get the collected cards from parade as an array
        parade.addToParade(playedCard); //add the card played to the parade
        for (Card card : collectedCards) {
            collectedParadeCards.add(card);
        }
        playerHand.remove(playedCard);

    }
    public abstract void takeTurn(Deck deck, Parade parade, boolean finalTurn);

    //Testing and Debugging
    //all commands run from parent folder of "app"
    //compile command: javac -d out -cp "out" src/app/entity/Player.java
}   
