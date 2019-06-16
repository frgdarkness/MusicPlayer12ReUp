package com.example.musicplayer1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

public class ActivitySongOfPL extends AppCompatActivity {
    Playlist playlistNow;
    TextView txtNamePL, txtCountSong;
    ListView listViewSong;
    SongAdapter adapter;
    ImageButton btnExit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_of_pl);
        Intent intent = getIntent();
        playlistNow = (Playlist) intent.getSerializableExtra("PLAYLIST");

        anhXa();
        txtNamePL.setText(playlistNow.getName());
        //txtCountSong.setText(playlistNow.getListSong().size());
        adapter = new SongAdapter(ActivitySongOfPL.this,R.layout.song_row,playlistNow.getListSong(),1);
        listViewSong.setAdapter(adapter);
    }

    public void anhXa(){
        txtNamePL = (TextView) findViewById(R.id.textViewNameActiPL);
        txtCountSong = (TextView) findViewById(R.id.textViewSongCountActiPL);
        listViewSong = (ListView) findViewById(R.id.listViewSongActiPL);
        btnExit = (ImageButton) findViewById(R.id.buttonExitActiPL);
    }
}
