package data;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import model.HistoryEntry;
import util.FileHandler;

public class HistoryRepository extends FileHandler<HistoryEntry> {

    private static final String FILE_PATH = "database/history.txt";

    private List<HistoryEntry> history;

    public HistoryRepository() {
        this.history = new ArrayList<>();
        load(FILE_PATH, history);
    }

    @Override
    public HistoryEntry handleLine(String line) {
        String[] parts = line.split("\\|", -1);
        if (parts.length != 2) {
            return null;
        }
        try {
            int songId = Integer.parseInt(parts[0]);
            LocalDateTime playedAt = LocalDateTime.parse(parts[1]);
            return new HistoryEntry(songId, playedAt);
        } catch (NumberFormatException | DateTimeParseException e) {
            return null;
        }
    }

    public List<HistoryEntry> getAll() {
        return history;
    }

    public boolean add(HistoryEntry entry) {
        history.add(entry);
        return save(history, FILE_PATH);
    }
}
