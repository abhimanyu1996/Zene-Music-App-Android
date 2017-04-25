package com.example.kapils.zene;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
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
public class music_lib_class extends Fragment {

    ListView lv;
    String items[];

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.music_lib_layout, container, false) ;
        lv = (ListView) view.findViewById(R.id.lvPlayList);

        final ArrayList<File> mySongs = findSongs(Environment.getExternalStorageDirectory());

        items = new String[mySongs.size()];

        for(int i =0;i<mySongs.size();i++){
            items[i] = mySongs.get(i).getName().toString().replace(".mp3", "").replace(".wav", "");
        }

        ArrayAdapter<String> adp = new ArrayAdapter<String>(view.getContext(),R.layout.songs_layout,R.id.textView3,items);
        lv.setAdapter(adp);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(view.getContext(), Player.class).putExtra("pos", position).putExtra("songlist", mySongs));

            }
        });
        return view;
    }

    //fuction to get all songs
    public ArrayList<File> findSongs(File root){
        ArrayList<File> al = new ArrayList<File>();
        File[] files = root.listFiles();

        if(files != null) {
            for(File f : files){

                if (f.isDirectory() && !f.isHidden()) {
                    ArrayList<File> z = findSongs(f);
                    al.addAll(z);
                } else if (f.getName().toString().endsWith(".mp3") || f.getName().toString().endsWith(".wav")) {
                    al.add(f);
                }
            }
        }

        return al;
    }

}
