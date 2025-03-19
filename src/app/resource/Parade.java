package app.resource;

import java.util.*;

public class Parade {
    // Declaring ANSI_RESET so that we can reset the color 
    public static final String ANSI_RESET = "\u001B[0m"; 
  
    // Declaring the color (Red, Blue, Purple, Green, Grey, Orange) 
    public static final String ANSI_BLACK  = "\u001B[30m";
    public static final String ANSI_RED    = "\u001B[31m";
    public static final String ANSI_BLUE   = "\u001B[34m";
    public static final String ANSI_YELLOW = "\u001B[33m"; 
    public static final String ANSI_BRIGHT_GREEN  = "\u001B[92m";
    public static final String ANSI_PURPLE = "\u001B[35m";

    private ArrayList<Card> paradeCards = new ArrayList<Card>();

    public Parade(Deck deck) { //This constructor should only ever be called once in a usual game of Parade
        // draw 6 cards from deck and add it to parade
        for (int i = 0; i < 6; i++) {
            Card drawnCard = deck.drawCard();
            paradeCards.add(drawnCard);
        }
    }

    public ArrayList<Card> getParadeCards() {
        return this.paradeCards;
    }

    public ArrayList<Card> removeEligibleCards(Card playedCard) {
        ArrayList<Card> removableCardsFromParadeList = new ArrayList<>(); 
        if (playedCard.getValue() >= paradeCards.size()) {
            return removableCardsFromParadeList;
        }

        for(int i = 0; i <= paradeCards.size() - playedCard.getValue() - 1; i++) {
            Card currentCard = paradeCards.get(i);
            currentCard.setRemovalMode();
        }

        for (Card currentParadeCard:paradeCards) {
            if (currentParadeCard.getRemovalMode() == true && (currentParadeCard.getValue() <= playedCard.getValue() || currentParadeCard.getColour().equals(playedCard.getColour()))) {
                removableCardsFromParadeList.add(currentParadeCard); 
            }
        }
        for(Card removingCard:removableCardsFromParadeList) {
            paradeCards.remove(removingCard);
        }
        return removableCardsFromParadeList;
    }

    //public ArrayList<Card> removeEligibleCards(Card playedCard) {
    //     ArrayList<Card> removableCardsFromParadeList = new ArrayList<>(); //list of cards that are removed from parade

    //     for (Card currentParadeCard : paradeCards) {
    //         // checking eligibility of cards to be removed from Parade to Players' Hand
    //         if (currentParadeCard.getValue() <= playedCard.getValue() || currentParadeCard.getColour().equals(playedCard.getColour())) {
    //             removableCardsFromParadeList.add(currentParadeCard);  
    //         }
    //     }
    //     paradeCards.removeAll(removableCardsFromParadeList);
    //     return removableCardsFromParadeList;
    // }

    public void addToParade(Card playedCard) {
        paradeCards.add(playedCard); // add playedCard to paradeCards
    }

    public void displayParade() {
        System.out.println("Current Parade");
        for(Card card:paradeCards) {
            System.out.println(card.toString());
        }
    }

        //Gets String to Convert Colour
        public String linkCardtoColour(Card c){
            String cardColor = null;
            switch (c.getColour()){
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
    
        //Gets String to append to print everything in same line
        public ArrayList<String> printCardString(Card c){
            ArrayList<String> returnval = new ArrayList<String>();
            returnval.add(0, "╭───╮");
            String toInsert;
            if (c.getValue() != 10){
                toInsert = "│ "+c.getValue()+" │";
            } else{
                toInsert = "│ T │";
            }
            returnval.add(1, toInsert);
            returnval.add(2, "╰───╯");
            return returnval;
        }
    
        public void printParade(){
            System.out.println("============ Parade ============");
            ArrayList<String> parade = new ArrayList<>();
            parade.add("");         
            parade.add(""); 
            parade.add("");
            for (Card c : paradeCards){
                String colorCode = linkCardtoColour(c);
                ArrayList<String> temp = printCardString(c);
                parade.set(0, parade.get(0) + colorCode + temp.get(0) + ANSI_RESET);
                parade.set(1, parade.get(1) + colorCode + temp.get(1) + ANSI_RESET);
                parade.set(2, parade.get(2) + colorCode + temp.get(2) + ANSI_RESET);
                //System.out.println(parade.get(2));
                //System.out.println();
            }
            System.out.println(parade.get(0));
            System.out.println(parade.get(1));
            System.out.println(parade.get(2));
        } 
    

    //testing and debug
    //commands are run from parent folder of "app", remember to go compile Deck and Card First
    //compile command: javac -d out -cp "src" src/app/resource/parade.java
    //execute command: java -cp "out" app.resource.Parade 

    // public static void main(String[] args) {
    //     Deck deck = new Deck();
    //     deck.shuffle();
    //     Parade testParade = new Parade(deck);
    //     testParade.displayParade();
    // }
}