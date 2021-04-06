package com.example.graspandbloom;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.android.ump.ConsentDebugSettings;
import com.google.android.ump.ConsentForm;
import com.google.android.ump.ConsentInformation;
import com.google.android.ump.ConsentRequestParameters;
import com.google.android.ump.FormError;
import com.google.android.ump.UserMessagingPlatform;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class podcast_Activity extends AppCompatActivity implements recyclerv.onItemClickListener {
    private recyclerv adapter;
    private RecyclerView recyclerView;
    private static ArrayList<PodcastModel> podcastList = new ArrayList<>();
    private ActionBarDrawerToggle toggle;
    private DrawerLayout drawerLayout;

    private NavigationView navigationView;
    private Button signout;
    private ConsentInformation ci;

    private ConsentForm c;

    private FirebaseFirestore db = FirebaseFirestore.getInstance(); // TestMode

    private CollectionReference podcastDB = db.collection("podcast");

    private ImageView podcastImage;

   private AdView adView;
   private Parcelable listState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_podcast);

        ConsentDebugSettings debugSettings = new ConsentDebugSettings.Builder(this).setDebugGeography(ConsentDebugSettings.DebugGeography.DEBUG_GEOGRAPHY_EEA).setForceTesting(true).build();

        ConsentRequestParameters param = new ConsentRequestParameters.Builder().setConsentDebugSettings(debugSettings).setTagForUnderAgeOfConsent(false).build();
        ci = UserMessagingPlatform.getConsentInformation(this);
        ci.requestConsentInfoUpdate(this, param, new ConsentInformation.OnConsentInfoUpdateSuccessListener() {
            @Override
            public void onConsentInfoUpdateSuccess() {


                Log.d("ConsentStatus0", (ci.isConsentFormAvailable())+""+ci.getConsentStatus());
if(ci.isConsentFormAvailable()){
                loadConsent();}else if(ci.getConsentStatus() == ConsentInformation.ConsentStatus.NOT_REQUIRED) {
    MobileAds.initialize(podcast_Activity.this);
     adView = findViewById(R.id.adView);


    AdRequest adRequest =new AdRequest.Builder().build();
    adRequest.isTestDevice(podcast_Activity.this);
    adView.loadAd(adRequest);

}

            }
        }, new ConsentInformation.OnConsentInfoUpdateFailureListener() {
            @Override
            public void onConsentInfoUpdateFailure(FormError formError) {

            }
        });



        Toolbar toolbar = findViewById(R.id.toolbar_2);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        signout = findViewById(R.id.signout_button);
        drawerLayout = findViewById(R.id.drawerLayout_ULE);
        toggle = new ActionBarDrawerToggle(podcast_Activity.this, drawerLayout, R.string.Drawer_open, R.string.Drawer_close);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nv_2);
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        podcastImage = findViewById(R.id.imageView);

        //View v = navigationView.inflateHeaderView(R.layout.drawerheader);


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.myAccount_id: startActivity(new Intent(podcast_Activity.this, myAccount.class));
                    podcast_Activity.this.finish();
                        break;
                    case R.id.contactUs: startActivity(new Intent(podcast_Activity.this, contact_activity.class));
                    podcast_Activity.this.finish();
                        break;
                    case R.id.podcasts: startActivity(new Intent(podcast_Activity.this, podcast_Activity.class));
                    podcast_Activity.this.finish();
                        break;
                }
                return false;
            }
        });


        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(podcast_Activity.this, "ok", Toast.LENGTH_SHORT).show();
                FirebaseAuth auth=FirebaseAuth.getInstance();
                auth.signOut();
                Intent i = new Intent(podcast_Activity.this,SignIn_Activity.class);
                startActivity(i);
                podcast_Activity.this.finish();
            }
        });


        db.collection("podcastImage").document("Image").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String url="";
                if ((url= (String) documentSnapshot.get("imageUrl"))!=null){
                    if (!url.isEmpty())
                        Picasso.get().load(url).fit().into(podcastImage);
                }
            }
        });
    }
    public void loadConsent() {


        UserMessagingPlatform.loadConsentForm(this, new UserMessagingPlatform.OnConsentFormLoadSuccessListener() {
            @Override
            public void onConsentFormLoadSuccess(ConsentForm consentForm) {
                podcast_Activity.this.c = consentForm;
                Log.d("ConsentStatus", (c)+"");
                Log.d("ConsentStatus1", (ci.getConsentStatus() == ConsentInformation.ConsentStatus.REQUIRED)+"");

                if (ci.getConsentStatus() == ConsentInformation.ConsentStatus.REQUIRED) {

                    c.show(podcast_Activity.this, new ConsentForm.OnConsentFormDismissedListener() {
                        @Override
                        public void onConsentFormDismissed(@Nullable FormError formError) {

                                MobileAds.initialize(podcast_Activity.this);
                                AdView adView = findViewById(R.id.adView);


                                AdRequest adRequest =new AdRequest.Builder().build();
                                adRequest.isTestDevice(podcast_Activity.this);
                                adView.loadAd(adRequest);

                               // ci.reset();

                            //loadConsent();

                        }
                    });
                }else if(ci.getConsentStatus() == ConsentInformation.ConsentStatus.NOT_REQUIRED){
                    MobileAds.initialize(podcast_Activity.this);
                    AdView adView = findViewById(R.id.adView);


                    AdRequest adRequest =new AdRequest.Builder().build();
                    adRequest.isTestDevice(podcast_Activity.this);
                    adView.loadAd(adRequest);
                }
            }
        }, new UserMessagingPlatform.OnConsentFormLoadFailureListener() {
            @Override
            public void onConsentFormLoadFailure(FormError formError) {

            }
        });


    }


    @Override
    protected void onStart() {
        super.onStart();
        podcastList.clear();
        podcastDB.orderBy("orderBy", Query.Direction.DESCENDING).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots != null && queryDocumentSnapshots.size() > 0){
                    for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots){
                        PodcastModel m = queryDocumentSnapshot.toObject(PodcastModel.class);

                        podcastList.add(m);
                    }

                    setAdapter(podcastList);
                    if(listState!=null){
                        recyclerView.getLayoutManager().onRestoreInstanceState(listState);
                    }

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(podcast_Activity.this, "Error occured", Toast.LENGTH_SHORT).show();
            }
        });



    }

    private void setAdapter(List<PodcastModel> pL) {
        adapter = new recyclerv(pL, this);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public static ArrayList<PodcastModel> getPodcastList()
    {
        return podcastList;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void itemClick(int position) {
       Intent intent = new Intent(podcast_Activity.this, podcastPlayer.class);
       intent.putExtra("index",position);
        listState= recyclerView.getLayoutManager().onSaveInstanceState();
       startActivity(intent);

    }



    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(ci!=null){
            ci.reset();
        }

        if(adView!=null){
            adView.destroy();
        }


    }
}