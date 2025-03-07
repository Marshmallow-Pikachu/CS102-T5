package app;

import java.util.*;

import app.entity.BotPlayer;
import app.entity.Player;

// import app.game.*;
// import app.entity.*;
// import app.resource.*;
// import app.utilities.*;


public class App {
    // Helper function to check whether the user would like to start the game or stop playing
    public static boolean startGame(Scanner sc) {
        // Prompt the user to start the game
        System.out.print("Start a new game? (Y/N): ");
        String start = sc.nextLine();

        // If the user enters n or N, stop asking the user
        while (!start.toUpperCase().equals("N")) {
            // If the user doesn't enter y or Y, that means that the inputs are invalid
            // and we should we reprompt the user for another input
            if (!start.toUpperCase().equals("Y")) {
                System.out.println("\nInvalid command. Try again please :)\n");
                System.out.print("Start a new game? (Y/N): ");
                start = sc.nextLine();
                continue;
            }

            // if the user enters y or Y, the game will start
            return true;
        }

        // If the user enters n or N, the game will stop
        return false;
    }


    public static void main(String[] args) {
        // Initialise Scanner to read inputs
        Scanner sc = new Scanner(System.in);

        // Starting message of the program
        System.out.println("Welcome to Parade!\n");

        // This is the main game starting loop, everything inside is to 
        while (startGame(sc)) {
            System.out.println("Play game");
        }

        // Exiting message once the user is done playing
        System.out.println("\n Bye bye! Hope you had fun playing Parade! See you next time!\n");

        

    }
}
