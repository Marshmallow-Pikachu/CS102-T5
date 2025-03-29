package app.entity;

import java.util.*;

import app.game.Game;
import app.resource.*;

/**
 * Represents a human player in the game of Parade.
 * This class handles human player interactions and decisions during their turn.
 */

public class HumanPlayer extends Player {

    private static final Scanner scanner = new Scanner(System.in); // Persistent Scanner
    /**
     * Constructs a HumanPlayer with a given deck and player name.
     * @param deck The deck from which the player will draw cards.
     * @param name The name of the human player.
     */
    public HumanPlayer(Deck deck, String name) {
        super(deck, name);
    }

    /**
     * Takes a turn for the human player by playing a card and draws a new one (if not game end).
     * @param game The current game instance.
     * @param playedCard The card the player decides to play this turn.
     */
    public void takeTurn(Game game, Card playedCard) {
        Parade parade = game.getParade();
        collectEligibleCardsFromParade(parade, playedCard);

        // If the game isn't ending, draw a card
        if (!game.getGameEnd()) {
            Deck deck = game.getDeck();
            super.getPlayerHand().add(deck.drawCard());
        }
    }
    /**
     * Prompts the player for a card to discard.
     * Will reprompt until a valid choice is made.
     */
    public void discardCard() {
        int index;
        while (true) {
            System.out.printf("%s: Select card to discard by entering its number (1 to " + super.getPlayerHand().size() + "):\n", getName());
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
}
