package com.example.musicplayer1;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class ActivityPlay extends AppCompatActivity {
    ImageButton btnNextUIP, btnPrevUIP, btnPlayUIP, btnRepeatUIP, btnRandomUIP,
            btnExitUIP, btnWaveUIP, btnLyricUIP, btnQueueUIP, btnSettingUIP;
    SeekBar sbSongUIP;
    TextView txtTitleUIP, txtArtistUIP, txtTimeNowUIP, txtTimeTotalUIP;
    ImageView imgCoverUIP;
    LinearLayout spaceCover;
    public static ActivityPlay instance;
    //SquareLinearLayout squareLayoutCover;
    ArrayList<Song> listSongTemp;
    ArrayList<Song> listSongM;
    int posSong=0;
    int posList=-1;
    int ktRepeat=1;
    int ktRandom=1;
    int timeNow=0;
    int timeMax=1000;
    Song songNow;
    boolean ktPlay=false;
    private Intent musicIntent;
    private ServiceConnection serviceConnection;
    private MusicService musicService;
    Handler handlerUIP;
    Runnable runnableUIP=null;
    SimpleDateFormat timeSong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        instance=this;
        anhXa();
        timeSong = new SimpleDateFormat("mm:ss");
        listSongTemp = new ArrayList<>();
        listSongM = MainActivity.instance.listSongTemp;
        for(Song i:listSongM)
            listSongTemp.add(i);
        handlerUIP = new Handler();
        musicIntent = new Intent(this,MusicService.class);
        ktPlay = MusicService.isPlay;
        if(serviceConnection==null){
            serviceConnection = new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName name, IBinder service) {
                    MusicService.ServiceBinder musicServiceBinder = (MusicService.ServiceBinder) service;
                    musicService = musicServiceBinder.getService();
                }

                @Override
                public void onServiceDisconnected(ComponentName name) {

                }
            };
            bindService(musicIntent,serviceConnection, Context.BIND_AUTO_CREATE);
        }
        btnPlayUIP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pauseUIP();
            }
        });

        setRepeat();

        setRandom();

        btnNextUIP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextSong();
            }
        });

        btnPrevUIP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prevSongUIP();
            }
        });

        setUIPlay();

        setSeekbar();

        btnLyricUIP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(ActivityPlay.this, "repeat: "+ktRepeat+" - random: "+ktRandom,Toast.LENGTH_SHORT).show();
                musicService.stopFG();
            }
        });

        btnExitUIP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnQueueUIP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityPlay.this, QueueActivity.class);
                startActivity(intent);
            }
        });


    }

    public void hello(){
        Toast.makeText(ActivityPlay.this, "Hello dmm",Toast.LENGTH_SHORT).show();
    }

    public void pauseUIP(){
        if(ktPlay){
            btnPlayUIP.setImageResource(R.drawable.ic_play2);
        }else{
            btnPlayUIP.setImageResource(R.drawable.ic_pause2);
        }
        musicService.pause();
        ktPlay=MusicService.isPlay;
        MainActivity.instance.setImageButtonPlay();
        setTimeNow();
        Log.i("Demo", "pauseUIP: "+MusicService.isPlay+" - "+ktPlay);
    }

    public void removeSongInListSongTemp(int n){
        Toast.makeText(ActivityPlay.this, "song remove: "+n+" - posnow: "+posSong,Toast.LENGTH_SHORT).show();
        listSongTemp.remove(n);
        if(posSong>n){
            posSong--;
        }else if(posSong==n){
            Toast.makeText(ActivityPlay.this,"pos remove: "+n+" - list size: "+listSongTemp.size(),Toast.LENGTH_SHORT).show();
            if(n==listSongTemp.size()){
                posSong=0;
                callPlayAtFromUIP();
            }else{
                callPlayAtFromUIP();
            }
        }
        //listSongTemp.remove(n);

    }

    public void swapSongInListSongTemp(int a, int b){
        Collections.swap(listSongTemp,a,b);
        posSong=b;
    }

    public void playAtFromUIP(){
        Song songTemp = listSongTemp.get(posSong);
        musicService.playSong(songTemp);
    }

    public void callPlayAtFromUIP(){
        handlerUIP.removeCallbacks(runnableUIP);
        Song songTemp = listSongTemp.get(posSong);
        musicService.playSong(songTemp);
        MusicService.posSongNow = posSong;
        MainActivity.instance.setLayoutSongPlayMain(listSongTemp.get(posSong));
        setUIPlay();
    }

    public void nextSong(){
        handlerUIP.removeCallbacks(runnableUIP);
        if(ktRepeat==2 || ktRepeat == 1){
            if(ktRandom ==2){
                Random rd = new Random();
                int d = rd.nextInt(listSongTemp.size()-1);
                posSong=d;
            }else{
                if(posSong ==(listSongTemp.size()-1))
                    posSong = 0;
                else posSong++;
            }
        }
        MusicService.posSongNow = posSong;
        MainActivity.instance.setLayoutSongPlayMain(listSongTemp.get(posSong));
        playAtFromUIP();
        //ktPlay=true;
        setUIPlay();
    }

    public void prevSongUIP(){
        handlerUIP.removeCallbacks(runnableUIP);
        if(posSong==0)
            posSong=listSongTemp.size()-1;
        else posSong--;
        MusicService.posSongNow = posSong;
        MainActivity.instance.setLayoutSongPlayMain(listSongTemp.get(posSong));
        playAtFromUIP();
        ktPlay=true;
        setUIPlay();
    }

    public void setSeekbar(){
        sbSongUIP.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int n = sbSongUIP.getProgress();
                musicService.seekTo(n*1000);
                setSbAndTimeNow(n);
            }
        });
    }

    public void setRepeat(){
        btnRepeatUIP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ktRepeat==3) {
                    ktRepeat = 1;
                    MusicService.isRepeat = 1;
                }
                else {
                    ktRepeat++;
                    MusicService.isRepeat = ktRepeat;
                }
                switch (ktRepeat){
                    case 1:
                        btnRepeatUIP.setImageResource(R.drawable.ic_non_repeat);
                        break;
                    case 2:
                        btnRepeatUIP.setImageResource(R.drawable.ic_repeat2);
                        break;
                    case 3:
                        btnRepeatUIP.setImageResource(R.drawable.ic_repeat_one_2);
                        break;
                }
            }
        });
    }

    public void setRandom(){
        btnRandomUIP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ktRandom==1){
                    ktRandom=2;
                    MusicService.isRandom=2;
                }else{
                    ktRandom=1;
                    MusicService.isRandom=1;
                }
                if(ktRandom==1){
                    btnRandomUIP.setImageResource(R.drawable.ic_non_random);

                }else{
                    btnRandomUIP.setImageResource(R.drawable.ic_random2);

                }
            }
        });
    }

    public void checkRandom(){
        if(ktRandom==1){
            btnRandomUIP.setImageResource(R.drawable.ic_non_random);
        }else
            btnRandomUIP.setImageResource(R.drawable.ic_random2);
    }

    public void checkRepeat(){
        switch (ktRepeat){
            case 1:
                btnRepeatUIP.setImageResource(R.drawable.ic_non_repeat);
                break;
            case 2:
                btnRepeatUIP.setImageResource(R.drawable.ic_repeat2);
                break;
            case 3:
                btnRepeatUIP.setImageResource(R.drawable.ic_repeat_one_2);
                break;
        }
    }

    public void setTimeNow(){
        if(musicService!=null)
            timeNow = musicService.getCurrentTimeSong()/1000;
        //timeNow++;
        Log.i("Demo", "KTPlay: "+ktPlay+ " - setTimeNow1: "+timeNow);

        if(ktPlay) {
            sbSongUIP.setProgress(timeNow);
            txtTimeNowUIP.setText(timeSong.format(timeNow * 1000));
        }
        if(timeNow>=timeMax-1){
            Toast.makeText(ActivityPlay.this,"Finish Song",Toast.LENGTH_SHORT).show();
            //nextSong();
        }
        else if(ktPlay) {
            runnableUIP = new Runnable() {
                @Override
                public void run() {
                    setTimeNow();
                }
            };
            handlerUIP.postDelayed(runnableUIP, 200);
        }
    }

    public void setUIPlay(){
        posSong = MusicService.posSongNow;
        posList = MusicService.posListNow;
        ktPlay = MusicService.isPlay;
        //ktPlay=true;
        ktRandom = MusicService.isRandom;
        ktRepeat = MusicService.isRepeat;
        checkRandom();
        checkRepeat();
        if(ktPlay)
            btnPlayUIP.setImageResource(R.drawable.ic_pause2);
        else
            btnPlayUIP.setImageResource(R.drawable.ic_play2);
        songNow = listSongTemp.get(posSong);
        txtTitleUIP.setText(songNow.getTitle());
        txtArtistUIP.setText(songNow.getArctis());
        imgCoverUIP.setImageBitmap(songNow.getCover());
        timeMax = songNow.getTimeTotal()/1000;
        txtTimeTotalUIP.setText(timeSong.format(timeMax*1000));
        sbSongUIP.setMax(timeMax);
        timeNow=0;
        setTimeNow();
    }

    public void setSbAndTimeNow(int n){
        sbSongUIP.setProgress(n);
        txtTimeNowUIP.setText(timeSong.format(n*1000));
    }

    public void anhXa(){
        btnNextUIP = (ImageButton) findViewById(R.id.buttonNextUIPlay);
        btnPlayUIP = (ImageButton) findViewById(R.id.buttonPlayUIPlay);
        btnPrevUIP = (ImageButton) findViewById(R.id.buttonPrevUIPlay);
        btnRepeatUIP = (ImageButton) findViewById(R.id.buttonRepeatUIPlay);
        btnRandomUIP = (ImageButton) findViewById(R.id.buttonRandomUIPlay);
        btnExitUIP = (ImageButton) findViewById(R.id.buttonExitUIPlay);
        btnWaveUIP = (ImageButton) findViewById(R.id.buttonWaveUIPlay);
        btnLyricUIP = (ImageButton) findViewById(R.id.buttonLyricUIPlay);
        btnQueueUIP = (ImageButton) findViewById(R.id.buttonListQueueUIPlay);
        btnSettingUIP = (ImageButton) findViewById(R.id.buttonSettingUIPlay);
        txtTitleUIP = (TextView) findViewById(R.id.textViewSongTitleUIPlay);
        txtArtistUIP = (TextView) findViewById(R.id.textViewSongArtistUIPlay);
        imgCoverUIP = (ImageView) findViewById(R.id.imageViewCoverUIPlay);
        sbSongUIP = (SeekBar) findViewById(R.id.seebarSongUIPlay);
        txtTimeNowUIP = (TextView) findViewById(R.id.textViewTimeSongNow);
        txtTimeTotalUIP = (TextView) findViewById(R.id.textViewTimeSongTotal);
        spaceCover = (LinearLayout) findViewById(R.id.lineSpaceCover);
        //squareLayoutCover = (LinearLayout) findViewById(R.id.layoutSquareCover);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        handlerUIP.removeCallbacks(runnableUIP);
    }

}
