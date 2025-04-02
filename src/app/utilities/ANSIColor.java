package app.utilities;

/**
 * Contains the 6 ANSI colors codes for the cards
 */
public class ANSIColor {
    /**
     * Default Constructor
     */
    public ANSIColor() {

    }
    
    /**
     * Reset color to the default terminal color.
     */
    public static final String ANSI_RESET = "\u001B[0m";

    /**
     * ANSI code for red text.
     */
    public static final String ANSI_RED = "\u001B[31m";

    /**
     * ANSI code for blue text.
     */
    public static final String ANSI_BLUE = "\u001B[34m";

    /**
     * ANSI code for purple text.
     */
    public static final String ANSI_PURPLE = "\u001B[35m";

    /**
     * ANSI code for green text.
     */
    public static final String ANSI_BRIGHT_GREEN = "\u001B[92m";

    /**
     * ANSI code for black text.
     */
    public static final String ANSI_BLACK = "\u001B[37m";

    /**
     * ANSI code for yellow text.
     */
    public static final String ANSI_YELLOW = "\u001B[33m";

}
