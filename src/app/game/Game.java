package app.game;

import app.entity.*;
import app.resource.*;
import java.util.*;

// Compile Command
//javac -cp "src" src/app/game/Game.java 

public class Game {
    // Declaring ANSI_RESET so that we can reset the color 
    public static final String ANSI_RESET = "\u001B[0m"; 
  
    // Declaring the color (Red, Blue, Purple, Green, Black, Yellow) 
    public static final String ANSI_RED    = "\u001B[31m";
    public static final String ANSI_BLUE   = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_BRIGHT_GREEN  = "\u001B[92m";
    public static final String ANSI_BLACK  = "\u001B[30m";
    public static final String ANSI_YELLOW = "\u001B[33m";

    public static final ArrayList<String> colourList = new ArrayList<>();


    // Feel free to add to psvm to check if your parts integrate
    private final ArrayList<Player> players; //Added final (shouldnt break anything)
    private final Parade parade;
    public static Deck deck;
    private boolean gameEnd;


    public Game(ArrayList<String> playersName, boolean b){
        colourList.add("Red");
        colourList.add("Blue");
        colourList.add("Purple");
        colourList.add("Green");
        colourList.add("Black");
        colourList.add("Yellow");
        deck = new Deck();
        deck.shuffle();
        parade = new Parade(deck);
        players = new ArrayList<Player>();
        for (String s : playersName){ //Add Player into ArrayList
            players.add(makeHumanPlayer(s));
        }
        System.out.println("Game Started");
    }


    public Game(ArrayList<Player> players, Deck deck){
        colourList.add("Red");
        colourList.add("Blue");
        colourList.add("Purple");
        colourList.add("Green");
        colourList.add("Black");
        colourList.add("Yellow");
        parade = new Parade(deck);
        this.deck = deck;
        this.players = players;
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
        System.out.println("This error should not happen, Error Color Not Matched!");
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
    // public void printCard(Card c){
    //     System.out.println(linkCardtoColour(c) + "╭───╮");
    //     System.out.printf("│ %d │ %n", c.getValue());
    //     System.out.println("╰───╯" + ANSI_RESET);
    // }

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
            parade.set(0, parade.get(0) + colorCode + temp.get(0) + ANSI_RESET); //Add Characters to parade to print
            parade.set(1, parade.get(1) + colorCode + temp.get(1) + ANSI_RESET);
            parade.set(2, parade.get(2) + colorCode + temp.get(2) + ANSI_RESET);
        }
        System.out.println(parade.get(0));
        System.out.println(parade.get(1));
        System.out.println(parade.get(2));
    } 


    public void displayGameState(Game game){     //- display each players scoring zone, number of cards in deck, parade itself)
        //Show Player's Scoring Zone
        for (Player p : players){
            System.out.printf("Scoring Zone: (%s) ========================================== %n",p.getName());
            printScoringZone(p.getCollectedParadeCards());
        }
        System.out.println("Size of deck: " + deck.getCards().size());

        //Prints Parade 
        System.out.println("============================== Current Parade ===============================");
        parade.printParade();
    } 
    
    public void initiateRound(){ 
        System.out.println("New Round Start");
        for (Player p : players){
            p.takeTurn(deck, parade, players, gameEnd); // Player takes turn
            //check for final round trigger
            if (p.hasSixColors()){ // Start Last Round Condition REMEMBER WHO STARTS THE LAST ROUND FIRST
                gameEnd = true;
            }
            if (deck.getIsEmpty()){ // Start Last Round Condition REMEMBER WHO STARTS THE LAST ROUND FIRST
                gameEnd = true;
            }
        }
    }

    public void initiateFinalRound(ArrayList<Player> players){ // if a player has all 6 colors, or deck is empty
        //Starts off with the person who initiated last round
        // Reorder the players ArrayList? / Make another ArrayList with Person who started last round as first man? / Any other ideas?

        System.out.println("Final Round!!");
        for (Player p : players){
            p.takeTurn(deck, parade, players, gameEnd);
        }
        discardTwoCards();
        flipMajorityCards();
    }


    public boolean getGameEnd(){
        return gameEnd;
    }


// After the end of the game each player chooses 2 cards from their hand and discards them. 
// The remaining 2 cards in hand are added to those already in front of the player.
// Note: Each of these 2 cards will either be added to colors you already own, or create a new pile (if you do not already have any cards of that color).
// Only the cards lying in front of a player are scored. The cards still taking part in the parade are discarded.
// For each color, players must determine the number of points they score. Each color is dealt with separately.

// 1.  Determine who has the majority in each color. The player or players with the 
// most cards in each color flip those cards over and each of these cards counts as
// 1 point. (The value printed on the cards is not counted.)

// 2.  Each player will then add up the printed values of all their face up cards. Sum up
// this total with the total obtained from any face down cards.

// Get count of color in play zone in sequence of red, blue, purple, green, black, yellow
    // public ArrayList<Integer> getCountOfColors(ArrayList<Card> cards){
    //     ArrayList<Integer> count = new ArrayList<>(6);
    //     count.add(0);count.add(0);count.add(0);count.add(0);count.add(0);count.add(0); // Prevent NullPointerException
    //     for (Card c : cards){
    //         switch (c.getColour()){ //Count number of cards by colour
    //             case ("Red"):
    //             count.set(0, count.get(0)+1);
    //             break;
    //             case ("Blue"):
    //             count.set(1, count.get(1)+1);
    //             break;
    //             case ("Purple"):
    //             count.set(2, count.get(2)+1);
    //             break;
    //             case ("Green"):
    //             count.set(3, count.get(3)+1);
    //             break;
    //             case ("Black"):
    //             count.set(4, count.get(4)+1);
    //             break;
    //             case ("Yellow"):
    //             count.set(5, count.get(5)+1);
    //             break;
    //         }
    //     }
    //     return count; //returns list of num of red, blue, purple, green, black, yellow in sequence
    // }

    public ArrayList<Integer> getCountOfColors(ArrayList<Card> cardList, ArrayList<String> colourList){
        ArrayList<Integer> returnList = new ArrayList<>();
        returnList.add(0);returnList.add(0);returnList.add(0);returnList.add(0);returnList.add(0);returnList.add(0);
        for (Card c : cardList){
            for (int i = 0; i < 6; i++){
                if (c.getColour().equals(colourList.get(i))){
                    returnList.set(i, returnList.get(i) + 1);
                }
            }
        }
        return returnList;
    }

    //Flip cards, based on index in sequence
    public void flipCardsByColour(ArrayList<Card> cardList,int val){
        String colour = null;
        colour = colourList.get(val); //if theres errors remove the line and uncomment below
        // switch (val){
        //     case 0:
        //     colour = "Red";
        //     break;
        //     case 1:
        //     colour = "Blue";
        //     break;
        //     case 2:
        //     colour = "Purple";
        //     break;
        //     case 3: 
        //     colour = "Green";
        //     break;
        //     case 4: 
        //     colour = "Black";
        //     break;
        //     case 5: 
        //     colour = "Yellow";
        //     break;
        // }
        for (Card c : cardList){
            if (c.getColour().equals(colour)){
                c.setFlipped();
            }
        }
    }

    public void flipTwoPlayers(){
        ArrayList<Card> p1 = players.get(0).getCollectedParadeCards();
            ArrayList<Card> p2 = players.get(1).getCollectedParadeCards();
            ArrayList<Integer> player1 = getCountOfColors(p1,colourList);
            ArrayList<Integer> player2 = getCountOfColors(p2,colourList);
            for (int i = 0; i < 5; i++){
                int diff = player1.get(i) - player2.get(i); //Compare number of x colour cards in p1 vs p2
                if (diff >= 2){                             //if p1 has majority, flip the card
                    flipCardsByColour(p1, i);
                }else if (diff <= -2){                      //if p2 has majority, flip the card
                    flipCardsByColour(p2, i);
                }
            }
    }

    public ArrayList<Integer> getMajorityForEachColour(){
        ArrayList<Integer> colourCount = new ArrayList<>(6);
        colourCount.add(0);colourCount.add(0);colourCount.add(0);colourCount.add(0);colourCount.add(0);colourCount.add(0);
        for (Player p : players){ //Get Majority of Each Colour
            ArrayList<Card> collectedCards = p.getCollectedParadeCards();

            //Index 0: Red, Index 1: Blue, Index 2: Purple, Index 3: Green, Index 4: Black, Index 5, Yellow
            List<Integer> counts = new ArrayList<>(); //Gets Number of Each Color in Hand
            for (String colour : colourList) {
                long count = collectedCards.stream()
                                        .filter(card -> colour.equals(card.getColour()))
                                        .count();
                counts.add((int)count);
            }
            for (int i = 0; i < 6; i++){ //Populate colourCount with MAX no. of colour in game
                if (colourCount.get(i) < counts.get(i)){
                    colourCount.set(i, counts.get(i));
                }
            }
        }
        return colourCount;
    }


    public void flipMajorityCards(){
        if (players.size() == 2){ // 2 Players
            this.flipTwoPlayers();
        } 
        else{ // More than 2 players
            ArrayList<Integer> colourCount = this.getMajorityForEachColour();
            for (Player p: players){ //Check each player for majority colours
                ArrayList<Card> scoringZone = p.getCollectedParadeCards();
                ArrayList<Integer> numOfColours = getCountOfColors(scoringZone,colourList); // Get count of each colour
                for (int i = 0; i < 6; i++){
                    if (numOfColours.get(i) == colourCount.get(i)){ //If player has majority colours, flip those cards of that colour
                        flipCardsByColour(scoringZone, i);
                    }
                }
            }
        }
    } 

    public void discardTwoCards(){
        for (Player p : players){
            p.discardCard(); //Discards card chosen by player and do not return anything
            p.discardCard();
            p.emptyHandToScoringArea();
        }
    }

    public ArrayList<Player> determineWinner(){
        //Count numbers in scoring zone
        ArrayList<Player> returnList = new ArrayList<>();
        int lowest = 10000;
        Player lowestcurrent = new HumanPlayer(new Deck(), "Temp"); //Temp human player
        for (Player p : players){
            int count = 0;
            for (Card c : p.getCollectedParadeCards()){ // Count score for each player
                if (c.getFlipped()){ //checked if flipped
                    count++;
                } else{
                    count += c.getValue();
                }
            }
            if (lowest > count){ //Check if current player has lower score
                lowest = count; 
                lowestcurrent = p;
            } else if (lowest == count){ //Check if there is a draw
                returnList.add(p);
            }
            System.out.println(p.getName() + " Points: " + count);
        }
        returnList.add(lowestcurrent);
        return returnList;
    }

    public void printWinScreen(){
        ArrayList<Player> winnerList = this.determineWinner();
        if (winnerList.size() > 1){ //If Draw
            System.out.print("Draw between ");
            for (int i = 0; i < winnerList.size(); i++){
                System.out.printf(winnerList.get(i).getName());
                if (i != winnerList.size()-1){
                    System.out.printf(" and ");
                }
            }
            System.out.println(".");
        }else{
            System.out.println("Winner is : " + winnerList.get(0).getName());
        }
    }





    public static void main(String[] args) {
        //Game game = new Game();
        ArrayList<String> players = new ArrayList<>();
        players.add("Player 1");
        players.add("Player 2");
        players.add("Player 3");
        players.add("Player 4");
        Game game = new Game(players, true);

        while (game.gameEnd == false){
            game.initiateRound();
            System.out.println();
            System.out.println();
            game.displayGameState(game);
        }
        game.initiateFinalRound(game.players);
        game.displayGameState(game);
        game.printWinScreen();

        
    }
}
