package com.example.musicplayer1;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public class FragmentPlaylist extends Fragment {
    ImageButton btnAdd;
    ListView listViewPlayL;
    //ArrayList<ArrayList<Song>> listPlaylist;
    ArrayList<Playlist> listPlaylist;
    PlaylistAdapter pAdapter;
    public FragmentPlaylist(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_playlist,container,false);
        btnAdd = view.findViewById(R.id.buttonAddFrgMnPlayList);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"add playlist",Toast.LENGTH_SHORT).show();
            }
        });
        /*
        ArrayList<Song> listSong = new ArrayList<>();
        ArrayList<Song> listSong2 = new ArrayList<>();
        listSong2.addAll(MainActivity.instance.listSongFull);
        Playlist p1 = new Playlist("playlist1",listSong);
        Playlist p2 = new Playlist("Coldplay",listSong);
        Playlist p3 = new Playlist("Vpop",listSong);
        Playlist p4 = new Playlist("new USUK 1",listSong);
        listPlaylist = new ArrayList<>();
        listPlaylist.add(p1);
        listPlaylist.add(p2);
        listPlaylist.add(p3);
        listPlaylist.add(p4);
        */
        readData2();
        listViewPlayL = view.findViewById(R.id.listViewListFrgMnPlayList);
        pAdapter = new PlaylistAdapter(getActivity(), R.layout.playlist_row, listPlaylist);
        listViewPlayL.setAdapter(pAdapter);
        return view;
    }

    private void readData2() {
        try {
            FileInputStream in = getActivity().openFileInput("Playlist.com");
            BufferedReader br= new BufferedReader(new InputStreamReader(in));
            ObjectInputStream ois = new ObjectInputStream(in);
            //ArrayList<Song> listSong = (ArrayList<Song>) ois.readObject();
            //listSongFull = listSong;
            listPlaylist = (ArrayList<Playlist>) ois.readObject();
            Log.i("Demo", "readData2 in fragmentPlaylist: read listsong sucessfull");
        } catch (Exception e) {
            Toast.makeText(getActivity(),"Error:"+ e.getMessage(),Toast.LENGTH_SHORT).show();
            Log.i("Demo", "readData2: error "+e.toString());
        }
    }
}
