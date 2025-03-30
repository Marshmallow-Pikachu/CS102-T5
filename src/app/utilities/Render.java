package app.utilities;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import app.resource.Card;

// Helper class to convert cards to ArrayList
/**
 * This class contains utility methods for rendering text images our cards and the scoring zone.
 */
public class Render {
    // Translate the card into [filename, color]
    /**
     * Translates card attributes to include text images and ANSI color codes.
     * @param c The card to translate.
     * @return A String [] containing details of the filename for the card image and the corresponding ANSI color code.
     */
    public static String[] translateCard(Card c) {
        String[] details = new String[2];

        switch (c.getColour()){
            case "Red":
                details[0] = "hatter.txt";
                details[1] = ANSIColor.ANSI_RED;
                break;
            case "Blue":
                details[0] = "alice.txt";
                details[1] = ANSIColor.ANSI_BLUE;
                break;
            case "Purple":
                details[0] = "cat.txt";
                details[1] = ANSIColor.ANSI_PURPLE;
                break;
            case "Green":
                details[0] = "egg.txt";
                details[1] = ANSIColor.ANSI_BRIGHT_GREEN;
                break;
            case "Black":
                details[0] = "rabbit.txt";
                details[1] = ANSIColor.ANSI_BLACK;
                break;
            case "Yellow":
                details[0] = "dodo.txt";
                details[1] = ANSIColor.ANSI_YELLOW;
                break;
        }
        return details;
    }

    // Convert a card into an ArrayList<Strings> to print out
    /**
     * Renders a card into a list of strings for display.
     * @param c The card to be rendered.
     * @return An ArrayList of String representing the rendered card.
     */
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
                line = line.replace("┼─┼", "\u001B[0m┼─┼\u001B[34m");
                line = line.replace("└─┘", "\u001B[0m└─┘\u001B[34m");

                cardRender.add(details[1]+line+ANSIColor.ANSI_RESET);
            }
        } catch (FileNotFoundException e) {
            System.out.printf("Missing %s...%n", details[0]);
        }

        return cardRender;
    }

    // Highlight a played Card
    /**
     * Highlights the last played card in the parade for visual clarity.
     * @param c The card to be highlighted.
     * @return An ArrayList of String representing the highlighted card.
     */
    public static ArrayList<String> renderPlayedCard(Card c) {
        ArrayList<String> printList = renderCard(c);

        for (int i = 0; i < 7; i++) {
            switch (i) {
                case 0:
                    printList.set(i, "\\" + printList.get(i) + "/");
                    break;
                case 3:
                    printList.set(i, "-" + printList.get(i) + "-");
                    break;
                case 6:
                    printList.set(i, "/" + printList.get(i) + "\\");
                    break;
                default:
                    printList.set(i, " " + printList.get(i) + " ");
                    break;
            }
        }

        return printList;
    }

    // Convert an ArrayList<Cards> into a stacked row of cards to print out
    /**
     * Renders a stacked representation of multiple cards. This is to keep things neat and prevent console width compatibility issues
     * @param cards The list of cards to render in a stacked format.
     * @return An ArrayList of strings representing the stacked cards.
     */
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
                        renderList.set(lineNo, renderList.get(lineNo) + details[1] + line + ANSIColor.ANSI_RESET);
                        lineNo++;
                    }
            } catch (FileNotFoundException e) {
                System.out.println("Seems like stacked is missing...");
            }
        }

        // To add the spacers at the end of each card stack
        for (int i = 0; i < 7; i++) {
            String spacer = " ".repeat(33 - (cards.size()-1) * 3);
            renderList.set(i, renderList.get(i) + spacer);
        }

        return renderList;
    }

    // Convert an int deckSize into a deck with that number to print out
    /**
     * Renders a deck visual based on the number of cards left.
     * @param deckSize The number of cards left in the deck.
     * @return An ArrayList of strings representing the deck.
     */
    public static ArrayList<String> renderDeck(int deckSize) {
        ArrayList<String> printList = new ArrayList<>();

        // Determine what type of deck image to display
        String deckType = "deck.txt";
        switch (deckSize) {
            case 0:
                deckType = "deckout.txt";
                break;
            case 1:
                deckType = "deck1.txt";
                break;
            case 2:
                deckType = "deck2.txt";
                break;
            default:
                deckType = "deck.txt";
                break;
        }
        try (Scanner sc = new Scanner(new File("./image/" + deckType))) {
            while (sc.hasNext()){
                String raw = sc.nextLine();
                String line = raw.replace("%%", String.format("%2d", deckSize));
                printList.add(line + " ");
            }
        } catch (FileNotFoundException e) {
            System.out.println("Missing the deck.txt file");
        }
        return printList;
    }
    /**
     * Renders the scoring zones, seperated by color.
     * @param cards The list of cards to render.
     * @return An ArrayList of type CardList, each representing a color section in the scoring zone.
     */
    public static ArrayList<CardList> renderScoringZone(ArrayList<Card> cards) {
        
        ArrayList<CardList> colorList = new ArrayList<>();

        // Loop through each color and create a PrintList for each of them
        String[] colors = new String[]{"Red", "Blue", "Purple", "Green", "Black", "Yellow"};
        for (String color : colors) {
            ArrayList<Card> sameColorCards = new ArrayList<>();
            for (Card c : cards) {
                if (c.getColour().equals(color)) {
                    sameColorCards.add(c);
                }
            }
            colorList.add(new CardList(renderStackedCards(sameColorCards)));
        }
        
        return colorList;
    }


}
