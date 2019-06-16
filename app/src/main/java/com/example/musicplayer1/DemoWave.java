package com.example.musicplayer1;

import android.Manifest;
import android.content.ContentUris;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.gauravk.audiovisualizer.visualizer.BarVisualizer;
import com.gauravk.audiovisualizer.visualizer.CircleLineVisualizer;
import com.gauravk.audiovisualizer.visualizer.WaveVisualizer;

import java.io.IOException;

public class DemoWave extends AppCompatActivity {
    BarVisualizer barVisual;
    CircleLineVisualizer circleVisual;
    WaveVisualizer waveVisualizer;
    int checkWave=1;
    int sessionID=-1;
    Song s ;
    MediaPlayer media;
    private static final int PERM_REQ_CODE = 23;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_wave);

        barVisual = (BarVisualizer) findViewById(R.id.barDemo);
        circleVisual = (CircleLineVisualizer) findViewById(R.id.circleDemo);
        //waveVisualizer = (WaveVisualizer) findViewById(R.id.waveDemo);
        //waveVisualizer.setVisibility(View.INVISIBLE);
        //sessionID = MusicService.mediaServiceSessionID;
        requestAudioPermission();
        barVisual.release();
        circleVisual.release();
        //waveVisualizer.release();
        media = new MediaPlayer();
        media.setAudioStreamType(AudioManager.STREAM_MUSIC);
        media.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        s = MainActivity.instance.listSongFull.get(5);
        media.reset();
        long id = s.getId();
        Uri songUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id);
        try {
            media.setDataSource(getApplicationContext(), songUri);
            media.prepare();
        } catch (IOException e) {
            Log.e("MusicPlayService", "I/O error");
            media.reset(); // Reset again to idle state
        } catch (IllegalArgumentException e) {
            Log.e("MusicPlayService", "Argument error");
            media.reset(); // Reset again to idle state
        }
        media.start();
        sessionID = media.getAudioSessionId();
        if(sessionID!=-1){
            if(checkWave==1) {
                //barVisual.setAudioSessionId(sessionID);
                circleVisual.setAudioSessionId(sessionID);
            }
            else
               waveVisualizer.setAudioSessionId(sessionID);
        }

        /*
        barVisual.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                barVisual.release();
                if(sessionID!=-1)
                    waveVisualizer.setAudioSessionId(sessionID);
                barVisual.setVisibility(View.INVISIBLE);
                waveVisualizer.setVisibility(View.VISIBLE);
                checkWave=2;
                return false;
            }
        });

        waveVisualizer.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                waveVisualizer.release();
                if(sessionID!=-1)
                    barVisual.setAudioSessionId(sessionID);
                waveVisualizer.setVisibility(View.INVISIBLE);
                barVisual.setVisibility(View.VISIBLE);
                checkWave=1;
                return false;
            }
        });
        */

    }

    private void requestAudioPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, PERM_REQ_CODE);
    }
}
