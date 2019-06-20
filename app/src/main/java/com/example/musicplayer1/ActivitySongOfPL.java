package com.example.musicplayer1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

public class ActivitySongOfPL extends AppCompatActivity {
    public static ActivitySongOfPL instance;
    Playlist playlistNow;
    TextView txtNamePL, txtCountSong;
    ListView listViewSong;
    SongAdapterInPL adapter;
    ImageButton btnExit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_of_pl);
        instance = this;
        Intent intent = getIntent();
        playlistNow = (Playlist) intent.getSerializableExtra("PLAYLIST");
        int posPL = intent.getIntExtra("POSPL",0);
        anhXa();
        txtNamePL.setText(playlistNow.getName());
        //txtCountSong.setText(playlistNow.getListSong().size());
        adapter = new SongAdapterInPL(ActivitySongOfPL.this,R.layout.song_row,playlistNow.getListSong(),posPL);
        listViewSong.setAdapter(adapter);
        btnExit = (ImageButton) findViewById(R.id.buttonExitActiPL);
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                //exit();
            }
        });
    }

    public void anhXa(){
        txtNamePL = (TextView) findViewById(R.id.textViewNameActiPL);
        txtCountSong = (TextView) findViewById(R.id.textViewSongCountActiPL);
        listViewSong = (ListView) findViewById(R.id.listViewSongActiPL);
        btnExit = (ImageButton) findViewById(R.id.buttonExitActiPL);
    }

    public void exit(){
        onPause();
        onDestroy();
    }
}
