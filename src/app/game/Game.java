package app.game;

import java.util.*;

import app.entity.*;
import app.resource.*;
import app.utilities.*;
import java.lang.reflect.Array;


//import Parade/Players when done can use the above as example 


// Compile Command
//javac -cp "src" src/app/game/Game.java 

public class Game {
    // Declaring ANSI_RESET so that we can reset the color 
    public static final String ANSI_RESET = "\u001B[0m"; 
  
    // Declaring the color (Red, Blue, Purple, Green, Grey, Orange) 
    // Custom declaration 
    public static final String ANSI_BLACK  = "\u001B[30m";
    public static final String ANSI_RED    = "\u001B[31m";
    public static final String ANSI_BLUE   = "\u001B[34m";
    public static final String ANSI_YELLOW = "\u001B[33m"; 
    public static final String ANSI_BRIGHT_GREEN  = "\u001B[92m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    //public static final String ANSI_GRAY = "\u001B[38;5;240m";  // Darker gray

    // Feel free to add to psvm to check if your parts integrate
    private ArrayList<Player> players;
    private Parade parade;
    public static Deck deck;
    private boolean gameEnd;


    // public Game(ArrayList<Player> players, Deck deck){
    //     addPlayers(players);    
    //     System.out.println("Game Started");
    // }

    // public Game(){ //For Testing Purposes Only
    //     deck = new Deck();
    //     parade = new Parade(deck);
    //     System.out.println("Game Started");
    // }


    public Game(ArrayList<String> playersName){
        deck = new Deck();
        parade = new Parade(deck);
        players = new ArrayList<Player>();
        for (String s : playersName){ //Add Player into ArrayList
            players.add(makeHumanPlayer(s));
        }
        System.out.println("Game Started");
    }

    public static Player makeHumanPlayer(String name){ //For Testing Only
        Player p = new HumanPlayer(deck, name);
        return p;
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

    public void printScoringZone(ArrayList<Card> cards){
        if (cards.isEmpty()){
            System.out.println("No Cards Collected");
            return;
        }
        ArrayList<Card> redCards = new ArrayList<>();
        ArrayList<Card> blueCards = new ArrayList<>();
        ArrayList<Card> purpleCards = new ArrayList<>();
        ArrayList<Card> greenCards = new ArrayList<>();
        ArrayList<Card> blackCards = new ArrayList<>();
        ArrayList<Card> yellowCards = new ArrayList<>();
        for (Card c : cards){
            switch (c.getColour()){
                case "Red":
                redCards.add(c);
                break;
                case "Blue":
                blueCards.add(c);
                break;
                case "Purple":
                purpleCards.add(c);
                break;
                case "Green":
                greenCards.add(c);
                break;
                case "Black":
                blackCards.add(c);
                break;
                case "Yellow":
                yellowCards.add(c);
                break;
            }
        }
        printSameColor(redCards, ANSI_RED);
        printSameColor(blueCards, ANSI_BLUE);
        printSameColor(purpleCards, ANSI_PURPLE);
        printSameColor(greenCards, ANSI_BRIGHT_GREEN);
        printSameColor(blackCards, ANSI_BLACK);
        printSameColor(yellowCards, ANSI_YELLOW);
    }

    //Prints all card of same Color in 1 Row
    public void printSameColor(ArrayList<Card> cards, String cardColor){
        if (cards.isEmpty()){
            return;
        }
        ArrayList<String> printString = new ArrayList<>();
        printString.add("");
        printString.add("");
        printString.add("");
        for (Card c : cards){
            ArrayList<String> string = printCardString(c);
            printString.set(0, printString.get(0) + string.get(0));
            printString.set(1, printString.get(1) + string.get(1));
            printString.set(2, printString.get(2) + string.get(2));
        }
        //Print out all cards that are XXX Color 
        System.out.println(cardColor + printString.get(0));
        System.out.println(printString.get(1));
        System.out.println(printString.get(2) + ANSI_RESET);
    }

    //Gets String to append to print everything in same line
    public ArrayList<String> printCardString(Card c){
        ArrayList<String> returnval = new ArrayList<String>();
        returnval.add(0, "╭───╮");
        String toInsert;
        if (c.getValue() != 10){
            toInsert = "│ "+c.getValue()+" │";
        } else{
            toInsert = "│ "+c.getValue()+"│";
        }
        returnval.add(1, toInsert);
        returnval.add(2, "╰───╯");
        return returnval;
    }
    //Prints 1 Card (For Testing)
    public void printCard(Card c){
        System.out.println(linkCardtoColour(c) + "╭───╮");
        System.out.printf("│ %d │ %n", c.getValue());
        System.out.println("╰───╯" + ANSI_RESET);
    }

    // public void printParade(ArrayList<Card> cards){ //to replace with Parade parade
    //     ArrayList<String> parade = new ArrayList<>();
    //     parade.add("");         
    //     parade.add(""); 
    //     parade.add("");
    //     for (Card c : cards){
    //         String colorCode = linkCardtoColour(c);
    //         ArrayList<String> temp = printCardString(c);
    //         parade.set(0, parade.get(0) + colorCode + temp.get(0) + ANSI_RESET);
    //         parade.set(1, parade.get(1) + colorCode + temp.get(1) + ANSI_RESET);
    //         parade.set(2, parade.get(2) + colorCode + temp.get(2) + ANSI_RESET);
    //         //System.out.println(parade.get(2));
    //         //System.out.println();
    //     }
    //     System.out.println(parade.get(0));
    //     System.out.println(parade.get(1));
    //     System.out.println(parade.get(2));
    // } 


    public void printParade(Parade p){
        ArrayList<String> parade = new ArrayList<>();
        parade.add("");         
        parade.add(""); 
        parade.add("");
        for (Card c : p.getParadeCards()){
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


    public void displayGameState(Game game){     //- display each players scoring zone, number of cards in deck, parade itself)
        //Show Player's Scoring Zone

        //Test Printing Card
        // System.out.printf("Scoring Zone: (%s) ========================================== %n","Player1Name");
        // ArrayList<Card> testList = new ArrayList<>();
        // testList.add(new Card(5, "Blue"));
        // testList.add(new Card(4, "Red"));
        // testList.add(new Card(2, "Yellow"));
        // testList.add(new Card(9, "Green"));
        // printScoringZone(testList); //replace with player's scoring Zone instead of deck

        for (Player p : players){
            System.out.printf("Scoring Zone: (%s) ========================================== %n",p.getName());
            printScoringZone(p.getCollectedParadeCards());

        }

        System.out.println("Size of deck: " + deck.getCards().size());

        //Prints Parade 
        // ArrayList<Card> testList = new ArrayList<>();
        // testList.add(new Card(5, "Blue"));
        // testList.add(new Card(4, "Red"));
        // testList.add(new Card(2, "Yellow"));
        // testList.add(new Card(9, "Green"));

        System.out.println("============================== Current Parade ===============================");
        //printParade(testList);
        printParade(parade);
    } 
    
    public void initiateRound(){  //Player chooses a card from hand to play to parade
        //Calls collectEligibleCardsFromParade(Parade parade, Card c)
        System.out.println("New Round Start");
        for (Player p : players){
            p.takeTurn(deck, parade);
            //check for final round trigger
            if (p.hasSixColors() || deck.getIsEmpty()){ // Start Last Round Condition REMEMBER WHO STARTS THE LAST ROUND FIRST
                gameEnd = true;
            }
        }
    }


// If a player has collected the 6th color, she finishes her turn as before. After that, every player, including the one who collected the 6th color, plays one more turn. 
// However, the players do not draw a card from the draw pile.
// The game ends after this last round.

// If the draw pile is exhausted, every player plays one more turn. The game ends when everybody has only 4 cards left in their hand.
// Even if other players get their 6th color during the last round, this has no further effect. The games ends after the round.

    public void initiateFinalRound(){ // if a player has all 6 colors, or deck is empty
        //Starts off with the person who initiated last round
        // Reorder the players ArrayList? / Make another ArrayList with Person who started last round as first man? / Any other ideas?

    }


    public boolean getGameEnd(){
        return gameEnd;
    }


// After the end of the game each player chooses 2 cards from their hand and discardsthem. 
// The remaining 2 cards in hand are added to those already in front of the player.
// Note: Each of these 2 cards will either be added to colors you already own, or create a new pile (if you do not already have any cards of that color).
// Only the cards lying in front of a player are scored. The cards still taking part in the parade are discarded.
// For each color, players must determine the number of points they score. Each color is dealt with separately.

// 1.  Determine who has the majority in each color. The player or players with the 
// most cards in each color flip those cards over and each of these cards counts as
// 1 point. (The value printed on the cards is not counted.)

// 2.  Each player will then add up the printed values of all their face up cards. Sum up
// this total with the total obtained from any face down cards.

    public void flipMajorityCards(){  // make sure to check if it’s a two player game
        if (players.size() == 2){

        } else{

        }
    } 

    // public Player determineWinner(){
    //     return new Player();
    // }





    public static void main(String[] args) {
        //Game game = new Game();
        ArrayList<String> players = new ArrayList<>();
        players.add("Player 1");
        players.add("Player 2");
        Game game = new Game(players);

        // while (game.gameEnd == false){
            //game.initiateRound();
            game.displayGameState(game);
        // }
        game.initiateFinalRound();

    }
}
