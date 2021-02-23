package com.example.graspandbloom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.media.Image;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.Network;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;


public class podcastPlayer extends AppCompatActivity implements Runnable {
    public static MediaPlayer mediaPlayer;
    public static Button button;
    private Button restartPlayer;
    private static SeekBar seekBar;
    private ArrayList<PodcastModel> list;
    private ImageView image;
    private TextView topic, duration, publishDate,message;
    private Thread thread;
    private static boolean Flag = false;
    public static int mediaDuration;
    public static byte buffer;
    public boolean internetCheck=true;
    private boolean firstCheck=false;
    private boolean whileCheck;
    private int mediaRestartduration;
    private boolean restartMediaCheck=false;
    private int index;

    private boolean previousState=false;
    private boolean ready=false;

    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_podcast_player);


        button = findViewById(R.id.button);
        message=findViewById(R.id.message);
        restartPlayer =findViewById(R.id.tryagain);
        seekBar = findViewById(R.id.seekBarID);
        seekBar.setEnabled(false);
        image = findViewById(R.id.podcastImage);
        topic = findViewById(R.id.podcastTopic);
        duration = findViewById(R.id.inPlayerDuration);
        publishDate = findViewById(R.id.inPlayerPublishDate);
        final Bundle bundle = getIntent().getExtras();
        builder=new AlertDialog.Builder(this);
        View view =getLayoutInflater().inflate(R.layout.media_player_loading,null);
        builder.setView(view);
        dialog=builder.create();
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {


                if (i==4)
                    podcastPlayer.this.finish();

                return false;
            }
        });
        dialog.setCanceledOnTouchOutside(false);



        restartPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


        if (isInternetCheck()){
            if (ready){
            if (mediaPlayer.isPlaying())
            {previousState=true;}
            else {previousState=false;}
            seekBar.setEnabled(false);
            button.setEnabled(false);

            restartMediaCheck=true;
            if (mediaPlayer.getCurrentPosition()>=mediaDuration)
            {mediaRestartduration=0; }else
            {     mediaRestartduration=mediaPlayer.getCurrentPosition()-5000;}

            setFlag(false);
            synchronized (this){
                if (thread!=null){
                    while(thread.getState() != Thread.State.TERMINATED){
                        try {
                            Thread.yield();
                            wait(1);
                        } catch (InterruptedException e) {
                            //  e.printStackTrace();
                        }
                    }}}
mediaPlayer.pause();
//mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer=null;

            setMediaPlayer();}else {
                setMediaPlayer();
            }}
        else {
            Toast.makeText(podcastPlayer.this, "Internet not available", Toast.LENGTH_SHORT).show();
        }    }
        });


//        SharedPreferences sd=getSharedPreferences("1",MODE_PRIVATE);
//        SharedPreferences.Editor edit=sd.edit();
//        edit.remove("index");
//        edit.remove("at");
//        edit.remove("check");
//        edit.clear();
//        edit.apply();
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.N) {
            cm.registerDefaultNetworkCallback(new ConnectivityManager.NetworkCallback(){
                @Override
                public void onAvailable(@NonNull Network network) {
                    super.onAvailable(network);
setInternetCheck(true);

                    if(firstCheck){
                    Toast.makeText(podcastPlayer.this, "Internet Available", Toast.LENGTH_SHORT).show();}
      }

                @Override
                public void onLost(@NonNull Network network) {
                    super.onLost(network);
                    setInternetCheck(false);
                    Toast.makeText(podcastPlayer.this, "Internet not available", Toast.LENGTH_SHORT).show();
                    if (!firstCheck)
                        firstCheck =true;


runOnUiThread(new Runnable() {
    @Override
    public void run() {



        message.setVisibility(View.VISIBLE);
        restartPlayer.setVisibility(View.VISIBLE);

    }
});



                 }


            });

        }


        if (bundle != null) {
     index=bundle.getInt("index");

            list = podcast_Activity.getPodcastList();


            duration.setText(getString(R.string.duration) + list.get(index).getDuration());
            publishDate.setText(getString(R.string.published) + list.get(index).getDate());
            topic.setText(getString(R.string.Topic)+list.get(index).getTopic());
            Picasso.get().load(list.get(index).getImageUrl()).into(image);

setMediaPlayer();


        }
    }

    public void setMediaPlayer(){
        dialog.show();

        mediaPlayer = new MediaPlayer();
        Log.d("Looping 1", "onCreate: "+mediaPlayer.isLooping());

        try {
            mediaPlayer.setDataSource(list.get(index).getAudioUrl());
            Log.d("Looping 2", "onCreate: "+mediaPlayer.isLooping());
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error occured", Toast.LENGTH_SHORT).show();
        }

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                synchronized (this){
                    if(thread!=null) {
                        while (thread.getState() != Thread.State.TERMINATED) {
                            try {
                                Thread.yield();
                                wait(1);

                            } catch (InterruptedException e) {

                            }
                        }
                    }
                }


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

       final  MediaPlayer.OnPreparedListener preparedListener = new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(final MediaPlayer mediaPlayer) {
                Toast.makeText(podcastPlayer.this, ""+"reachedOnPrepared", Toast.LENGTH_SHORT).show();
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

                if (restartMediaCheck){
if (isInternetCheck()){
    restartPlayer.setVisibility(View.GONE);
message.setVisibility(View.GONE);}
                    mediaPlayer.seekTo(mediaRestartduration);
                    seekBar.setProgress(mediaRestartduration);

                    button.setEnabled(true);
                    seekBar.setEnabled(true);
                    if (previousState){
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
                        }else {
                            button.setText("Pause");
                        }
                    }

                    restartMediaCheck=false;
                }else {
                    button.setEnabled(true);
                    seekBar.setEnabled(true);
                }
                if(!ready)
                    ready=true;

                if (isInternetCheck()){
                    restartPlayer.setVisibility(View.GONE);message.setVisibility(View.GONE);
                }

                dialog.dismiss();
            }

        };
        mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                Toast.makeText(podcastPlayer.this, "Error occurred", Toast.LENGTH_SHORT).show();
                return false;
            }

        });
        mediaPlayer.setOnPreparedListener(preparedListener);
        //      button.setEnabled(true);
        mediaPlayer.prepareAsync();

        mediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {

            @Override
            public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {
                seekBar.setSecondaryProgress((int) ((i/(double)100)*mediaPlayer.getDuration()));
                buffer= (byte) i;
//                  if(!internetCheck){
//                      while (!internetCheck){
//                          if((i!=100) &&(( (int)(mediaPlayer.getCurrentPosition()*0.001))>=((int)(((buffer/(double)100)*mediaDuration)*0.001)))){
//                              int rP =mediaPlayer.getCurrentPosition();
//                          }
//                          synchronized (this){
//                              try {
//                                  wait(1);
//                              } catch (InterruptedException e) {
//
//                              }
//
//                          }
//                      }
//                  }
            }

        });

    }

     public synchronized  boolean isInternetCheck() {
        notifyAll();
        return internetCheck;
    }

    public synchronized  void setInternetCheck(boolean internetCheck) {
        this.internetCheck = internetCheck;
        notifyAll();


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

        synchronized (this){
while(getFlag() != true){
    try {
     //   Log.d("currentPosition3", "onClick: " + "ok" + getFlag());
        wait(1);
    } catch (InterruptedException e) {
        Toast.makeText(this, "Error occured", Toast.LENGTH_SHORT).show();
    }
}}
        try {
            Thread.sleep(1000);}
        catch (InterruptedException e){
            Toast.makeText(this, "Error occured", Toast.LENGTH_SHORT).show();
        }

int c=0;

        while (mediaPlayer.getCurrentPosition()<mediaDuration  && getFlag()){
                Log.d("Buffer check", "onPrepared: "+buffer + " "+mediaPlayer.getCurrentPosition()+" "+(buffer/(double)100)*mediaDuration + " "+(((buffer!=(byte)100))&&((int)((mediaPlayer.getCurrentPosition()*0.001)+5)>(int)(((buffer/(double)100)*mediaDuration)*0.001))));
                seekBar.setProgress(mediaPlayer.getCurrentPosition());
                if ((int)(mediaPlayer.getCurrentPosition()*0.001) == (int)(mediaDuration*0.001)) {

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

    @Override
    protected void onPause() {
        super.onPause();

        if (mediaPlayer!=null){
            mediaPlayer.pause();
            button.setText("Start");
        }
    }

}
