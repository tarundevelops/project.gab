package com.example.graspandbloom;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;

public class podcastPlayer extends AppCompatActivity {
    public MediaPlayer mediaPlayer;
    private Button button;
    private SeekBar seekBar;
    private List<PodcastModel> list;
    private ImageView image;
    private TextView topic;
    private Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_podcast_player);


        button = findViewById(R.id.button);
        seekBar = findViewById(R.id.seekBarID);
        image = findViewById(R.id.podcastImage);
        topic = findViewById(R.id.podcastTopic);
        final Bundle bundle = getIntent().getExtras();

        if (bundle != null) {

            list = podcast_Activity.getPodcastList();
            topic.setText(list.get(bundle.getInt("index")).getTopic());
            Picasso.get().load(list.get(bundle.getInt("index")).getImageUrl()).into(image);
                mediaPlayer = new MediaPlayer();

            try {
                mediaPlayer.setDataSource(list.get(bundle.getInt("index")).getAudioUrl());
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Error occured", Toast.LENGTH_SHORT).show();
            }

            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
               seekBar.setProgress(0);
               button.setText("Play");
               handler.removeCallbacks(runnable);
                }
            });

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
                    handler.removeCallbacks(runnable);
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (mediaPlayer.isPlaying()){
                                mediaPlayer.pause();
                                handler.removeCallbacks(runnable);

                                button.setText("Play");
                            }else {
                                handler.postDelayed(runnable,1000);
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

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            seekBar.setProgress(mediaPlayer.getCurrentPosition());
            Log.d("TimeLog", "run: " + (int)(mediaPlayer.getCurrentPosition()*0.001));
            handler.postDelayed(runnable,1000);


        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
        handler = null;
        if (mediaPlayer != null){
            mediaPlayer.pause();
            mediaPlayer.release();

            mediaPlayer = null;        }
    }
}