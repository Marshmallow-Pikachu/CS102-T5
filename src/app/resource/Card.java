package app.resource;

import java.lang.*;

public class Card {
    // Declaring ANSI_RESET so that we can reset the color 
    public static final String ANSI_RESET = "\u001B[0m"; 
  
    // Declaring the color (Red, Blue, Purple, Green, Grey, Orange) 
    public static final String ANSI_BLACK  = "\u001B[30m";
    public static final String ANSI_RED    = "\u001B[31m";
    public static final String ANSI_BLUE   = "\u001B[34m";
    public static final String ANSI_YELLOW = "\u001B[33m"; 
    public static final String ANSI_BRIGHT_GREEN  = "\u001B[92m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    
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
        return "Colour: " + colour + ", Number: " + value;
    }


    //Prints 1 Card (For Testing)
    public void printCard(){
        System.out.println(linkCardtoColour(colour) + "╭───╮");
        System.out.printf("│ %d │ %n", value);
        System.out.println("╰───╯" + ANSI_RESET);
    }

    //Gets String to Convert Colour
    public String linkCardtoColour(String colour){
        String cardColor = null;
        switch (colour){
            case "Red":
            return ANSI_RED;
            case "Blue":
            return ANSI_BLUE;
            case "Purple":
            return ANSI_PURPLE;
            case "Green":
            return ANSI_BRIGHT_GREEN;
            case "Black":
            return ANSI_BLACK;
            case "Yellow":
            return ANSI_YELLOW;
        }
        System.out.println("Error Color Not Matched!");
        return cardColor;
    }

}