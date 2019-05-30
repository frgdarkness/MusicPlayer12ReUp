package com.example.musicplayer1;

import android.os.Environment;

import java.util.ArrayList;

/**
 * Created by ducth on 3/17/2018.
 */

public class Config {
    public static final String PREF = "pref";
    public static final String KEY_PUT_INTENT = "key put intent";
    public static final String KEY_PUT_INTENT_DETAIL = "key put intent details";
    public static final String TIME_OFF = "time off";
    public static String PUT_EDIT_SONG = "put edit song";
    public static String PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/MusicEditors";
    public static ArrayList<Song> LIST_SONG = new ArrayList<>();

    public static final String EQUALIZER_TURN = "equalizer_turn";
    public static final String BASS_BOOSTER = "bass_boost";
    public static final String VIRTUALIZER = "virtualizer";
    public static final String REVERB = "reverb";
    public static final String CREAT_SHORTCUT = "creat shortcut";

    public static final int REQUEST_BOOSTER = 1000;
    public static final String KEY_BOOSTER_DATA = "booster";
    public static final String KEY_GO_EQUALIZER = "go_equalizer";
    public static final String KEY_GO_VISUALIZER = "go_visualizer";
    public static final String CHECK_PER = "check permission";

    public static final String INSTALL_FIRST = "install_first";

    public static final String PRESET_SELECT = "preset";

    public static final String FIRST_START = "first_start";
    public static final String FIRST_ENABLE = "enable";
    public static final String FIRST_GUIDE = "guide";

}
