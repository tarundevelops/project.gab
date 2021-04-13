package com.example.graspandbloom;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
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
import com.google.firebase.firestore.Source;
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
    private Button signout,tryAgain;
    private ConsentInformation ci;


    private ConsentForm c;

    private FirebaseFirestore db = FirebaseFirestore.getInstance(); // TestMode

    private CollectionReference podcastDB = db.collection("podcast");

    private ImageView podcastImage;

   private AdView adView;
   private Parcelable listState;
   private Boolean iCheck =true;
    private boolean firstCheck =false;
    private ConnectivityManager cm;
    private ConnectivityManager.NetworkCallback networkCallback;
private Source source = Source.SERVER;
private TextView itext;
    private boolean hasRestarted=false;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_podcast);


         cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
         networkCallback =new ConnectivityManager.NetworkCallback(){
            @Override
            public void onAvailable(@NonNull Network network) {
                super.onAvailable(network);
                firstCheck=true;
                if(!iCheck){
               Toast.makeText(podcast_Activity.this, "Internet Available", Toast.LENGTH_SHORT).show();
                iCheck=true;
                }

            }

             @Override
             public void onLost(@NonNull Network network) {
                 super.onLost(network);
                 firstCheck=false;

                 runOnUiThread(new Runnable() {
                     @Override
                     public void run() {
                         if(tryAgain.getVisibility() != View.VISIBLE){
                         recyclerView.setVisibility(View.GONE);
                         itext.setVisibility(View.VISIBLE);
                         tryAgain.setVisibility(View.VISIBLE);}
                     }
                 });
             }
         };
         cm.registerDefaultNetworkCallback(networkCallback);





        ConsentRequestParameters param = new ConsentRequestParameters.Builder().setTagForUnderAgeOfConsent(false).build();
        ci = UserMessagingPlatform.getConsentInformation(this);
        ci.requestConsentInfoUpdate(this, param, new ConsentInformation.OnConsentInfoUpdateSuccessListener() {
            @Override
            public void onConsentInfoUpdateSuccess() {


if(ci.isConsentFormAvailable()){
                loadConsent();}else if(ci.getConsentStatus() == ConsentInformation.ConsentStatus.NOT_REQUIRED) {
    MobileAds.initialize(podcast_Activity.this);
    adView = findViewById(R.id.adView);
    Bundle networkExtrasBundle = new Bundle();
    networkExtrasBundle.putInt("rdp", 1);
    AdRequest request = new AdRequest.Builder()
            .addNetworkExtrasBundle(AdMobAdapter.class, networkExtrasBundle)
            .build();
    SharedPreferences sharedPref = podcast_Activity.this.getPreferences(Context.MODE_PRIVATE);
    SharedPreferences.Editor editor = sharedPref.edit();
    editor.putInt("gad_rdp", 1);
    editor.commit();


    request.isTestDevice(podcast_Activity.this);
    adView.loadAd(request);

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
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.black));
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nv_2);
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        podcastImage = findViewById(R.id.imageView);
        itext = findViewById(R.id.iu);
        tryAgain =findViewById(R.id.tryAgainButton);





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
                    case R.id.tnc:
                        Intent openURL = new Intent(android.content.Intent.ACTION_VIEW);
                        openURL.setData(Uri.parse("https://decib.in/terms-and-conditions/"));
                        startActivity(openURL);
                        hasRestarted=true;
                        break;
                    case R.id.privacyPolicy:
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.addCategory(Intent.CATEGORY_BROWSABLE);
                        intent.setData(Uri.parse("https://decib.in/privacy-policy/"));
                        startActivity(intent);
                        hasRestarted=true;
                        break;
                }
                return false;
            }
        });


        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseAuth auth=FirebaseAuth.getInstance();
                auth.signOut();
                Intent i = new Intent(podcast_Activity.this,SignIn_Activity.class);
                startActivity(i);
                podcast_Activity.this.finish();
            }
        });

db.collection("Notice").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
    @Override
    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
   for(QueryDocumentSnapshot snapshot:queryDocumentSnapshots){
       String displayNotice = (String) snapshot.get("notice");
       if(displayNotice!=null){
           if(!displayNotice.isEmpty()){
             Snackbar.make(podcast_Activity.this,podcast_Activity.this.findViewById(R.id.imageView),displayNotice,10000).show();


           }
       }
   }
    }
});

    }
    public void loadConsent() {


        UserMessagingPlatform.loadConsentForm(this, new UserMessagingPlatform.OnConsentFormLoadSuccessListener() {
            @Override
            public void onConsentFormLoadSuccess(ConsentForm consentForm) {
                podcast_Activity.this.c = consentForm;


                if (ci.getConsentStatus() == ConsentInformation.ConsentStatus.REQUIRED) {

                    c.show(podcast_Activity.this, new ConsentForm.OnConsentFormDismissedListener() {
                        @Override
                        public void onConsentFormDismissed(@Nullable FormError formError) {

                                MobileAds.initialize(podcast_Activity.this);
                            adView = findViewById(R.id.adView);

                            Bundle networkExtrasBundle = new Bundle();
                            networkExtrasBundle.putInt("rdp", 1);
                            AdRequest request = new AdRequest.Builder()
                                    .addNetworkExtrasBundle(AdMobAdapter.class, networkExtrasBundle)
                                    .build();
                            SharedPreferences sharedPref = podcast_Activity.this.getPreferences(Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putInt("gad_rdp", 1);
                            editor.commit();


                            request.isTestDevice(podcast_Activity.this);
                            adView.loadAd(request);




                        }
                    });
                }else if(ci.getConsentStatus() == ConsentInformation.ConsentStatus.NOT_REQUIRED){
                    MobileAds.initialize(podcast_Activity.this);
                    adView = findViewById(R.id.adView);
                    Bundle networkExtrasBundle = new Bundle();
                    networkExtrasBundle.putInt("rdp", 1);
                    AdRequest request = new AdRequest.Builder()
                            .addNetworkExtrasBundle(AdMobAdapter.class, networkExtrasBundle)
                            .build();
                    SharedPreferences sharedPref = podcast_Activity.this.getPreferences(Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putInt("gad_rdp", 1);
                    editor.commit();


                    request.isTestDevice(podcast_Activity.this);
                    adView.loadAd(request);

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


   listPosterLoad();


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
        if(firstCheck){
       Intent intent = new Intent(podcast_Activity.this, podcastPlayer.class);
       intent.putExtra("index",position);
        listState= Objects.requireNonNull(recyclerView.getLayoutManager()).onSaveInstanceState();
       startActivity(intent);
       hasRestarted=true;
        }


    }



    @Override
    protected void onPause() {
        super.onPause();
        if(cm!=null && networkCallback!=null && !hasRestarted ){
            try{
cm.unregisterNetworkCallback(networkCallback);}catch (Exception e){


            }
      }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(ci!=null){

            ci.reset();
            ci=null;
        }

        if(adView!=null){
            adView.destroy();
      }
        if(cm!=null && networkCallback!=null){
            try{
            cm.unregisterNetworkCallback(networkCallback);}catch (Exception e){

            }
        }


    }

    public void podcastLoad(View view) {
        if(firstCheck) {
            Intent load = new Intent(podcast_Activity.this, podcast_Activity.class);
            startActivity(load);

            podcast_Activity.this.finish();
        }
    }

    public void listPosterLoad(){
        if(firstCheck) {

            recyclerView.setVisibility(View.VISIBLE);
            db.collection("podcastImage").document("Image").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    String url = "";
                    if ((url = (String) documentSnapshot.get("imageUrl")) != null) {
                        if (!url.isEmpty())
                            Picasso.get().load(url).fit().into(podcastImage);
                    }
                }
            });

            podcastList.clear();
            podcastDB.orderBy("orderBy", Query.Direction.DESCENDING).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    if (queryDocumentSnapshots != null && queryDocumentSnapshots.size() > 0) {
                        for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
                            PodcastModel m = queryDocumentSnapshot.toObject(PodcastModel.class);

                            podcastList.add(m);
                        }

                        setAdapter(podcastList);
                        if (listState != null) {
                            Objects.requireNonNull(recyclerView.getLayoutManager()).onRestoreInstanceState(listState);
                        }

                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(podcast_Activity.this, "Problem occurred", Toast.LENGTH_SHORT).show();
                }
            });

        }else {
           if(tryAgain.getVisibility() != View.VISIBLE){
            recyclerView.setVisibility(View.GONE);
            itext.setVisibility(View.VISIBLE);
            tryAgain.setVisibility(View.VISIBLE);}
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
hasRestarted=false;

    }
}