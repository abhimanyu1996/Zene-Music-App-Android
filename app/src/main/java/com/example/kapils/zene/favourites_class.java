package com.example.kapils.zene;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Kapils on 7/7/2016.
 */
public class favourites_class extends Fragment {

    ListView lv;
    String items[];
    ArrayList<File> fav_songs;
    MyDBhandler dBhandler;
    int i,j,k=0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.favourites_layout, container, false);

        lv = (ListView) view.findViewById(R.id.lvPlayFav);
        dBhandler = new MyDBhandler(this.getContext(),null,null,1);

        fav_songs = dBhandler.getFav();
        items = new String[fav_songs.size()];


        for(int i =0;i<fav_songs.size();i++){
            items[i] = fav_songs.get(i).getName().replace(".mp3", "").replace(".wav", "");
        }

        ArrayAdapter<String> adp = new ArrayAdapter<String>(view.getContext(),R.layout.songs_layout,R.id.textView3,items);
        lv.setAdapter(adp);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(view.getContext(), Player.class).putExtra("pos", position).putExtra("songlist", fav_songs));
            }
        });

        return view;
    }
}
