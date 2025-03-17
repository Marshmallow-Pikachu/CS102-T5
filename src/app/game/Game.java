package app.game;

import app.entity.*;
import app.resource.*;
import java.util.*;


//import Parade/Players when done can use the above as example 


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


    public Game(ArrayList<String> playersName, boolean b){
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
        parade.printParade();
    } 
    
    public void initiateRound(){  //Player chooses a card from hand to play to parade
        //Calls collectEligibleCardsFromParade(Parade parade, Card c)
        System.out.println("New Round Start");
        for (Player p : players){
            p.takeTurn(deck, parade, gameEnd);
            //check for final round trigger
            if (p.hasSixColors()){ // Start Last Round Condition REMEMBER WHO STARTS THE LAST ROUND FIRST
                gameEnd = true;
                //System.out.println("Game End Triggered by Collected Colours");
            }
            if (deck.getIsEmpty()){ // Start Last Round Condition REMEMBER WHO STARTS THE LAST ROUND FIRST
                gameEnd = true;
                //System.out.println("Game End Triggered by Empty Deck");
            }
        }
    }


// If a player has collected the 6th color, she finishes her turn as before. After that, every player, including the one who collected the 6th color, plays one more turn. 
// However, the players do not draw a card from the draw pile.
// The game ends after this last round.

// If the draw pile is exhausted, every player plays one more turn. The game ends when everybody has only 4 cards left in their hand.
// Even if other players get their 6th color during the last round, this has no further effect. The games ends after the round.

    public void initiateFinalRound(ArrayList<Player> players){ // if a player has all 6 colors, or deck is empty
        //Starts off with the person who initiated last round
        // Reorder the players ArrayList? / Make another ArrayList with Person who started last round as first man? / Any other ideas?

        System.out.println("Final Round!!");
        for (Player p : players){
            p.takeTurn(deck, parade, gameEnd); // Change to p.takeTurnWithoutDrawing
        }
        //System.out.println("Test1");
        flipMajorityCards();
        //System.out.println("Test2");
        discardTwoCards(players);
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
    public ArrayList<Integer> getCountOfColors(ArrayList<Card> cards){
        ArrayList<Integer> count = new ArrayList<>(6);
        count.add(0);count.add(0);count.add(0);count.add(0);count.add(0);count.add(0);
        for (Card c : cards){
            switch (c.getColour()){
                case ("Red"):
                count.set(0, count.get(0)+1);
                break;
                case ("Blue"):
                count.set(1, count.get(1)+1);
                break;
                case ("Purple"):
                count.set(2, count.get(2)+1);
                break;
                case ("Green"):
                count.set(3, count.get(3)+1);
                break;
                case ("Black"):
                count.set(4, count.get(4)+1);
                break;
                case ("Yellow"):
                count.set(5, count.get(5)+1);
                break;
            }
        }
        return count;
    }

    //Flip cards, based on index in sequence
    public void flipCardsByColour(ArrayList<Card> cardList,int val){
        String colour = null;
        switch (val){
            case 0:
            colour = "Red";
            break;
            case 1:
            colour = "Blue";
            break;
            case 2:
            colour = "Purple";
            break;
            case 3: 
            colour = "Green";
            break;
            case 4: 
            colour = "Black";
            break;
            case 5: 
            colour = "Yellow";
            break;
        }
        //System.out.println("Colour: "+colour);
        for (Card c : cardList){
            //System.out.println(c.getColour());
            if (c.getColour().equals(colour)){
                //System.out.println("FLIPPPPPPPPPP");
                c.setFlipped();
            }
        }
    }

    public ArrayList<Integer> getCountOfColour(ArrayList<Card> cardList, ArrayList<String> colourList){
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


    public void flipMajorityCards(){
        if (players.size() == 2){ // 2 Players
            ArrayList<Card> p1 = players.get(0).getCollectedParadeCards();
            ArrayList<Card> p2 = players.get(1).getCollectedParadeCards();
            ArrayList<Integer> player1 = getCountOfColors(p1);
            ArrayList<Integer> player2 = getCountOfColors(p2);
            //System.out.println("TEST1: 2 Players HIT");
            for (int i = 0; i < 5; i++){
                int diff = player1.get(i) - player2.get(i);
                if (diff >= 2){
                    //System.out.println("Player 1 Flipping Index: " + i);
                    flipCardsByColour(p1, i);
                }else if (diff <= -2){
                    //System.out.println("Player 2 Flipping Index: " + i);
                    flipCardsByColour(p2, i);
                }
            }
        } 
        else{
            //System.out.println("TEST1: MORE THAN 2 Players HIT");
            ArrayList<String> colourList = new ArrayList<>();
            colourList.add("Red");colourList.add("Blue");colourList.add("Purple");colourList.add("Green");colourList.add("Black");colourList.add("Yellow");
            ArrayList<Integer> colourCount = new ArrayList<>(6);
            colourCount.add(0);colourCount.add(0);colourCount.add(0);colourCount.add(0);colourCount.add(0);colourCount.add(0);


            //System.out.println("TEST2");
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
                // System.out.println("SIZE: " + counts.size());
                // System.out.println("SIZE2: " + colourCount.size());

                for (int i = 0; i < 6; i++){ //Populate colourCount with MAX no. of colour in game
                    if (colourCount.get(i) < counts.get(i)){ //HERE!
                        colourCount.set(i, counts.get(i));
                    }
                }
            }
            System.out.println("Majority of Each Colour");
            for (int i = 0; i < 6; i++){
                System.out.println("Count of : " + colourList.get(i) + " = " + colourCount.get(i));
            }

            for (Player p: players){ //do flippings using the Majority of Each Colour
                //System.out.println(p.getName());
                ArrayList<Card> scoringZone = p.getCollectedParadeCards();
                //System.out.println(p.getName());
                ArrayList<Integer> numOfColours = getCountOfColors(scoringZone);
                // for (Integer i : numOfColours){
                //     System.out.println(i);
                // }
                // System.out.println("numOfColours : " + numOfColours);
                // System.out.println("numOfColours : " + colourCount);
                for (int i = 0; i < 6; i++){
                    if (numOfColours.get(i) == colourCount.get(i)){
                        //System.out.println("Flip for : " + p.getName() + " Colour: "+colourList.get(i) + " Count : " + numOfColours.get(i));
                        flipCardsByColour(scoringZone, i);
                    }
                }
            }

            //System.out.println("TEST3");
            // for (Player p: players){ //do flippings using the Majority of Each Colour
            //     System.out.println(p.getName());
            //     ArrayList<Card> scoringZone = p.getCollectedParadeCards();
            //     for (Card c : scoringZone){
            //         int i = 0;


            //         for (String colour : colourList){
            //             int countOfColour = (int) scoringZone.stream()
            //                                 .filter(card -> colour.equals(c.getColour()))
            //                                 .count();


            //             //System.out.println("HAHA");
            //             System.out.println("Count of "+colour + " : " + countOfColour);
            //             System.out.println("Count of MAX " + colour + " : " + colourCount.get(i));
            //             if (colourCount.get(i) == countOfColour){
            //                 System.out.println("Colourrr : "+ colourList.get(i));
            //                 System.out.println("INT VALUE: "+ i);
            //                 flipCardsByColour(scoringZone, i);
            //             }
            //             i++;
            //         }
            //     }
            // }

        }
    } 

    public void discardTwoCards(ArrayList<Player> players){
        for (Player p : players){
            p.discardCard(); //Discards card chosen by player and do not return anything
            p.discardCard();
            //p.emptyHandToScoringArea();
        }
    }

    public Player determineWinner(){
        //Count numbers in scoring zone
        int lowest = 10000;
        Player lowestcurrent = new HumanPlayer(new Deck(), "Temp");
        for (Player p : players){
            //System.out.println(p.getName());
            int count = 0;
            for (Card c : p.getCollectedParadeCards()){
                //System.out.println(c.getFlipped());
                if (c.getFlipped()){ //checked if flipped
                    //System.out.println("Flipped Card");
                    count++;
                } else{
                    count += c.getValue();
                }
            }
            if (lowest > count){
                lowest = count; 
                lowestcurrent = p;
            }
            System.out.println(p.getName() + " Points: " + count);
        }
        return lowestcurrent;
    }





    public static void main(String[] args) {
        //Game game = new Game();
        ArrayList<String> players = new ArrayList<>();
        players.add("Player 1");
        players.add("Player 2");
        // players.add("Player 3");
        // players.add("Player 4");
        Game game = new Game(players, true);

        while (game.gameEnd == false){
            game.initiateRound();
            System.out.println();
            System.out.println();
            game.displayGameState(game);
        }
        game.initiateFinalRound(game.players);
        game.displayGameState(game);
        System.out.println("Winner is : " + game.determineWinner().getName());
        //new Card(2,"Red").printCard();

    }
}
