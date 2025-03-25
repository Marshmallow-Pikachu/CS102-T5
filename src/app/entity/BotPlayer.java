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

    public void discardCard(ArrayList<Player> gamePlayers) {
        ArrayList<Integer> highestColourCounts = obtainHighestColourCounts(gamePlayers);
        ArrayList<Integer> botColourCounts = super.obtainPlayerColourCounts(super.getPlayerHand());
 
    }

    public ArrayList<Integer> obtainHighestColourCounts(ArrayList<Player> gamePlayers) {
        ArrayList<Integer> highestColourCounts = new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0, 0));
        for (Player p : gamePlayers){
            ArrayList<Integer> colourCount = obtainPlayerColourCounts(p.getCollectedParadeCards());
                for (int i = 0; i < 6; i++) {
                    if (colourCount.get(i) > highestColourCounts.get(i)) {
                        highestColourCounts.set(i, colourCount.get(i));
                    }
                }
            }
            return highestColourCounts;
        }
    

    // public ArrayList<Integer> getCountOfColors(ArrayList<Card> cardList, ArrayList<String> colourList){
    //     ArrayList<Integer> returnList = new ArrayList<>();
    //     returnList.add(0);returnList.add(0);returnList.add(0);returnList.add(0);returnList.add(0);returnList.add(0);
    //     for (Card c : cardList){
    //         for (int i = 0; i < 6; i++){
    //             if (c.getColour().equals(colourList.get(i))){
    //                 returnList.set(i, returnList.get(i) + 1);
    //             }
    //         }
    //     }
    //     return returnList;
    // }


    public void takeTurn(Deck deck, Parade parade, ArrayList<Player> players, boolean finalTurn) {
        Card chosenCard = determineCardChoice(super.getPlayerHand(), parade);
        System.out.println("Bot has chosen to play " + chosenCard.toString());
        collectEligibleCardsFromParade(parade, chosenCard);
        if (!finalTurn) {
            super.getPlayerHand().add(deck.drawCard());
        }
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
