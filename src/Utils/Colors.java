package Utils;

public record Colors() {
    /**
     * ANSI color red
     */
    public final static String RED = "\033[0;31m";
    /**
     * ANSI color green underline
     */
    public final static String GREEN_UNDERLINE = "\033[4;32m";
    /**
     * ANSI color yellow
     */
    public final static String YELLOW  = "\033[0;33m";
    /**
     * ANSI color yellow_underline
     */
    public final static String YELLOW_UNDERLINE = "\033[4;33m";
    /**
     * ANSI reset colors
     */
    public final static String RESET = "\033[0m";  
}
