package app.utilities;

import java.util.ArrayList;

/**
 * Manages a list of lines, used for printing a pretty card.
 */
public class CardList {
    private ArrayList<String> lines;

    /**
     * Constructs a new CardList with 7 empty lines.
     */
    public CardList() {
        reset();
    }

    /**
     * Constructs a new CardList using an existing list of strings.
     * @param newList The list of strings to initialize the CardList.
     */
    public CardList(ArrayList<String> newList) {
        this.lines = newList;
    }

    /**
     * Resets the lines in the CardList, initializing it to contain 7 empty strings.
     */
    public void reset() {
        this.lines = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            lines.add("");
        }
    }

    /**
     * Checks if the lines list is null.
     * @return true if lines is null, false otherwise.
     */
    public boolean isNull() {
        if (this.lines == null) {
            return true;
        }
        return false;
    }

    /**
     * Method to add raw output from Render to itself
     * @param newList raw output from Render
     */
    public void add(ArrayList<String> newList) {
        for (int i = 0; i < 7; i++) {
            this.lines.set(i, this.lines.get(i) + "   " + newList.get(i));
        }
    }

    /**
     * Method to add another CardList to itself
     * @param newList another CardList
     */
    public void add(CardList newList) {
        for (int i = 0; i < 7; i++) {
            this.lines.set(i, this.lines.get(i) + "   " + newList.lines.get(i));
        }
    }

    /**
     * Outputs the cards as a string to be printed, then reset itself
     * @return The string version of the cards
     */
    public String output() {
        String output = "";
        for (String line : lines) {
            output += line + "\n";
        }
        reset();
        return output;
    }

    /**
     * Outputs the cards in list with spaces, and reset itself once done
     * It is used in printing of scoring zone
     * @param isSingleRow if the card list is by itself or with another row
     * @return A string version of the cards to be used in scoring zone
     */
    public String outputSpaced(boolean isSingleRow) {
        String output = "";
        if (isSingleRow) {
            for (String line : lines) {
                output += "|" + line + " ".repeat(47) + "   |\n";
            }
            
        } else {
            for (String line : lines) {
                output += "|" + line + "   |\n";
            }
        }
        reset();
        return output;
    }
}
