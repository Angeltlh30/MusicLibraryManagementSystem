package view;

public class ConsoleTablePrinter {

    public static void printTable(String[] headers, String[][] rows) {
        int[] widths = new int[headers.length];
        for (int c = 0; c < headers.length; c++) {
            widths[c] = AnsiColors.visibleLength(headers[c]);
        }
        for (String[] row : rows) {
            for (int c = 0; c < row.length; c++) {
                String value = row[c] == null ? "" : row[c];
                int length = AnsiColors.visibleLength(value);
                if (length > widths[c]) {
                    widths[c] = length;
                }
            }
        }

        printSeparator(widths);
        printHeaderRow(headers, widths);
        printSeparator(widths);
        for (String[] row : rows) {
            printRow(row, widths);
        }
        printSeparator(widths);
    }

    private static void printSeparator(int[] widths) {
        StringBuilder line = new StringBuilder("+");
        for (int w : widths) {
            for (int i = 0; i < w + 2; i++) {
                line.append('-');
            }
            line.append('+');
        }
        System.out.println(AnsiColors.colorize(line.toString(), AnsiColors.BORDER));
    }

    private static void printHeaderRow(String[] headers, int[] widths) {
        StringBuilder line = new StringBuilder(AnsiColors.colorize("|", AnsiColors.BORDER));
        for (int c = 0; c < headers.length; c++) {
            String value = headers[c];
            line.append(' ').append(AnsiColors.colorize(value, AnsiColors.HEADER));
            for (int i = AnsiColors.visibleLength(value); i < widths[c]; i++) {
                line.append(' ');
            }
            line.append(' ').append(AnsiColors.colorize("|", AnsiColors.BORDER));
        }
        System.out.println(line);
    }

    private static void printRow(String[] cells, int[] widths) {
        StringBuilder line = new StringBuilder(AnsiColors.colorize("|", AnsiColors.BORDER));
        for (int c = 0; c < cells.length; c++) {
            String value = cells[c] == null ? "" : cells[c];
            line.append(' ').append(value);
            for (int i = AnsiColors.visibleLength(value); i < widths[c]; i++) {
                line.append(' ');
            }
            line.append(' ').append(AnsiColors.colorize("|", AnsiColors.BORDER));
        }
        System.out.println(line);
    }
}
