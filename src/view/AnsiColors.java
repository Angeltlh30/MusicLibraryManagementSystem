package view;

import java.util.regex.Pattern;

public class AnsiColors {

    public static final String RESET = "\u001B[0m";
    public static final String BORDER = "\u001B[96m";
    public static final String LABEL = "\u001B[1;96m";
    public static final String HEADER = "\u001B[1;97m";
    public static final String SUCCESS = "\u001B[92m";
    public static final String ERROR = "\u001B[91m";
    public static final String WARNING = "\u001B[93m";
    public static final String ACCENT = "\u001B[95m";
    public static final String DIM = "\u001B[2m";

    private static final Pattern ANSI_CODE = Pattern.compile("\\u001B\\[[0-9;]*m");

    public static String colorize(String text, String color) {
        return color + text + RESET;
    }

    public static int visibleLength(String text) {
        return ANSI_CODE.matcher(text).replaceAll("").length();
    }
}
