package app.game;

import app.entity.*;
import app.resource.*;
import java.util.*;

/**
 * Handles the main gameplay logic for Parade. This class manages the game state,
 * the parade,  deck, and player turns.
 */
public class Game {

    /**
     * To initialise the possible colours in the card game
     */
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
        //check for final round triggers
        if (p.hasSixColors()){ 
            gameEnd = true;
        }
        if (deck.getIsEmpty()){
            gameEnd = true;
        }

        // Move first player to last place in arraylist
        this.players.remove(p);
        players.add(p);
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
            //Populate colourCount with MAX no. of colour in game
            ArrayList<Integer> counts = p.obtainPlayerColourCounts(p.getCollectedParadeCards()); //Ive not verified this method from player so im leaving the above to use in case this somehow breaks
            //System.out.println("counts" + counts);
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
     * Method to get the scores of each player
     * @return a map of Name : score of each player
     */
    public Map<String, Integer> calculateScore() {
        Map<String, Integer> scoreList = new HashMap<>();
        for (Player p : players) {
            int count = 0;
            for (Card c : p.getCollectedParadeCards()){ // Count score for each player
                if (c.getFlipped()){ //checked if flipped
                    count++;
                } else{
                    count += c.getValue();
                }
            }
            scoreList.put(p.getName(), count);
        }

        return scoreList;
    }

    /**
     * Determines the winner of the game by counting points. 
     * Flipped cards count for 1 point, ignoring face value.
     * @return An ArrayList of players who have the lowest score. Can have multiple players with the same score and tie.
     */
    public ArrayList<Player> determineWinner(){
        //Count numbers in scoring zone
        ArrayList<Player> winnerList = new ArrayList<>();
        int lowest = 10000;
        Map<String,Integer> scoreList = this.calculateScore();

        for (Player p : players) {                  //find lowest score
            if (scoreList.get(p.getName()) < lowest){
                lowest = scoreList.get(p.getName());
            }
        }

        for (Player p1 : players){
            if (scoreList.get(p1.getName()) == lowest){
                winnerList.add(p1);
            }
        }


        if (winnerList.size() > 1){                 // if draw by score, check number of cards in each winner's scoring zone to find true winner, fewest number wins
            ArrayList<Player> newWinnerList = new ArrayList<>();
            int noCardInScoringZone = 66;
            System.out.println("Draw by Score (Winner chosen by who has less cards in scoring zone)");
            for (Player p2 : winnerList){           //find smallest no of cards in scoring zone
                if (p2.getCollectedParadeCards().size() < noCardInScoringZone){
                    noCardInScoringZone = p2.getCollectedParadeCards().size();
                }
            }

            for (Player p2 : winnerList){           // find player(s) with the smallest no of cards in scoring zone
                if (p2.getCollectedParadeCards().size()== noCardInScoringZone){
                    newWinnerList.add(p2);
                }
            }
            winnerList = newWinnerList;
        }
        return winnerList;
    }

}
