package com.example.musicplayer1;

public class Lyric {
    private String title;
    private int lyric;

    public Lyric(String title, int lyric) {
        this.title = title;
        this.lyric = lyric;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getLyric() {
        return lyric;
    }

    public void setLyric(int lyric) {
        this.lyric = lyric;
    }
}
