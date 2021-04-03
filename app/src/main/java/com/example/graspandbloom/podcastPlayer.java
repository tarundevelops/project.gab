package com.example.graspandbloom;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class podcastPlayer extends AppCompatActivity implements Runnable {
    public static MediaPlayer mediaPlayer;
    public static Button button,likeButton;
    private Button restartPlayer;
    private static SeekBar seekBar;
    private ArrayList<PodcastModel> list;
    private ImageView image;
    private TextView topic, duration, publishDate,message,listenedby,likedBy;
    private static TextView timeLeft;
    private Thread thread;
    private static boolean Flag = false;
    public static int mediaDuration;
    public static byte buffer;
    public boolean internetCheck=true;
    private boolean firstCheck=false;
    private int mediaRestartduration;
    private boolean restartMediaCheck=false;
    private int index;
    private FirebaseFirestore db=FirebaseFirestore.getInstance();
    private FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();

    private boolean previousState=false;
    private boolean ready=false;

   // private AlertDialog.Builder builder;
   // private AlertDialog dialog;
   ProgressDialog dialog;
    private boolean likeCheck=false;

    private static int listenedByCount;
    private static int LikedByCount;


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
        listenedby=findViewById(R.id.podcastListnedBy);
        timeLeft=findViewById(R.id.timeleft);
        likeButton=findViewById(R.id.like);
        likedBy=findViewById(R.id.podcastLikedBy);

        final Bundle bundle = getIntent().getExtras();
       /* builder=new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.media_player_loading,null);
        builder.setView(view);
        dialog=builder.create();*/
        dialog = ProgressDialog.show(this, "", "Loading ....");
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {


                if (i==4){
                    podcastPlayer.this.finish();
                dialog.dismiss();}

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

                        }
                    }}}
mediaPlayer.pause();
            mediaPlayer.release();
            mediaPlayer=null;

            setMediaPlayer();}else {
                setMediaPlayer();
            }}
        else {
            Toast.makeText(podcastPlayer.this, "Internet not available", Toast.LENGTH_SHORT).show();
        }    }
        });

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
            getListenedBy();
            getLikedBy();
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
                            listened();
                            if (!getFlag() && thread == null) {
                                setFlag(true);
                                thread = new Thread(new podcastPlayer());
                                thread.start();
                            }else if(!getFlag() && thread!=null && thread.getState() == Thread.State.TERMINATED  ) {
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


                        if (!getFlag() && thread == null) {
                            setFlag(true);
                            thread = new Thread(new podcastPlayer());
                            thread.start();
                        }else if(!getFlag() && thread!=null && thread.getState() == Thread.State.TERMINATED  ) {
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
        mediaPlayer.prepareAsync();

        mediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {

            @Override
            public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {
                seekBar.setSecondaryProgress((int) ((i/(double)100)*mediaPlayer.getDuration()));
                buffer= (byte) i;
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
while(!getFlag()){
    try {

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
                seekBar.setProgress(mediaPlayer.getCurrentPosition());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (mediaPlayer!=null)
                        timeLeft.setText(((mediaDuration-mediaPlayer.getCurrentPosition())/60000 )+":"+((mediaDuration-mediaPlayer.getCurrentPosition())%60000)/1000);
                    }
        });
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
    public void listened(){



            db.collection("podcast").whereEqualTo("audioUrl",list.get(index).getAudioUrl()).addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                    if (error==null){
                        if (value!=null){
                            if (!value.isEmpty()) {
                                for (QueryDocumentSnapshot documentSnapshot:value) {
                                    Map<String,Object> ud=new HashMap<>();
                                    ud.put("i",true);
                                    db.collection("podcast").document(documentSnapshot.getId()).collection("ListenedBy").document(user.getUid()).set(ud).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            getListenedBy();
                                        }
                                    });
}                            }
                        }
                    }
                }
            });



    }

    public void setLikeButton(View view) {
        final Map<String,Object> ud=new HashMap<>();
        final boolean[] check = {false};


        final ArrayList<QueryDocumentSnapshot> d=new ArrayList<>();

if (likeCheck){


    db.collection("podcast").whereEqualTo("audioUrl",list.get(index).getAudioUrl()).addSnapshotListener(new EventListener<QuerySnapshot>() {
        @Override
        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

            if (error==null){
                if (value!=null){
                    if (!value.isEmpty()) {
                        for (QueryDocumentSnapshot documentSnapshot:value) {


                            d.add(documentSnapshot);

                            break;
                        }
                        if (!check[0]){



                            ud.put("i",true);
                        db.collection("podcast").document(d.get(0).getId()).collection("LikedBy").document(user.getUid()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                    likeCheck=false;
                                    check[0] =true;

                                    likeButton.setBackgroundColor(Color.WHITE);
                                    d.clear();

                                    getLikedBy();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });}
                    }
                }
            }
        }
    });
}else{


    db.collection("podcast").whereEqualTo("audioUrl",list.get(index).getAudioUrl()).addSnapshotListener(new EventListener<QuerySnapshot>() {
        @Override
        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

            if (error==null){
                if (value!=null){
                    if (!value.isEmpty()) {
                        for (QueryDocumentSnapshot documentSnapshot:value) {


                            d.add(documentSnapshot);

                            break;
                        }
                        if (!check[0]){


                            ud.put("i",true);
                        db.collection("podcast").document(d.get(0).getId()).collection("LikedBy").document(user.getUid()).set(ud).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                    likeCheck=true;
                                    check[0] =true;

                                    likeButton.setBackgroundColor(Color.RED);
                                    d.clear();

                                    getLikedBy();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });}
                    }
                }
            }
        }
    });
}

    }
    public void getLikedBy(){
        final boolean[] check = {false};


        final ArrayList<QueryDocumentSnapshot> d=new ArrayList<>();




            db.collection("podcast").whereEqualTo("audioUrl",list.get(index).getAudioUrl()).addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                    if (error==null){
                        if (value!=null){
                            if (!value.isEmpty()) {
                                for (QueryDocumentSnapshot documentSnapshot:value) {


                                    d.add(documentSnapshot);

                                    break;
                                }
                                if (!check[0]){



                                    Source source =Source.SERVER;
                                    db.collection("podcast").document(d.get(0).getId()).collection("LikedBy").get(source).addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                        @Override
                                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                            check[0]=true;
                                            for (QueryDocumentSnapshot document:queryDocumentSnapshots){
                                                if (document.getId().equals(user.getUid())){
                                                    likeCheck=true;


                                                    likeButton.setBackgroundColor(Color.RED);

                                                }


                                            }
                                            LikedByCount=queryDocumentSnapshots.size();
                                            likedBy.setText(getString(R.string.likedby)+LikedByCount);


                                            d.clear();
                                        }
                                    });


                                }
                            }
                        }
                    }
                }
            });

    }
    public void getListenedBy(){
        final boolean[] check = {false};


        final ArrayList<QueryDocumentSnapshot> d=new ArrayList<>();




        db.collection("podcast").whereEqualTo("audioUrl",list.get(index).getAudioUrl()).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                if (error==null){
                    if (value!=null){
                        if (!value.isEmpty()) {
                            for (QueryDocumentSnapshot documentSnapshot:value) {


                                d.add(documentSnapshot);

                                break;
                            }
                            if (!check[0]){

                                Source source=Source.SERVER;

                                db.collection("podcast").document(d.get(0).getId()).collection("ListenedBy").get(source).addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                        check[0]=true;


                                        listenedByCount=queryDocumentSnapshots.size();
                                        listenedby.setText(getString(R.string.listenedby)+listenedByCount);

                                        d.clear();
                                    }
                                });

                            }
                        }
                    }
                }
            }
        });

    }

}
