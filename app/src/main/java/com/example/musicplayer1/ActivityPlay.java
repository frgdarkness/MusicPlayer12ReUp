package com.example.musicplayer1;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
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

import com.gauravk.audiovisualizer.visualizer.BarVisualizer;
import com.gauravk.audiovisualizer.visualizer.CircleLineVisualizer;
import com.gauravk.audiovisualizer.visualizer.WaveVisualizer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class ActivityPlay extends AppCompatActivity {
    private static final int PERM_REQ_CODE = 23;
    ImageButton btnNextUIP, btnPrevUIP, btnPlayUIP, btnRepeatUIP, btnRandomUIP,
            btnExitUIP, btnWaveUIP, btnLyricUIP, btnQueueUIP, btnSettingUIP;
    SeekBar sbSongUIP;
    TextView txtTitleUIP, txtArtistUIP, txtTimeNowUIP, txtTimeTotalUIP, lyricRow1, lyricRow2;
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
    int ktWave=0;
    int ktLyric=0;
    Song songNow;
    MyMedia myMedia;
    //String[] lyricS;
    ArrayList<String> lyricList;
    ArrayList<Integer> timeLyricList;
    boolean haveLyric;
    boolean ktPlay=false;
    private Intent musicIntent;
    private ServiceConnection serviceConnection;
    private MusicService musicService;
    Handler handlerUIP;
    Runnable runnableUIP=null;
    SimpleDateFormat timeSong;
    BarVisualizer barVisual;
    WaveVisualizer waveVisualizer;
    CircleLineVisualizer circleVisual;
    int sessionID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        instance=this;
        myMedia = new MyMedia(ActivityPlay.this);
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

        setLyricVisibility(false);

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
        //barVisual.release();
        //waveVisualizer.setVisibility(View.INVISIBLE);
        //barVisual.setVisibility(View.INVISIBLE);
        circleVisual.release();
        circleVisual.setVisibility(View.INVISIBLE);
        sessionID = MusicService.mediaServiceSessionID;
        Log.i("Demo", "onCreate: SessionID: "+sessionID);


        btnLyricUIP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(ActivityPlay.this, "repeat: "+ktRepeat+" - random: "+ktRandom,Toast.LENGTH_SHORT).show();
                //musicService.stopFG();
                //imgCoverUIP.setBack
                if(songNow.getLyric()==R.string.no_lyric)
                    Toast.makeText(ActivityPlay.this,"No lyric",Toast.LENGTH_SHORT).show();
                if(ktLyric==1)
                    ktLyric=0;
                else
                    ktLyric=1;
                if(ktWave==1){
                    ktLyric=1;
                }
                if(ktLyric==1){
                    imgCoverUIP.setColorFilter(R.color.colorTintCover1);
                    circleVisual.setVisibility(View.INVISIBLE);
                    setLyricVisibility(true);
                }else{
                    imgCoverUIP.setColorFilter(null);
                    setLyricVisibility(false);
                }

            }
        });

        btnWaveUIP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(ActivityPlay.this, "lyric: "+timeLyricList.get(5)+" - "+lyricList.get(5),Toast.LENGTH_SHORT).show();
                //Toast.makeText(ActivityPlay.this, "Session: "+sessionID+" - service: "+MusicService.mediaServiceSessionID,Toast.LENGTH_SHORT).show();
                //if(sessionID!=-1)
                //    barVisual.setAudioSessionId(sessionID);
                //barVisual.setVisibility(View.VISIBLE);

                //Intent intent = new Intent(ActivityPlay.this, DemoWave.class);
                //startActivity(intent);
                circleVisual.release();
                if(sessionID!=-1){
                    if (checkAudioPermission())
                        circleVisual.setAudioSessionId(MusicService.mediaServiceSessionID);
                    else
                        requestAudioPermission();

                }

                if(ktWave==0)
                    ktWave=1;
                else ktWave=0;
                if(ktLyric==1){
                    setLyricVisibility(false);
                    circleVisual.setVisibility(View.VISIBLE);
                    setCoverTint(true);
                    ktWave=1;
                }else {
                    if (ktWave == 1) {
                        circleVisual.setVisibility(View.VISIBLE);
                        setCoverTint(true);
                    } else {
                        circleVisual.setVisibility(View.INVISIBLE);
                        setCoverTint(false);
                    }
                }

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
        Toast.makeText(ActivityPlay.this, "Hello alo",Toast.LENGTH_SHORT).show();
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

    public void checkLyric(){
        if(songNow.getLyric()==R.string.no_lyric)
            haveLyric=false;
        else{
            haveLyric=true;
            timeLyricList = new ArrayList<>();
            lyricList = new ArrayList<>();
            String s = getString(songNow.getLyric());
            String[] mangStr = s.split("\\_");
            for (int i = 0; i < mangStr.length; i++) {
                String temp = mangStr[i];
                int post = temp.indexOf('-');
                String s1, s2;
                s1 = temp.substring(0, post);
                s2 = temp.substring(post + 1, temp.length() );
                timeLyricList.add(Integer.parseInt(s1));
                lyricList.add(s2);
                //rowLyricList.add(new RowLyric(s2, false));
            }

        }
    }

    public void setLyricVisibility(boolean b){
        if(b){
            lyricRow1.setVisibility(View.VISIBLE);
            lyricRow2.setVisibility(View.VISIBLE);
            ktLyric=1;
        }else{
            lyricRow1.setVisibility(View.INVISIBLE);
            lyricRow2.setVisibility(View.INVISIBLE);
            ktLyric=0;
        }
    }

    public void setCoverTint(boolean b){
        if(b){
            imgCoverUIP.setColorFilter(R.color.colorTintCover1);
        }else
            imgCoverUIP.setColorFilter(null);
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
                if(haveLyric){
                    for(int i=1; i<timeLyricList.size(); i++){
                        if(timeLyricList.get(i)>timeNow){
                            lyricRow1.setText(lyricList.get(i-1));
                            lyricRow2.setText(lyricList.get(i));
                            break;
                        }
                    }
                }
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

        if(haveLyric) {
            int j=0;
            for (int i=j; i< timeLyricList.size(); i++) {
                if (timeLyricList.get(i) > (timeNow) && timeLyricList.get(i) < (timeNow + 2)) {
                    Log.i("Demo", "setTimeNow: time lyric: "+timeLyricList.get(i));
                    lyricRow1.setText(lyricList.get(i));
                    j=i;
                    if (i < lyricList.size() - 1)
                        lyricRow2.setText(lyricList.get(i + 1));
                    else
                        lyricRow2.setText("End.");
                    break;
                }
            }
        }

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
        lyricRow2.setText("");
        lyricRow1.setText("");
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
        imgCoverUIP.setImageBitmap(myMedia.getBitmapByByte(songNow.getByteImage()));
        timeMax = songNow.getTimeTotal()/1000;
        txtTimeTotalUIP.setText(timeSong.format(timeMax*1000));
        sbSongUIP.setMax(timeMax);
        timeNow=0;
        checkLyric();
        if(haveLyric==false)
            lyricRow1.setText("No Lyric");
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
        lyricRow1 = (TextView) findViewById(R.id.textViewLyric1);
        lyricRow2 = (TextView) findViewById(R.id.textViewLyric2);
        //barVisual = (BarVisualizer) findViewById(R.id.bar);
        //waveVisualizer = (WaveVisualizer) findViewById(R.id.wave);
        circleVisual = (CircleLineVisualizer) findViewById(R.id.circleDemo);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        circleVisual.release();
        handlerUIP.removeCallbacks(runnableUIP);
    }

    private boolean checkAudioPermission() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestAudioPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, PERM_REQ_CODE);
    }

}
