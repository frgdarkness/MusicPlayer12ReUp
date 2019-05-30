package com.example.musicplayer1;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class FragmentSearch extends Fragment {
    Button btnSearch;
    public FragmentSearch(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search,container,false);
        btnSearch = view.findViewById(R.id.buttonSearch);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"HELLO",Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

    public void changeSearch(){
        btnSearch.setText("Tim Kiem");
    }

    public void show(){
        Toast.makeText(getActivity(),"Check",Toast.LENGTH_SHORT).show();
    }

}
