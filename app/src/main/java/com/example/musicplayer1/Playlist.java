package com.example.musicplayer1;

import java.io.Serializable;
import java.util.ArrayList;

public class Playlist implements Serializable {
    private String name;
    private ArrayList<Song> listSong;

    public Playlist(String name, ArrayList<Song> listSong) {
        this.name = name;
        this.listSong = listSong;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Song> getListSong() {
        return listSong;
    }

    public void setListSong(ArrayList<Song> listSong) {
        this.listSong = listSong;
    }

    public void addSongToList(Song song){
        this.listSong.add(song);
    }
}
