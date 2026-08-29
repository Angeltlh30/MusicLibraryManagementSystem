package view;

import java.util.ArrayList;
import java.util.List;

public class MenuBoxRenderer {

    public static int computeContentWidth(String[][] items) {
        int numberWidth = 0;
        for (String[] item : items) {
            numberWidth = Math.max(numberWidth, item[0].length());
        }
        int contentWidth = 0;
        for (String[] item : items) {
            int lineLength = numberWidth + 2 + item[1].length();
            contentWidth = Math.max(contentWidth, lineLength);
        }
        return contentWidth;
    }

    public static List<String> buildBox(String title, String[][] items, int minContentWidth, int minRows) {
        int numberWidth = 0;
        for (String[] item : items) {
            numberWidth = Math.max(numberWidth, item[0].length());
        }

        List<String> contentLines = new ArrayList<>();
        int contentWidth = minContentWidth;
        for (String[] item : items) {
            String line = padLeft(item[0], numberWidth) + ". " + item[1];
            contentLines.add(line);
            contentWidth = Math.max(contentWidth, line.length());
        }
        while (contentLines.size() < minRows) {
            contentLines.add("");
        }

        List<String> lines = new ArrayList<>();
        lines.add(AnsiColors.colorize(title, AnsiColors.LABEL));
        lines.add(AnsiColors.colorize(topBorder(contentWidth), AnsiColors.BORDER));
        for (String content : contentLines) {
            lines.add(AnsiColors.colorize("│", AnsiColors.BORDER) + " " + padRight(content, contentWidth)
                    + " " + AnsiColors.colorize("│", AnsiColors.BORDER));
        }
        lines.add(AnsiColors.colorize(bottomBorder(contentWidth), AnsiColors.BORDER));
        return lines;
    }

    public static void printRow(List<List<String>> boxes, int gap) {
        int height = 0;
        int[] widths = new int[boxes.size()];
        for (int i = 0; i < boxes.size(); i++) {
            height = Math.max(height, boxes.get(i).size());
            for (String line : boxes.get(i)) {
                widths[i] = Math.max(widths[i], AnsiColors.visibleLength(line));
            }
        }

        String gapSpaces = padRight("", gap);
        for (int row = 0; row < height; row++) {
            StringBuilder line = new StringBuilder();
            for (int i = 0; i < boxes.size(); i++) {
                List<String> box = boxes.get(i);
                String cell = row < box.size() ? box.get(row) : "";
                line.append(cell);
                int pad = widths[i] - AnsiColors.visibleLength(cell);
                line.append(padRight("", pad));
                if (i < boxes.size() - 1) {
                    line.append(gapSpaces);
                }
            }
            System.out.println(line);
        }
    }

    public static int outerWidth(int contentWidth) {
        return contentWidth + 4;
    }

    public static String buildTitleBar(String text, int totalWidth) {
        String label = " " + text + " ";
        int fillTotal = totalWidth - label.length();
        if (fillTotal < 0) {
            fillTotal = 0;
        }
        int left = fillTotal / 2;
        int right = fillTotal - left;
        return repeat('=', left) + label + repeat('=', right);
    }

    private static String topBorder(int width) {
        return "┌" + repeat('─', width + 2) + "┐";
    }

    private static String bottomBorder(int width) {
        return "└" + repeat('─', width + 2) + "┘";
    }

    private static String repeat(char c, int count) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < count; i++) {
            builder.append(c);
        }
        return builder.toString();
    }

    private static String padLeft(String text, int width) {
        StringBuilder builder = new StringBuilder();
        for (int i = text.length(); i < width; i++) {
            builder.append(' ');
        }
        return builder.append(text).toString();
    }

    private static String padRight(String text, int width) {
        StringBuilder builder = new StringBuilder(text);
        for (int i = text.length(); i < width; i++) {
            builder.append(' ');
        }
        return builder.toString();
    }
}
