package com.example.graspandbloom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
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

public class podcastPlayer extends AppCompatActivity implements Runnable {
    public static MediaPlayer mediaPlayer;
    public static Button button;
    private static SeekBar seekBar;
    private List<PodcastModel> list;
    private ImageView image;
    private TextView topic, duration, publishDate;
    private Thread thread;
    private static boolean Flag = false;
    public static int mediaDuration;
    public static byte buffer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_podcast_player);


        button = findViewById(R.id.button);
        seekBar = findViewById(R.id.seekBarID);
        seekBar.setEnabled(false);
        image = findViewById(R.id.podcastImage);
        topic = findViewById(R.id.podcastTopic);
        duration = findViewById(R.id.inPlayerDuration);
        publishDate = findViewById(R.id.inPlayerPublishDate);

        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        final Bundle bundle = getIntent().getExtras();


        if (bundle != null) {
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.N) {
                cm.registerDefaultNetworkCallback(new ConnectivityManager.NetworkCallback(){
                    @Override
                    public void onAvailable(@NonNull Network network) {
                        super.onAvailable(network);
                        Toast.makeText(podcastPlayer.this, "Internet Available", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onLost(@NonNull Network network) {
                        super.onLost(network);
                        Toast.makeText(podcastPlayer.this, "Internet not available", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            list = podcast_Activity.getPodcastList();

            duration.setText(getString(R.string.duration) + list.get(bundle.getInt("index")).getDuration());
            publishDate.setText(getString(R.string.published) + list.get(bundle.getInt("index")).getDate());
            topic.setText(getString(R.string.Topic)+list.get(bundle.getInt("index")).getTopic());
            Picasso.get().load(list.get(bundle.getInt("index")).getImageUrl()).into(image);


            mediaPlayer = new MediaPlayer();
            Log.d("Looping 1", "onCreate: "+mediaPlayer.isLooping());

            try {
                mediaPlayer.setDataSource(list.get(bundle.getInt("index")).getAudioUrl());
                Log.d("Looping 2", "onCreate: "+mediaPlayer.isLooping());
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Error occured", Toast.LENGTH_SHORT).show();
            }

            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    synchronized (this){
                        while (thread.getState() != Thread.State.TERMINATED) {
                            try {
                                Thread.yield();
                                wait(1);

                            } catch (InterruptedException e) {

                            }
                        }
                    }
                    Toast.makeText(podcastPlayer.this, " " + thread.getState(), Toast.LENGTH_SHORT).show();

                    button.setText("Start");
                }
            });

            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                    if (b) {
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
                    mediaDuration = mediaPlayer.getDuration();
                    seekBar.setMax(mediaDuration);
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (mediaPlayer.isPlaying()) {
                                mediaPlayer.pause();


                                button.setText("Start");
                            } else {

                                mediaPlayer.start();
                                button.setText("Pause");


                                if (getFlag()!=true && thread == null) {
                                    setFlag(true);
                                    thread = new Thread(new podcastPlayer());
                                    thread.start();
                                }else if(getFlag()!=true && thread!=null && thread.getState() == Thread.State.TERMINATED  ) {
                                    setFlag(true);
                                    thread = new Thread(new podcastPlayer());
                                    thread.start();
                                }
                            }
                        }
                    });
                    button.setEnabled(true);
                    seekBar.setEnabled(true);
                }
            };

            mediaPlayer.setOnPreparedListener(preparedListener);
     //      button.setEnabled(true);
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
                @Override
                public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {
                    buffer= (byte) i;
                }
            });



        }
    }

    public synchronized boolean getFlag(){
        notifyAll();
        return Flag;
}
public synchronized void setFlag(boolean a){Flag = a;
notifyAll();
}
    @Override
    protected void onDestroy() {
        super.onDestroy();
        setFlag(false);
        if (thread!=null){
        synchronized (this){

        while(thread.getState() != Thread.State.TERMINATED){
            try {
                Thread.yield();
                wait(1);
            } catch (InterruptedException e) {
              //  e.printStackTrace();
            }
        }}}
        if (mediaPlayer != null){
            if (thread==null){
            mediaPlayer.pause();

            mediaPlayer.release();
            mediaPlayer =null;
            } else if(thread.getState() == Thread.State.TERMINATED){
                mediaPlayer.pause();

                mediaPlayer.release();
                mediaPlayer =null;
            }}
    }

    @Override
     public void run() {
        try {
            Thread.sleep(1000);}
        catch (InterruptedException e){
            Toast.makeText(this, "Error occured", Toast.LENGTH_SHORT).show();
        }
        synchronized (this){
while(getFlag() != true){
    try {
     //   Log.d("currentPosition3", "onClick: " + "ok" + getFlag());
        wait(1);
    } catch (InterruptedException e) {
        Toast.makeText(this, "Error occured", Toast.LENGTH_SHORT).show();
    }
}}

int c=0;

        while (mediaPlayer.getCurrentPosition()<mediaDuration  && getFlag()){
                Log.d("Buffer check", "onPrepared: "+buffer + " "+mediaPlayer.isPlaying());
                seekBar.setProgress(mediaPlayer.getCurrentPosition());
                if ((int)(mediaPlayer.getCurrentPosition()*0.001) == (int)(mediaDuration*0.001)){

if (c>4){

    break;
}else {c++;}

                }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Toast.makeText(this, "Error occured", Toast.LENGTH_SHORT).show();
            }
        }
        if (mediaPlayer.getCurrentPosition() >=mediaDuration){

            seekBar.setProgress(mediaDuration);
        }
synchronized (this) {
    seekBar.setProgress(0);
    setFlag(false);
    notifyAll();
}

    }
}
