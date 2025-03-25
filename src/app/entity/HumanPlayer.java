package app.entity;

import java.util.*;
import app.resource.*;
import app.utilities.DisplayPlayerMenu;

public class HumanPlayer extends Player {

    private static final Scanner scanner = new Scanner(System.in); // Persistent Scanner
    // Players take turns
    // private ArrayList<Card> playerHand;
    // private ArrayList<Card> collectedParadeCards;
    // private boolean hasSixColors;

    public HumanPlayer(Deck deck, String name) {
        super(deck, name);
    }

    
    public void takeTurn(Deck deck, Parade parade, ArrayList<Player> players, boolean finalTurn) {
        DisplayPlayerMenu.displayGameState(parade, players, super.getName(), super.getPlayerHand());
        Card playedCard = DisplayPlayerMenu.promptPlayerForCardToPlay(super.getPlayerHand());
        collectEligibleCardsFromParade(parade, playedCard);
        if (!finalTurn) {
            super.getPlayerHand().add(deck.drawCard());
        }
    }

    public void discardCard() {
        int index;
        while (true) {
            System.out.println("Select card to discard by entering its number (1 to " + super.getPlayerHand().size() + "):");
            if (scanner.hasNextInt()) {
                index = scanner.nextInt() - 1;
                scanner.nextLine();
                if (index >=0 && index < super.getPlayerHand().size()) {
                    break; //exit loop on valid input
                }
            } else {
                scanner.nextLine(); //discard invalid input
            }
            System.out.println("Invalid choice. Please enter a number between 1 and " + super.getPlayerHand().size() + ".");
        }
        Card discardedCard = super.getPlayerHand().remove(index);
        System.out.println("You discarded: " + discardedCard);
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
