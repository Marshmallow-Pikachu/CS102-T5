package app.utilities;

import java.util.ArrayList;

// class 
public class CardList {
    private ArrayList<String> lines;

    // When called, will create 7 lines of empty strings to store the card information
    public CardList() {
        reset();
    }


    public CardList(ArrayList<String> newList) {
        this.lines = newList;
    }

    // Reset the rows in PrintList
    public void reset() {
        this.lines = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            lines.add("");
        }
    }

    // To check if the rows have been set to null
    public boolean isNull() {
        if (this.lines == null) {
            return true;
        }
        return false;
    }

    // Add cards to be printed
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
