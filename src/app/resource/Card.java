package app.resource;

import java.lang.*;

/**
 * Represents a card instance in the game with a color and number value. This class also helps set removable cards in the parade
 * when a card is played, and allows cards to be flipped in the end game.
 */
public class Card {
    private int value;
    private String colour;
    private boolean removalMode;
    private boolean flipped;

    /**
     * Constructor for Card with the specified value and color.
     * @param value The value of the card.
     * @param colour The color of the card.
     */
    public Card(int value, String colour) {
        this.value = value;
        this.colour = colour;
        removalMode = false;
        flipped = false;
    }
    /**
     * Gets the value of the card.
     * @return The value of the card.
     */
    public int getValue() {
        return value;
    }
    /**
     * Gets the color of the card.
     * @return The color of the card.
     */
    public String getColour() {
        return colour;
    }
    /**
     * Checks if the card is in removal mode.
     * @return True if the card is in removal mode, otherwise false.
     */
    public boolean getRemovalMode(){
        return removalMode;
    }
    /**
     * Checks if the card is flipped.
     * @return True if the card is flipped, otherwise false.
     */
    public boolean getFlipped() {
        return flipped;
    }
    /**
     * Sets the card's removal mode to true, allowing card to be removed from parade
     */
    public void setRemovalMode() {
        removalMode = true;
    }
    /**
     * Resets the card's removal mode to false.
     */
    public void resetRemovalMode() {
        removalMode = false;
    }
    /**
     * Flips the card for post game counting and to determine final score. 
     */
    public void setFlipped () {
        flipped = true;
    }
    /**
     * Returns a string representation of the card, including its color and value.
     * @return A string representing the card's attributes.
     */
    public String toString() {
        return "Colour: " + colour + ", Number: " + value;
    }
}