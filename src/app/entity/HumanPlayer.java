package app.entity;

import app.game.Game;
import app.resource.*;
import app.utilities.Printer;

/**
 * Represents a human player in the game of Parade.
 * This class handles human player interactions and decisions during their turn.
 */

public class HumanPlayer extends Player {
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
    public void discardCard(int index) {
        Card discardedCard = super.getPlayerHand().remove(index);
        Printer.printDiscard(discardedCard);
    }
}
