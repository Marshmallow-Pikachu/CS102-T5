package app.utilities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import app.entity.BotPlayer;
import app.entity.Player;
import app.resource.Deck;

public class AppUtils {
        // Helper function to generate an arraylist of bot players to add to the game
    /**
     * Creates an ArrayList of Bot players to add into the game.
     * @param NoOfPlayers The number of Bots you would want to add into the game (number between 0 to 6)
     * @param deck The common deck instance
     * @return An Arraylist of bots to be added into the game
     */
    public static ArrayList<Player> generateBotPlayers(int NoOfPlayers, Deck deck) {
        ArrayList<Player> botPlayers = new ArrayList<>();

        // Default list of bot names to randomise
        List<String> rawParadeBotNames = List.of("Alice", "Mad Hatter", "White Rabbit", 
        "Humpty Dumpty", "Cheshire Cat", "Dodo Bird");
        List<String> paradeBotNames = new ArrayList<>(rawParadeBotNames);
        Collections.shuffle(paradeBotNames);

        // Add the bots to the ArrayList and return them
        for (int i = 0; i < NoOfPlayers; i++) {
            botPlayers.add(new BotPlayer(deck, paradeBotNames.get(i)));
        };
        return botPlayers;
    }

}
