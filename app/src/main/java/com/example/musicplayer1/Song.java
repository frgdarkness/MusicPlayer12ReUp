package com.example.musicplayer1;

import android.graphics.Bitmap;

import java.io.Serializable;

public class Song implements Serializable {
    private Long id;
    private String title;
    private String data;
    private float size;
    private float duration;
    private String arctis;
    private String album;
    private Long idAlbum;
    private byte[] bmImage;
    private Bitmap cover;
    private int timeTotal;

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public Long getIdAlbum() {
        return idAlbum;
    }

    public void setIdAlbum(Long idAlbum) {
        this.idAlbum = idAlbum;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public float getSize() {
        return size;
    }

    public void setSize(float size) {
        this.size = size;
    }

    public float getDuration() {
        return duration;
    }

    public void setDuration(float duration) {
        this.duration = duration;
    }

    public String getArctis() {
        return arctis;
    }

    public void setArctis(String arctis) {
        this.arctis = arctis;
    }

    public byte[] getByteImage() {

        return bmImage;
    }

    public void setByteImage(byte[] bmImage) {
        this.bmImage = bmImage;
    }

    public Bitmap getCover() {
        return cover;
    }

    public void setCover(Bitmap cover) {
        this.cover = cover;
    }

    public int getTimeTotal() {
        return timeTotal;
    }

    public void setTimeTotal(int timeTotal) {
        this.timeTotal = timeTotal;
    }
}
