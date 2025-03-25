package app.entity;

import java.util.*;

import app.game.Game;
import app.resource.*;
import app.utilities.Printer;

public class HumanPlayer extends Player {
    // Players take turns
    // private ArrayList<Card> playerHand;
    // private ArrayList<Card> collectedParadeCards;
    // private boolean hasSixColors;

    public HumanPlayer(Deck deck, String name) {
        super(deck, name);
    }

    
    public void takeTurn(Game game) {
        // Print the game state and the players hand
        Printer.displayGameState(game);
        System.out.printf("%s Hand%n", super.getName());
        Printer.printCards(super.getPlayerHand());
        
        // Ask player to play a card
        Card playedCard = Printer.promptPlayerForCardToPlay(super.getPlayerHand());
        Parade parade = game.getParade();
        collectEligibleCardsFromParade(parade, playedCard);

        // If the game isn't ending, draw a card
        if (!game.getGameEnd()) {
            Deck deck = game.getDeck();
            super.getPlayerHand().add(deck.drawCard());
        }
    }



    // add 1 card to the end of the parade
    // then, take cards from the parade on the following conditions
    // take face value of the card
    // if card value is 3, remove the last 3 cards from the available pool of cards
    // OR
    // iterate through the list and stop i < index size - X, 3 in this case
    // if ((card.value <= placedCard.value) || (card.color.equals(placedCard.color))) {, yes its value <= value
    //        remove from parade and add to players card list
    //} 
    // cards removed from the parade will be placed OPEN UP infront of the player, perhaps another attribute
    // ArrayList<Cards> collectedParadeCards
    // fill up the empty gaps, reorganise the board ArrayList 
    // Finally, draw +1 card into player's hand CLOSED 
    // redraws up to a hand of 5

    //Testing and Debugging
    //compile command: javac -d out -cp "out" src/app/entity/HumanPlayer.java
    //execute command: java -cp "out" app.entity.HumanPlayer

    // public static void main(String[] args) {
    //     Deck deck = new Deck();
    //     deck.shuffle();
    //     Parade parade = new Parade(deck);
    //     HumanPlayer testHumanPlayer = new HumanPlayer(deck, "TestPlayer");
    //     testHumanPlayer.takeTurn(deck, parade);
    //     DisplayPlayerMenu.displayParadeAndMyHand(parade, testHumanPlayer.getPlayerHand());
    // }

}
