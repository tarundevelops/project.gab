package com.example.graspandbloom;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

public class podcastPlayer extends AppCompatActivity {
    private MediaPlayer mediaPlayer;
    private Button button;
    private SeekBar seekBar;
    private List<PodcastModel> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_podcast_player);


        button = findViewById(R.id.button);
        seekBar = findViewById(R.id.seekBarID);
        final Bundle bundle = getIntent().getExtras();

        if (bundle != null) {

            list = podcast_Activity.getPodcastList();
                mediaPlayer = new MediaPlayer();

            try {
                mediaPlayer.setDataSource(list.get(bundle.getInt("index")).getAudioUrl());
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Error occured", Toast.LENGTH_SHORT).show();
            }

//            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//                @Override
//                public void onCompletion(MediaPlayer mediaPlayer) {
//                    int i = mediaPlayer.getDuration();
//                }
//            });

            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                    if (b){
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

            MediaPlayer.OnPreparedListener preparedListener = new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(final MediaPlayer mediaPlayer) {
                    seekBar.setMax(mediaPlayer.getDuration());
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (mediaPlayer.isPlaying()){
                                mediaPlayer.pause();
                                button.setText("Play");
                            }else {
                                mediaPlayer.start();

                                button.setText("Pause");
                            }
                        }
                    });
                }
            };
            mediaPlayer.setOnPreparedListener(preparedListener);
            mediaPlayer.prepareAsync();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null){
            mediaPlayer.pause();
            mediaPlayer.release();

            mediaPlayer = null;        }
    }
}