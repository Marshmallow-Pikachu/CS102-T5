package app.utilities;

import java.util.ArrayList;

// class 
/**
 * Manages a list of lines, used for printing a pretty card.
 */
public class CardList {
    private ArrayList<String> lines;

    // When called, will create 7 lines of empty strings to store the card information
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

    // Reset the rows in PrintList
    /**
     * Resets the lines in the CardList, initializing it to contain 7 empty strings.
     */
    public void reset() {
        this.lines = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            lines.add("");
        }
    }

    // To check if the rows have been set to null
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

    
    public void add(ArrayList<String> newList) {
        for (int i = 0; i < 7; i++) {
            this.lines.set(i, this.lines.get(i) + "   " + newList.get(i));
        }
    }

    public void add(CardList newList) {
        for (int i = 0; i < 7; i++) {
            this.lines.set(i, this.lines.get(i) + "   " + newList.lines.get(i));
        }
    }

    // Display cards in list, and reset itself once done
    public void output() {
        for (String line : lines) {
            System.out.println(line);
        }
        reset();
    }

    // Display cards in list with spaces, and reset itself once done
    public void outputSpaced(boolean isSingleRow) {
        if (isSingleRow) {
            for (String line : lines) {
                System.out.println("|" + line + " ".repeat(47) + "   |");
            }
            
        } else {
            for (String line : lines) {
                System.out.println("|" + line + "   |");
            }
        }
        reset();
    }
}
