package com.example.musicplayer1;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public class FragmentPlaylist extends Fragment {
    ImageButton btnAdd;
    ListView listViewPlayL;
    public static FragmentPlaylist instance;
    //ArrayList<ArrayList<Song>> listPlaylist;
    ArrayList<Playlist> listPlaylistF;
    PlaylistAdapter pAdapter;
    RelativeLayout relaFavo, relaLast;
    public FragmentPlaylist(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_playlist,container,false);
        btnAdd = view.findViewById(R.id.buttonAddFrgMnPlayList);

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
        //readData2();
        listPlaylistF = new ArrayList<>();
        listPlaylistF.addAll(MainActivity.instance.listPlaylist);
        listViewPlayL = view.findViewById(R.id.listViewListFrgMnPlayList);
        relaFavo = (RelativeLayout) view.findViewById(R.id.relaFavoriteFrMPL);
        relaLast = (RelativeLayout)view.findViewById(R.id.relaLastSongsFrMPL);

        pAdapter = new PlaylistAdapter(getActivity(), R.layout.playlist_row, listPlaylistF);
        listViewPlayL.setAdapter(pAdapter);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getActivity(),"add playlist",Toast.LENGTH_SHORT).show();
                //createDialogAdd();
                final Dialog dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.dialog_create_playlist);
                Button btnCreat, btnCancel;
                btnCreat = dialog.findViewById(R.id.buttonCreatDiaCPL);
                btnCancel = dialog.findViewById(R.id.buttonCancelDiaCPL);
                final EditText edt = dialog.findViewById(R.id.editTextDialogCreatPL);
                btnCreat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String name = edt.getText().toString();
                        Playlist pl = new Playlist(name,new ArrayList<Song>());
                        MainActivity.instance.addPlaylist(pl);
                        listPlaylistF.clear();
                        listPlaylistF.addAll(MainActivity.instance.listPlaylist);
                        //pAdapter.notifyDataSetChanged();
                        pAdapter = new PlaylistAdapter(getActivity(), R.layout.playlist_row, listPlaylistF);
                        listViewPlayL.setAdapter(pAdapter);

                    }
                });
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });
                dialog.show();
            }
        });
        relaFavo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"CountPLMain: "+MainActivity.instance.listPlaylist.size()+" - last: "
                            +MainActivity.instance.listPlaylist.get(MainActivity.instance.listPlaylist.size()-1).getName(),Toast.LENGTH_SHORT).show();
            }
        });

        relaLast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listPlaylistF.clear();
                listPlaylistF.addAll(MainActivity.instance.listPlaylist);
                Toast.makeText(getActivity(), listPlaylistF.get(listPlaylistF.size()-1).getName(),Toast.LENGTH_SHORT).show();
                pAdapter.notifyDataSetChanged();
                pAdapter = new PlaylistAdapter(getActivity(), R.layout.playlist_row, listPlaylistF);
                listViewPlayL.setAdapter(pAdapter);
                //listViewPlayL.invalidateViews();
                //listViewPlayL.refreshDrawableState();
                //listViewPlayL.setAdapter(new PlaylistAdapter(getActivity(),R.layout.playlist_row,new ArrayList<Playlist>()));
                //pAdapter.notifyDataSetChanged();
            }
        });
        return view;
    }

    public void updateListViewPL(){
        listPlaylistF.clear();
        listPlaylistF.addAll(MainActivity.instance.listPlaylist);
        //Toast.makeText(getActivity(), listPlaylistF.get(listPlaylistF.size()-1).getName(),Toast.LENGTH_SHORT).show();
        //pAdapter.notifyDataSetChanged();
        //pAdapter = new PlaylistAdapter(getActivity(), R.layout.playlist_row, listPlaylistF);
        //listViewPlayL.setAdapter(pAdapter);
    }

    private void readData2() {
        try {
            FileInputStream in = getActivity().openFileInput("Playlist.com");
            BufferedReader br= new BufferedReader(new InputStreamReader(in));
            ObjectInputStream ois = new ObjectInputStream(in);
            //ArrayList<Song> listSong = (ArrayList<Song>) ois.readObject();
            //listSongFull = listSong;
            listPlaylistF = (ArrayList<Playlist>) ois.readObject();
            Log.i("Demo", "readData2 in fragmentPlaylist: read listsong sucessfull");
        } catch (Exception e) {
            Toast.makeText(getActivity(),"Error:"+ e.getMessage(),Toast.LENGTH_SHORT).show();
            Log.i("Demo", "readData2: error "+e.toString());
        }
    }

    public void createDialogAdd(){
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_create_playlist);
        Button btnCreat, btnCancel;
        btnCreat = dialog.findViewById(R.id.buttonCreatDiaCPL);
        btnCancel = dialog.findViewById(R.id.buttonCancelDiaCPL);
        final EditText edt = dialog.findViewById(R.id.editTextDialogCreatPL);
        btnCreat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edt.getText().toString();
                Playlist pl = new Playlist(name,new ArrayList<Song>());
                MainActivity.instance.addPlaylist(pl);
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialog.show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.exit(0);
    }

}
