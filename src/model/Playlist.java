package model;

import structures.CircularDoublyLinkedList;
import util.Persistable;

public class Playlist implements Persistable {
    private int id;
    private String name;
    private String description;
    private CircularDoublyLinkedList<Integer> songIds;

    public Playlist(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.songIds = new CircularDoublyLinkedList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CircularDoublyLinkedList<Integer> getSongIds() {
        return songIds;
    }

    public void addSongId(int songId) {
        songIds.addLast(songId);
    }

    public boolean removeSongId(int songId) {
        return songIds.remove(songId);
    }

    public boolean containsSongId(int songId) {
        return songIds.contains(songId);
    }

    public int getSongCount() {
        return songIds.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Playlist playlist = (Playlist) o;
        return id == playlist.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }

    @Override
    public String toString() {
        return "#" + id + " - " + name + " (" + description + ") [" + getSongCount() + " songs]";
    }

    @Override
    public String toDataString() {
        StringBuilder idsBuilder = new StringBuilder();
        boolean first = true;
        for (Integer songId : songIds) {
            if (!first) {
                idsBuilder.append(',');
            }
            idsBuilder.append(songId);
            first = false;
        }
        return id + "|" + name + "|" + description + "|" + idsBuilder;
    }
}
