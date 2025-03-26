import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class DisplayCard {
    public static void main(String[] args) {
        String[] files = new String[]{"alice.txt", "hatter.txt", "rabbit.txt", "egg.txt", "dodo.txt", "cat.txt"};
        String[] color = new String[]{"\u001B[36m", "\u001B[31m", "\u001B[90m", "\u001B[92m", "\u001B[33m", "\u001B[35m"};

        // Display every card by itself
        for (int i = 0; i< 6; i++) {
            try (Scanner sc = new Scanner(new File(files[i]))) {
                
                while (sc.hasNext()) {
                    String raw = sc.nextLine();
                    System.out.print(color[i]);
                    for (int j = 0; j < 10; j++) {
                        String line = raw.replace("%", Integer.toString(j));
                        // Alice specific
                        line = line.replace("┼─┼", "\u001B[0m┼─┼\u001B[36m");
                        line = line.replace("└─┘", "\u001B[0m└─┘\u001B[36m");
                        System.out.print(line + " ");
                    }
                    String line = raw.replace("%", "T");

                    // alice specific
                    line = line.replace("┼─┼", "\u001B[0m┼─┼\u001B[36m");
                    line = line.replace("└─┘", "\u001B[0m└─┘\u001B[36m");
                    
                    System.out.print(line + " ");
                    System.out.println("\u001B[0m");
                }
                
                while (sc.hasNext()) {
                    String raw = sc.nextLine();
                    System.out.print(color[i]);
                    for (int j = 0; j < 10; j++) {
                        String line = raw.replace("%", Integer.toString(j));
                        // Alice specific
                        line = line.replace("┼─┼", "\u001B[0m┼─┼\u001B[36m");
                        line = line.replace("└─┘", "\u001B[0m└─┘\u001B[36m");
                        System.out.print(line + " ");
                    }
                    String line = raw.replace("%", "T");

                    // alice specific
                    line = line.replace("┼─┼", "\u001B[0m┼─┼\u001B[36m");
                    line = line.replace("└─┘", "\u001B[0m└─┘\u001B[36m");
                    
                    System.out.print(line + " ");
                    System.out.println("\u001B[0m");
                }
            } catch (FileNotFoundException e) {
                System.out.printf("Seems like %s is missing...", files[i]);
            }
        }

        // Display cards that are stacked
        for (int i = 0; i< 6; i++) {
            ArrayList<String> printList = new ArrayList<>();
            for (int j = 0; j<7; j++) {
                printList.add("");
            }
            // print the first card
            try (Scanner sc = new Scanner(new File(files[i]))) {
                int lineNo = 0;
                while (sc.hasNext()) {
                        String raw = sc.nextLine();
                        String line = raw.replace("%", "0");
                        // Alice specific
                        line = line.replace("┼─┼", "\u001B[0m┼─┼\u001B[36m");
                        line = line.replace("└─┘", "\u001B[0m└─┘\u001B[36m");
                        if (lineNo < 7) {
                            printList.set(lineNo, color[i] + line);
                            lineNo++;
                        }
                }
            } catch (FileNotFoundException e) {
                System.out.printf("Seems like %s is missing...", files[i]);
            }

            // print the rest of the stack
            try (Scanner sc = new Scanner(new File("stacked.txt"))) {
                int lineNo = 0;
                while (sc.hasNext()) {
                        String raw = sc.nextLine();
                        for (int j = 1; j < 11; j++) {
                            int val = j % 11;
                            String line = raw.replace("%", Integer.toString(val));
                            line = line.replace("10", "T");
                            // Alice specific
                            line = line.replace("┼─┼", "\u001B[0m┼─┼\u001B[36m");
                            line = line.replace("└─┘", "\u001B[0m└─┘\u001B[36m");
                            printList.set(lineNo, printList.get(lineNo)+line);
                        }
                        lineNo++;
                        
                }
            } catch (FileNotFoundException e) {
                System.out.printf("Seems like stacked is missing...");
            }

            for (String line : printList) {
                System.out.println(line + "\u001B[0m");
            }
        }
    }
}