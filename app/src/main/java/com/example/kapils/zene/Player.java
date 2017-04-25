package com.example.kapils.zene;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;


public class Player extends AppCompatActivity implements View.OnClickListener {

    static MediaPlayer mp;
    ArrayList<File> mySongs;
    SeekBar sb;
    Uri u;
    Handler myHandler= new Handler();
    int position;
    Button Play, Next, Prev, Fav;
    MyDBhandler dBhandler;
    TextView sname,currentT,finalT;
    private double startTime=0,finalTime=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player);

        Play = (Button) findViewById(R.id.Play);
        Next = (Button) findViewById(R.id.Next);
        Prev = (Button) findViewById(R.id.Prev);
        //Fav=(Button)findViewById(R.id.fav);
        dBhandler = new MyDBhandler(this, null, null, 1);
        sname = (TextView) findViewById(R.id.song_name);
        currentT = (TextView) findViewById(R.id.current);
        finalT = (TextView) findViewById(R.id.total);

        Play.setOnClickListener(this);
        Next.setOnClickListener(this);
        Prev.setOnClickListener(this);

        sb = (SeekBar) findViewById(R.id.seekBar);

        if (mp != null) {
            mp.stop();
            mp.release();
        }

        Intent i = getIntent();
        Bundle b = i.getExtras();
        mySongs = (ArrayList) b.getParcelableArrayList("songlist");
        position = b.getInt("pos", 0);

        u = Uri.parse(mySongs.get(position).toString());
        mp = MediaPlayer.create(getApplicationContext(), u);
        mp.start();

        sname.setText(mySongs.get(position).getName().toString().replace(".mp3", "").replace(".wav", ""));
        finalTime=mp.getDuration();
        startTime=mp.getCurrentPosition();
        finalT.setText(String.format("%d:%02d",
                TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                TimeUnit.MILLISECONDS.toSeconds((long) finalTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) finalTime))));

       currentT.setText(String.format("%d:%02d",
               TimeUnit.MILLISECONDS.toMinutes((long) startTime),
               TimeUnit.MILLISECONDS.toSeconds((long) startTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) startTime))));

        myHandler.postDelayed(UpdateSongTime, 100);

        mp.setOnCompletionListener(
                new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp1) {
                        /*mp.stop();
                        mp.release();*/
                        position = (position + 1) % (mySongs.size());
                        u = Uri.parse(mySongs.get(position).toString());
                        mp = MediaPlayer.create(getApplicationContext(), u);
                        mp.start();
                        finalTime = mp.getDuration();
                        finalT.setText(String.format("%d:%02d",
                                TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                                TimeUnit.MILLISECONDS.toSeconds((long) finalTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) finalTime))));

                        sname.setText(mySongs.get(position).getName().toString().replace(".mp3", "").replace(".wav", ""));
                        sb.setMax(mp.getDuration());

                    }
                }
        );


        sb.setMax(mp.getDuration());

        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            boolean k = false;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(k)
                    mp.seekTo(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                k = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                k= false;
            }
        });

    }

    private Runnable UpdateSongTime =new Runnable() {
        @Override
        public void run() {
            startTime=mp.getCurrentPosition();
            currentT.setText(String.format("%d:%02d",
                    TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                    TimeUnit.MILLISECONDS.toSeconds((long) startTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) startTime))));
            sb.setProgress((int)startTime);
            myHandler.postDelayed(this,100);
        }
    };



    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.Play:
                if (mp.isPlaying()) {
                    mp.pause();
                    Play.setBackgroundResource(R.drawable.plays);
                } else {
                    mp.start();
                    Play.setBackgroundResource(R.drawable.pauses);
                }
                break;
            case R.id.Next:
                mp.stop();
                mp.release();
                position = (position + 1) % (mySongs.size());
                u = Uri.parse(mySongs.get(position).toString());
                mp = MediaPlayer.create(getApplicationContext(), u);
                mp.start();
                finalTime=mp.getDuration();
                finalT.setText(String.format("%d:%02d",
                        TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                        TimeUnit.MILLISECONDS.toSeconds((long) finalTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) finalTime))));

                sname.setText(mySongs.get(position).getName().toString().replace(".mp3", "").replace(".wav", ""));
                sb.setMax(mp.getDuration());

                startTime = mp.getCurrentPosition();
                sb.setProgress((int)startTime);
                myHandler.postDelayed(UpdateSongTime, 100);
                break;
            case R.id.Prev:
                mp.stop();
                mp.release();
                position = ((position - 1) < 0) ? (mySongs.size() - 1) : (position - 1);
                u = Uri.parse(mySongs.get(position).toString());
                mp = MediaPlayer.create(getApplicationContext(), u);
                mp.start();
                finalTime=mp.getDuration();
                finalT.setText(String.format("%d:%02d",
                        TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                        TimeUnit.MILLISECONDS.toSeconds((long) finalTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) finalTime))));


                sname.setText(mySongs.get(position).getName().toString().replace(".mp3", "").replace(".wav", ""));
               sb.setMax(mp.getDuration());

                startTime = mp.getCurrentPosition();
                sb.setProgress((int) startTime);
                myHandler.postDelayed(UpdateSongTime, 100);
                break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

  /*  public void manageFav(View view){
        String current_song=mySongs.get(position).getPath();

        if((Fav.getText().toString()).equalsIgnoreCase("Add")){

          // if (!checkPresent(current_song)){
                Fav.setText("Remove");
            Log.d(current_song, "manageFav: ");
                Favourites favourite=new Favourites(current_song);
                dBhandler.addFav(favourite);
           //}
        }
       else{
            if(checkPresent(current_song)){
            Fav.setText("Add");
            dBhandler.deleteFav(current_song);}
       }
    }

    private boolean checkPresent(String current_song) {
        ArrayList<File> fav_songs = dBhandler.getFav();
        for(int i=0;i<fav_songs.size();i++)
            if(fav_songs.get(i).toString().equals(current_song))
                return true;

        return false;
    }*/
}
