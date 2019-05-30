package com.example.musicplayer1;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;

public class FragmentListSong extends Fragment implements SearchView.OnQueryTextListener{

    ArrayList<Song> listSongM;
    ArrayList<Song> listSongF;
    SongAdapter adapter;
    SearchView editSearch;
    ListView list;

    public FragmentListSong(){

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        listSongM = ((MainActivity) getActivity()).listSongFull;
        listSongF = new ArrayList<>();
        for(Song i:listSongM)
            listSongF.add(i);

        View view = (View) inflater.inflate(R.layout.fragment_list_song,container,false);

        list = view.findViewById(R.id.listViewSong);
        adapter = new SongAdapter(getActivity(),R.layout.song_row,listSongF);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(),"hello",Toast.LENGTH_SHORT).show();
                ((MainActivity) getActivity()).playAllSongAt(position);


            }
        });
        editSearch = (SearchView) view.findViewById(R.id.searchSongList);
        //editsearch1.setBackgroundColor(Color.BLUE);
        editSearch.setOnQueryTextListener(this);

        return view;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {

        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        Toast.makeText(getActivity(),"search Song",Toast.LENGTH_SHORT).show();
        String text = newText;
        adapter.filter(text);

        //list.setVisibility(View.VISIBLE);
        return false;
    }
}
