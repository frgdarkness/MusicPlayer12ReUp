package com.example.musicplayer1;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.IBinder;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_ACCOUNTS = 1;
    private static final int PERM_REQ_CODE = 23;
    public static MainActivity instance;
    ImageButton btnPlayMain, btnQueueMain;
    TextView txtTitleSongMain, txtArtistSongMain;
    ImageView imgCoverSongMain;
    RelativeLayout layoutSongPlayMain;
    ArrayList<Song> listSongFull;
    ArrayList<Song> listSongTemp;
    ArrayList<Lyric> listLyric;
    static int mPosSong=0;
    static int mPosList=0;
    Song mSongNow;
    MyMedia myMedia;
    Song mSongPlaying;
    private MusicService musicService;
    private Intent musicIntent;
    private ServiceConnection serviceConnection;
    ArrayList<ArrayList<Song>> listListSong;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        instance = this;
        anhXa();
        //btnTest1 = (Button) findViewById(R.id.buttonTest1);
        //txtTest1 = (TextView) findViewById(R.id.textViewTest1);
        //imgTest1 = (ImageView) findViewById(R.id.imageView);
        askForPermission();
        requestAudioPermission();

        myMedia = new MyMedia(MainActivity.this);
        readData2();
        if(listSongFull==null)
            saveDataDefault2();
        //listSongFull = myMedia.getAllListSong();

        musicIntent = new Intent(this,MusicService.class);
        startService(musicIntent);

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

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        // Set the text for each tab.
        tabLayout.addTab(tabLayout.newTab().setText("Song"));
        tabLayout.addTab(tabLayout.newTab().setText("Playlist"));
        tabLayout.addTab(tabLayout.newTab().setText("Play Online"));
        //tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_label3));
        // Set the tabs to fill the entire layout.
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        // Use PagerAdapter to manage page views in fragments.
        // Use PagerAdapter to manage page views in fragments.
        // Each page is represented by its own fragment.
        final ViewPager viewPager = findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        // Setting a listener for clicks.
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override

            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        setLayoutSongPlayClick();
        setButtonPlayClick();
        btnQueueMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(MainActivity.this,"Song failed: "+MyMedia.countFailed,Toast.LENGTH_SHORT).show();
                //Intent intent = new Intent(MainActivity.this, QueueActivity.class);
                //startActivity(intent);
                viewPager.setCurrentItem(1);
            }
        });
    }

    public void anhXa(){
        btnPlayMain = (ImageButton) findViewById(R.id.buttonPlayMain);
        btnQueueMain = (ImageButton) findViewById(R.id.buttonListQueueMain);
        txtArtistSongMain = (TextView) findViewById(R.id.textViewArtistMainSongPlay);
        txtTitleSongMain = (TextView) findViewById(R.id.textviewTitleMainSongPlay);
        imgCoverSongMain = (ImageView) findViewById(R.id.imageViewCoverMainSongPlay);
        layoutSongPlayMain = (RelativeLayout) findViewById(R.id.layoutSongPlayMain);
    }

    public void setLayoutSongPlayMain(Song s){
        imgCoverSongMain.setImageBitmap(myMedia.getBitmapByByte(s.getByteImage()));
        txtTitleSongMain.setText(s.getTitle());
        txtArtistSongMain.setText(s.getArctis());
        setImageButtonPlay();
    }

    public void playAllSongAt(int n){
        listSongTemp = new ArrayList<>();
        for(Song i:listSongFull)
            listSongTemp.add(i);
        MusicService.posListNow = 0;
        MusicService.posSongNow = n;
        musicService.playSong(listSongTemp.get(n));
        setLayoutSongPlayMain(listSongTemp.get(n));
        Intent intent = new Intent(MainActivity.this, ActivityPlay.class);
        startActivity(intent);
    }

    public void setImageButtonPlay(){
        if(MusicService.isPlay)
            btnPlayMain.setImageResource(R.drawable.ic_pause_main);
        else
            btnPlayMain.setImageResource(R.drawable.ic_play_main);
    }

    public void setButtonPlayClick(){
        btnPlayMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityPlay.instance.pauseUIP();
            }
        });
    }

    public void setLayoutSongPlayClick(){
        layoutSongPlayMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ActivityPlay.class);
                startActivity(intent);
            }
        });
    }

    public void setListSongTemp(ArrayList<Song> list){
        listSongTemp = list;
    }

    public void removeSongInListSongTemp(int n){
        listSongTemp.remove(n);
    }

    public void swapSongInListSongTemp(int a, int b){
        Collections.swap(listSongTemp,a,b);
        mPosSong=b;
    }

    public void askForPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(this, Manifest.permission_group.STORAGE) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_SETTINGS) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(this, Manifest.permission.WAKE_LOCK) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_SETTINGS)
                        || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_PHONE_STATE)
                        || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WAKE_LOCK)
                        || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission_group.STORAGE)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Thông báo");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setMessage("Vui lòng xác nhận cấp quyền cho ứng dụng");
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @TargetApi(Build.VERSION_CODES.M)
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            requestPermissions(new String[]{
                                    Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WAKE_LOCK, Manifest.permission.READ_PHONE_STATE, Manifest.permission_group.STORAGE
                            }, MY_PERMISSIONS_REQUEST_ACCOUNTS);
                        }
                    });
                    builder.show();
                } else {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WAKE_LOCK, Manifest.permission.READ_PHONE_STATE, Manifest.permission_group.STORAGE
                            },
                            MY_PERMISSIONS_REQUEST_ACCOUNTS);
                }
            } else {
                //initView();
                creatFolderParent();
            }
        } else {
            //initView();
            creatFolderParent();
        }
    }

    public void creatFolderParent() {
        File file = new File(Config.PATH);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    public void saveDataDefault2(){
        String fileName = "Song.com";
        //String content = "Blog chia se kien thuc lap trinh";

        FileOutputStream outputStream = null;
        ObjectOutputStream oos;
        try {
            outputStream = openFileOutput(fileName, Context.MODE_PRIVATE);
            oos = new ObjectOutputStream(outputStream);
            ArrayList<Song> listSong = myMedia.getAllListSong();

            oos.writeObject(listSong);
            oos.close();
            listSongFull = listSong;
            Log.i("Demo", "saveDataDefault2: read listsong and save data sucessfull");
            Toast.makeText(this, "Saved successfully", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("Demo", "saveDataDefault2: error "+e.toString());
        }
    }

    private void readData2() {
        try {
            FileInputStream in = this.openFileInput("Song.com");
            BufferedReader br= new BufferedReader(new InputStreamReader(in));
            ObjectInputStream ois = new ObjectInputStream(in);
            ArrayList<Song> listSong = (ArrayList<Song>) ois.readObject();
            listSongFull = listSong;
            Log.i("Demo", "readData2: read listsong sucessfull");
        } catch (Exception e) {
            Toast.makeText(this,"Error:"+ e.getMessage(),Toast.LENGTH_SHORT).show();
            Log.i("Demo", "readData2: error "+e.toString());
        }
    }



    private void requestAudioPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, PERM_REQ_CODE);
    }
}
