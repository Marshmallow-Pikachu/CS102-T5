package app.game;

import app.entity.*;
import app.resource.*;
import app.utilities.Printer;
import java.util.*;

/**
 * Handles the main gameplay logic for Parade. This class manages the game state,
 * the parade,  deck, and player turns.
 */
public class Game {
    public static final ArrayList<String> colourList = new ArrayList<>(Arrays.asList("Red", "Blue", "Purple", "Green", "Black", "Yellow"));
    private final ArrayList<Player> players;
    private final Parade parade;
    private Deck deck;
    private boolean gameEnd;

    /**
     * Constructs a new game with the specified players and a deck.
     * @param players The list of players participating in the game.
     * @param deck The current deck instance for the game.
     */
    public Game(ArrayList<Player> players, Deck deck){
        parade = new Parade(deck);
        this.deck = deck;
        this.players = players;
        System.out.println("Game Started");
    }
    /**
     * Returns the parade instance.
     * @return The current parade.
     */
    public Parade getParade() {
        return this.parade;
    }
    /**
     * Returns the list of players in the game.
     * @return The players currently in the game.
     */
    public ArrayList<Player> getPlayers() {
        return this.players;
    }
    /**
     * Retrieves the current player, who is always the first in the list.
     * @return The current player.
     */
    public Player getCurrentPlayer() {
        return this.players.get(0);
    }
    /**
     * Retrieves the deck used in the game.
     * @return The deck.
     */
    public Deck getDeck() {
        return this.deck;
    }
        /**
     * Retrieves state of gameend.
     * @return truth value of gameend.
     */
    public boolean getGameEnd(){
        return gameEnd;
    }

    // If it is a bot player, just pass -1
    /**
     * Starts the next player's turn.
     * Checks if game end is to be triggered.
     * Removes the current player who is first in the list, and places him at the last position of the list. 
     * @param playedCard The card played by the current player.
     */
    public void nextTurn(Card playedCard){ 
        Player p = getCurrentPlayer();
        p.takeTurn(this, playedCard);
        
        //check for final round trigger
        if (p.hasSixColors()){ // Start Last Round Condition REMEMBER WHO STARTS THE LAST ROUND FIRST
            gameEnd = true;
        }
        if (deck.getIsEmpty()){ // Start Last Round Condition REMEMBER WHO STARTS THE LAST ROUND FIRST
            gameEnd = true;
        }
        this.players.remove(p);
        players.add(p);
    }
    /**
     * This method starts the final round, where players have to discard 2 cards from their hand and flip the colors which they have majority in color.
     * @param players The list of players in the game
     */
    public void initiateFinalRound(ArrayList<Player> players){ // if a player has all 6 colors, or deck is empty
        discardTwoCards();
        flipMajorityCards();
    }

    //This Function needs to be uncommented and changed, discardCard() is not longer a function of player, it now has seperate implementations in HumanPlayer and BotPlayer
    /**
     * This method helps human players and bot players discard their cards. 
     */
    public void discardTwoCards(){
        for (Player p : players){
            if (p instanceof HumanPlayer) {
                HumanPlayer human = (HumanPlayer)p;
                Printer.printRenderedHand(p);
                human.discardCard();
                Printer.printRenderedHand(p);
                human.discardCard();
                human.emptyHandToScoringArea();
            } else{
                BotPlayer bot = (BotPlayer)p;
                bot.discardCards(players);
                bot.emptyHandToScoringArea();
            }
        }
    }
    /**
     * Calculates the count of each color in a list of cards.
     * @param cardList The list of cards.
     * @param colourList The list of 6 colors.
     * @return An ArrayList of type Integer containing the counts of each color.
     */
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

     /**
     * Flips cards of a specific color based on the given index, which corresponds to a color in the color list.
     * @param cardList The list of cards to be flipped.
     * @param val The index in the color list to flip.
     */
    public void flipCardsByColour(ArrayList<Card> cardList,int val){
        String colour = null;
        colour = colourList.get(val); //if theres errors remove the line and uncomment below
        for (Card c : cardList){
            if (c.getColour().equals(colour)){
                c.setFlipped();
            }
        }
    }
    /**
     * Logic for 2 players, which runs with a separate flipping logic
     * Flips cards for two players based on their collected card counts. 
     * Cards are flipped if one player has 2 more of a color than the other.
     */
    public void flipTwoPlayers(){
        ArrayList<Card> p1 = players.get(0).getCollectedParadeCards();
            ArrayList<Card> p2 = players.get(1).getCollectedParadeCards();
            ArrayList<Integer> player1 = getCountOfColors(p1,colourList);
            ArrayList<Integer> player2 = getCountOfColors(p2,colourList);
            for (int i = 0; i < 6; i++){
                int diff = player1.get(i) - player2.get(i); //Compare number of x colour cards in p1 vs p2
                if (diff >= 2){                             //if p1 has majority, flip the card
                    flipCardsByColour(p1, i);
                }else if (diff <= -2){                      //if p2 has majority, flip the card
                    flipCardsByColour(p2, i);
                }
            }
    }
    /**
     * Calculates the highest number of each color collected by any player.
     * @return An ArrayList of type Integer containing the highest counts for each color.
     */
    public ArrayList<Integer> getMajorityForEachColour(){
        ArrayList<Integer> colourCount = new ArrayList<>(6);
        colourCount.add(0);colourCount.add(0);colourCount.add(0);colourCount.add(0);colourCount.add(0);colourCount.add(0);
        for (Player p : players){ //Get Majority of Each Colour
            //ArrayList<Card> collectedCards = p.getCollectedParadeCards(); 
            //Index 0: Red, Index 1: Blue, Index 2: Purple, Index 3: Green, Index 4: Black, Index 5, Yellow
            // List<Integer> counts = new ArrayList<>(); //Gets Number of Each Color in Hand
            // for (String colour : colourList) {
            //     long count = collectedCards.stream()
            //                             .filter(card -> colour.equals(card.getColour()))
            //                             .count();
            //     counts.add((int)count);
            // }
            
            //Populate colourCount with MAX no. of colour in game
            ArrayList<Integer> counts = p.obtainPlayerColourCounts(p.getPlayerHand()); //Ive not verified this method from player so im leaving the above to use in case this somehow breaks
            for (int i = 0; i < 6; i++){ 
                if (colourCount.get(i) < counts.get(i)){
                    colourCount.set(i, counts.get(i));
                }
            }
        }
        return colourCount;
    }
    /**
     * For Games with more than 2 players.
     * Flips cards for each player based on who holds the majority of each color.
     */
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
    /**
     * Determines the winner of the game by counting points. 
     * Flipped cards count for 1 point, ignoring face value.
     * @return An ArrayList of players who have the lowest score. Can have multiple players with the same score and tie.
     */
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
    /**
     * Checks if the game has ended.
     * @return True if the game has ended, otherwise false.
     */
}
