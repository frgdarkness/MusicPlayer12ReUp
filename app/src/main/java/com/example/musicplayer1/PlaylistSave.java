package com.example.musicplayer1;

import java.io.Serializable;
import java.util.ArrayList;

public class PlaylistSave implements Serializable {
    private String namePL;
    private ArrayList<Long> listIdSong;

    public PlaylistSave(String namePL, ArrayList<Long> listIdSong) {
        this.namePL = namePL;
        this.listIdSong = listIdSong;
    }

    public String getNamePL() {
        return namePL;
    }

    public void setNamePL(String namePL) {
        this.namePL = namePL;
    }

    public ArrayList<Long> getListIdSong() {
        return listIdSong;
    }

    public void setListIdSong(ArrayList<Long> listIdSong) {
        this.listIdSong = listIdSong;
    }
}
