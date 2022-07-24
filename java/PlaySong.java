package com.example.sounfeel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class PlaySong extends AppCompatActivity {
    @Override
    protected void onDestroy() {
        // When the activity is destroyed this function gets executed
        super.onDestroy();
        handler.removeCallbacks(runnable);
        mediaPlayer.stop();
        mediaPlayer.release();
    }

    TextView textView;
    ImageView previous, play, next, repeat;
    SeekBar seekBar;
    MediaPlayer mediaPlayer;
    ArrayList<File> songs;
    String textContent;
    int position;
    Handler handler;
    Runnable runnable;
    boolean isRepeat = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_song);
        textView = findViewById(R.id.textView);
        previous = findViewById(R.id.previous);
        play = findViewById(R.id.play);
        next = findViewById(R.id.next);
        seekBar = findViewById(R.id.seekBar);
        repeat = findViewById(R.id.repeat);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        songs = (ArrayList)bundle.getParcelableArrayList("songList");
        textContent = intent.getStringExtra("currentSong");
        textView.setText(textContent);
        textView.setSelected(true);
        position = intent.getIntExtra("position", 0);
        Uri uri = Uri.parse(songs.get(position).toString());
        mediaPlayer = MediaPlayer.create(this, uri);
        seekBar.setMax(mediaPlayer.getDuration());
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                seekBar.setProgress(mediaPlayer.getCurrentPosition());
                handler.postDelayed(this, 800);
            }
        };
        mediaPlayer.start();
        handler.postDelayed(runnable, 0);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(b){
                    mediaPlayer.seekTo(i);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handler.removeCallbacks(runnable);
                mediaPlayer.stop();
                mediaPlayer.release();
                if(position!=0){
                    position--;
                }
                else {
                    position = songs.size() - 1;
                }
                Uri uri = Uri.parse(songs.get(position).toString());
                mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
                seekBar.setMax(mediaPlayer.getDuration());
                mediaPlayer.start();
                handler.postDelayed(runnable, 0);
                textView.setText(songs.get(position).getName().replace(".mp3",""));
                play.setImageResource(R.drawable.pause);
                if(isRepeat){
                    mediaPlayer.setLooping(true);
                    repeat.setImageResource(R.drawable.repeting);
                }
                else {
                    mediaPlayer.setLooping(true);
                    repeat.setImageResource(R.drawable.repeat);
                }
            }
        });
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mediaPlayer.isPlaying()){
                    play.setImageResource(R.drawable.play);
                    mediaPlayer.pause();
                    handler.removeCallbacks(runnable);
                }
                else {
                    play.setImageResource(R.drawable.pause);
                    mediaPlayer.start();
                    handler.postDelayed(runnable, 0);
                }
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.stop();
                mediaPlayer.release();
                if(position!=songs.size()-1){
                    position++;
                }
                else {
                    position = 0;
                }
                Uri uri = Uri.parse(songs.get(position).toString());
                mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
                seekBar.setMax(mediaPlayer.getDuration());
                mediaPlayer.start();
                handler.postDelayed(runnable, 0);
                textView.setText(songs.get(position).getName().replace(".mp3",""));
                play.setImageResource(R.drawable.pause);
                if(isRepeat){
                    mediaPlayer.setLooping(true);
                    repeat.setImageResource(R.drawable.repeting);
                }
                else {
                    mediaPlayer.setLooping(false);
                    repeat.setImageResource(R.drawable.repeat);
                }
            }
        });
        repeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mediaPlayer.isLooping()) {
                    mediaPlayer.setLooping(false);
                    repeat.setImageResource(R.drawable.repeat);
                    isRepeat = false;
                }
                else {
                    mediaPlayer.setLooping(true);
                    repeat.setImageResource(R.drawable.repeting);
                    isRepeat = true;
                }
            }
        });
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mediaPlayer.stop();
                mediaPlayer.release();
                if(position!=songs.size()-1){
                    position++;
                }
                else {
                    position = 0;
                }
                Uri uri = Uri.parse(songs.get(position).toString());
                mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
                seekBar.setMax(mediaPlayer.getDuration());
                mediaPlayer.start();
                handler.postDelayed(runnable, 0);
                textView.setText(songs.get(position).getName().replace(".mp3",""));
            }
        });
    }
}