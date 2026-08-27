package data;

import java.util.ArrayList;
import java.util.List;
import model.Playlist;
import util.FileHandler;

public class PlaylistRepository extends FileHandler<Playlist> {

    private static final String FILE_PATH = "database/playlists.txt";

    private List<Playlist> playlists;

    public PlaylistRepository() {
        this.playlists = new ArrayList<>();
        load(FILE_PATH, playlists);
        if (renumberIds()) {
            save(playlists, FILE_PATH);
        }
    }

    @Override
    public Playlist handleLine(String line) {
        String[] parts = line.split("\\|", -1);
        if (parts.length != 4) {
            return null;
        }
        try {
            int id = Integer.parseInt(parts[0]);
            String name = parts[1];
            String description = parts[2];

            Playlist playlist = new Playlist(id, name, description);
            if (!parts[3].isEmpty()) {
                String[] idTokens = parts[3].split(",");
                for (String idToken : idTokens) {
                    playlist.addSongId(Integer.parseInt(idToken));
                }
            }
            return playlist;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public List<Playlist> getAll() {
        return playlists;
    }

    public Playlist findById(int id) {
        for (Playlist playlist : playlists) {
            if (playlist.getId() == id) {
                return playlist;
            }
        }
        return null;
    }

    public int generateNextId() {
        int maxId = 0;
        for (Playlist playlist : playlists) {
            if (playlist.getId() > maxId) {
                maxId = playlist.getId();
            }
        }
        return maxId + 1;
    }

    public boolean add(Playlist playlist) {
        playlists.add(playlist);
        return save(playlists, FILE_PATH);
    }

    public boolean update(Playlist playlist) {
        for (int i = 0; i < playlists.size(); i++) {
            if (playlists.get(i).getId() == playlist.getId()) {
                playlists.set(i, playlist);
                return save(playlists, FILE_PATH);
            }
        }
        return false;
    }

    public boolean deleteById(int id) {
        Playlist existing = findById(id);
        if (existing == null) {
            return false;
        }
        playlists.remove(existing);
        renumberIds();
        return save(playlists, FILE_PATH);
    }

    private boolean renumberIds() {
        boolean changed = false;
        int nextId = 1;
        for (Playlist playlist : playlists) {
            if (playlist.getId() != nextId) {
                playlist.setId(nextId);
                changed = true;
            }
            nextId++;
        }
        return changed;
    }
}
