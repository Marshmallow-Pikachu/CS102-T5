import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class DisplayCard {
    public static void main(String[] args) {
        String[] files = new String[]{"alice.txt", "hatter.txt", "rabbit.txt", "egg.txt", "dodo.txt", "cat.txt"};
        String[] color = new String[]{"\u001B[34m", "\u001B[31m", "\u001B[30m", "\u001B[92m", "\u001B[33m", "\u001B[35m"};
        for (int i = 0; i< 6; i++) {
            try (Scanner sc = new Scanner(new File(files[i]))) {
                
                while (sc.hasNext()) {
                    String raw = sc.nextLine();
                    System.out.print(color[i]);
                    for (int j = 0; j < 10; j++) {
                        String line = raw.replace("%", Integer.toString(j));
                        System.out.print(line + " ");
                    }
                    String line = raw.replace("%", "T");
                    System.out.print(line + " ");
                    System.out.println("\u001B[0m");
                }
            } catch (FileNotFoundException e) {
                System.out.printf("Seems like %s is missing...", files[i]);
            }
        }
    }
}