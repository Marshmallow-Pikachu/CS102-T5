package app.utilities;

import app.entity.*;
import app.game.Game;
import app.resource.*;
import java.io.*;
import java.util.*;

public class Printer {
    // Declaring the color (Red, Blue, Purple, Green, Black, Yellow) and Reset codes
    public static final String ANSI_RESET = "\u001B[0m"; 
    public static final String ANSI_RED    = "\u001B[31m";
    public static final String ANSI_BLUE   = "\u001B[36m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_BRIGHT_GREEN  = "\u001B[92m";
    public static final String ANSI_BLACK  = "\u001B[30m";
    public static final String ANSI_YELLOW = "\u001B[33m";

    public static void displayGameState(Game game) {
        Parade parade = game.getParade();
        ArrayList<Player> players = game.getPlayers();
        // print out the current game state
        
        System.out.println("----------------------------------------------------------");
        for (Player p : players) {
            System.out.printf("%s Scoring Zone%n%n", p.getName());
            printCollectedCards(p.getCollectedParadeCards());
            System.out.println("----------------------------------------------------------");
        }

        System.out.println("============ Parade ============");
        printRenderedCards(parade.getParadeCards());
    }


    public static void printCollectedCards(ArrayList<Card> cards){
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
        printCollectionZone(redCards, blueCards, purpleCards, greenCards, blackCards, yellowCards);
    }

    // Player will input a number from 1 to 5, representing the 5 cards in his hand
    // Player input is validated here by my try catch, not sure if there is a need for validateInput.java

    public static Card promptPlayerForCardToPlay(ArrayList<Card> myHand) { //should be left non-static since this 
        Scanner sc = new Scanner(System.in);
        int cardSelectedIndex = -1; 
        String input = null;
        while (true) {
            try {
                System.out.println("    (1)         (2)         (3)         (4)         (5)    "); // To number the cards the player can play
                System.out.print("Enter a number between 1 to 5 to select a card: ");    
                input = sc.nextLine();
                cardSelectedIndex = Integer.parseInt(input) - 1; // the -1 is because number between 1 to 5. Arraylist is 0 indexed
                if (cardSelectedIndex < 0 || cardSelectedIndex > 4) {
                    throw new IllegalArgumentException(); // catch below
                }
                break; //if didnt throw any exception, break
            } catch (Exception e) {
                System.out.println(String.format("%s is not a valid input", input));
            } 
        }
        return myHand.get(cardSelectedIndex);
    }

    // compile command: javac -d out -cp "src" src/app/utilities/DisplayPlayerMenu.java
    // public static void main(String[] args) {
    //     // run java app.utilities.DisplayPlayerMenu to test this after compiling
    //     DisplayPlayerMenu menu = new DisplayPlayerMenu();
    //     menu.promptPlayerForCardToPlay();
    // }


    // Print static logo for our game
    public static void printLogo() {
        try (Scanner sc = new Scanner(new File("./image/logo.txt"))) {
            System.out.print(ANSI_PURPLE);
            while (sc.hasNextLine()) {
                System.out.println(sc.nextLine());
            }
            System.out.println(ANSI_RESET);
        } catch (FileNotFoundException e) {
            System.out.println("Seems like the image folder is missing...");
        }
    }

    // Translate the card into [filename, color]
    public static String[] translateCard(Card c) {
        String[] details = new String[2];

        switch (c.getColour()){
            case "Red":
                details[0] = "hatter.txt";
                details[1] = ANSI_RED;
                break;
            case "Blue":
                details[0] = "alice.txt";
                details[1] = ANSI_BLUE;
                break;
            case "Purple":
                details[0] = "cat.txt";
                details[1] = ANSI_PURPLE;
                break;
            case "Green":
                details[0] = "egg.txt";
                details[1] = ANSI_BRIGHT_GREEN;
                break;
            case "Black":
                details[0] = "rabbit.txt";
                details[1] = ANSI_BLACK;
                break;
            case "Yellow":
                details[0] = "dodo.txt";
                details[1] = ANSI_YELLOW;
                break;
        }
        return details;
    }

    // Convert a card into an ArrayList of Strings to print out
    public static ArrayList<String> renderCard(Card c) {
        // Get the image and the colour of the card
        String[] details = translateCard(c);

        // Initialise the ArrayList to store the card render
        ArrayList<String> cardRender = new ArrayList<>(7);

        
        try (Scanner sc = new Scanner(new File("./image/" + details[0]))) {
            while (sc.hasNext()) {
                String line = sc.nextLine();
                // To add the number on the card, replace % with the value
                if (c.getValue() == 10) {
                    line = line.replace("%", "T");
                } else {
                    line = line.replace("%", Integer.toString(c.getValue()));
                }
                // For alice white pinefore specifically
                line = line.replace("┼─┼", "\u001B[0m┼─┼\u001B[36m");
                line = line.replace("└─┘", "\u001B[0m└─┘\u001B[36m");

                cardRender.add(details[1]+line+ANSI_RESET);
            }
        } catch (FileNotFoundException e) {
            System.out.printf("Missing %s...%n", details[0]);
        }

        return cardRender;
    }

    public static void printRenderedCards(ArrayList<Card> cards) {
        if (cards.isEmpty()) {
            return;
        }
        // We print 10 cards per line for better display
        int count = 0;
        ArrayList<String> printList = generatePrintList();

        for (Card c : cards) {
            // Check if we had hit 10 cards -> if so, flush out the printlist
            if (count == 10) {
                // flush out
                for (int j=0; j < 7; j++) {
                    System.out.println(printList.get(j));
                }

                // Refresh the list
                printList = generatePrintList();
            }

            // Get the lines of the card
            ArrayList<String> renderedCard = renderCard(c);
                
            // Add each line of the card to the printList
            for (int j=0; j < 7; j++) {
                printList.set(j, printList.get(j) + renderedCard.get(j) + " ");
            }

            // increment count by 1
            count += 1;
        }
        // Print out the last few cards
        for (int j=0; j < 7; j++) {
            System.out.println(printList.get(j));
        }
    }

    // This method assumes that it is fed an ArrayList of the same coloured cards
    // But it is still able to print a stack of diff coloured cards
    public static ArrayList<String> renderStackedCards(ArrayList<Card> cards) {
        if (cards.isEmpty()) {
            return null;
        }
        // Create the first card
        Card first = cards.get(0);
        ArrayList<String> renderList = renderCard(first);

        // Create the subsequent cards
        for (int i = 1; i < cards.size(); i++) {
            try(Scanner sc = new Scanner(new File("./image/stacked.txt"))) {
                    Card c = cards.get(i);
                    String[] details =  translateCard(c);
                    int lineNo = 0;
                    while (sc.hasNext()) {
                        String raw = sc.nextLine();
                        String line = raw.replace("%", Integer.toString(c.getValue()));
                        line = line.replace("10", "T");
                        renderList.set(lineNo, renderList.get(lineNo) + details[1] + line + ANSI_RESET);
                        lineNo++;
                    }
            } catch (FileNotFoundException e) {
                System.out.println("Seems like stacked is missing...");
            }
        }
        return renderList;
    }

    // internal helper function to initialise the printList
    private static ArrayList<String> generatePrintList() {
        ArrayList<String> printList = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            printList.add("");
        }

        return printList;
    }

    private static void addToPrintList(ArrayList<String> printList, ArrayList<String> newList) {
        for (int i = 0; i < 7; i++) {
            printList.set(i, printList.get(i) + "   " + newList.get(i));
        }
    }

    private static void outputPrintList(ArrayList<String> printList) {
        for (String line : printList) {
            System.out.println(line);
        }
    }

    public static void printCollectionZone(ArrayList<Card> redCards, ArrayList<Card> blueCards, ArrayList<Card> purpleCards, 
    ArrayList<Card> greenCards, ArrayList<Card> blackCards, ArrayList<Card> yellowCards) {
        // Get all the rendered versions of each color
        ArrayList<String> redList = renderStackedCards(redCards);
        ArrayList<String> blueList = renderStackedCards(blueCards);
        ArrayList<String> purpleList = renderStackedCards(purpleCards);
        ArrayList<String> greenList = renderStackedCards(greenCards);
        ArrayList<String> blackList = renderStackedCards(blackCards);
        ArrayList<String> yellowList = renderStackedCards(yellowCards);
        
        // initialise the printList
        ArrayList<String> printList = generatePrintList();


        // To keep track of whether we want to print a second row of cards
        boolean secondRow = false;

        if (redList != null) {
            addToPrintList(printList, redList);
            secondRow = true;
        }

        if (blueList != null) {
            if (secondRow) {
                addToPrintList(printList, blueList);
                outputPrintList(printList);
                printList = generatePrintList();
                secondRow = false;
            } else {
                addToPrintList(printList, blueList);
                secondRow = true;
            }
        }

        if (purpleList != null) {
            if (secondRow) {
                addToPrintList(printList, purpleList);
                outputPrintList(printList);
                printList = generatePrintList();
                secondRow = false;
            } else {
                addToPrintList(printList, purpleList);
                secondRow = true;
            }
        }

        if (greenList != null) {
            if (secondRow) {
                addToPrintList(printList, greenList);
                outputPrintList(printList);
                printList = generatePrintList();
                secondRow = false;
            } else {
                addToPrintList(printList, greenList);
                secondRow = true;
            }
        }

        if (blackList != null) {
            if (secondRow) {
                addToPrintList(printList, blackList);
                outputPrintList(printList);
                printList = generatePrintList();
                secondRow = false;
            } else {
                addToPrintList(printList, blackList);
                secondRow = true;
            }
        }

        if (yellowList != null) {
            if (secondRow) {
                addToPrintList(printList, yellowList);
                outputPrintList(printList);
                printList = generatePrintList();
                secondRow = false;
            } else {
                addToPrintList(printList, yellowList);
                secondRow = true;
            }
        }

        // To clear the last card
        if (secondRow) {
            outputPrintList(printList);
        }
    }
}

