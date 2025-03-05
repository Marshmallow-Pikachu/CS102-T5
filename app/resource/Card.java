package app.resource;

public class Card {
    private int value;
    private String colour;
    private boolean removalMode;
    private boolean flipped;

    public Card(int value, String colour) {
        this.value = value;
        this.colour = colour;
        removalMode = false;
        flipped = false;
    }

    public int getValue() {
        return value;
    }

    public String getColour() {
        return colour;
    }

    public boolean getRemovalMode(){
        return removalMode;
    }

    public boolean getFlipped() {
        return flipped;
    }

    public void setRemovalMode() {
        removalMode = true;
    }

    public void setFlipped () {
        flipped = true;
    }

    public String toString() {
        return "Colour: " + colour + " Number: " + value;
    }
}