package app.entity;

import app.game.Game;
import app.resource.*;

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
     * Prompts the user for a card to discard
     * @param index the index of the card to be discarded
     * @return the card that is discarded
     * @throws IllegalArgumentException when the index is incorrect
     */
    public Card discardCard(int index) throws IllegalArgumentException {
        if (index < 0 || index > super.getPlayerHand().size() -1  ) {
            throw new IllegalArgumentException(); // catch below
        }
        return super.getPlayerHand().remove(index);
    }
}
