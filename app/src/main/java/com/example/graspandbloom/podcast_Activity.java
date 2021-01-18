package com.example.graspandbloom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class podcast_Activity extends AppCompatActivity implements recyclerv.onItemClickListener {
    private recyclerv adapter;
    private RecyclerView recyclerView;
    private List<PodcastModel> podcastList = new ArrayList<>();

    private ActionBarDrawerToggle toggle;
    private DrawerLayout drawerLayout;

    private NavigationView navigationView;
    private Button signout;

    private FirebaseFirestore db = FirebaseFirestore.getInstance(); // TestMode

    private CollectionReference podcastDB = db.collection("podcast");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_podcast);

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

        //View v = navigationView.inflateHeaderView(R.layout.drawerheader);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.Home: startActivity(new Intent(podcast_Activity.this, podcast_Activity.class));
                        break;
                    case R.id.liveEvents: startActivity(new Intent(podcast_Activity.this, podcast_Activity.class));
                        break;
                    case R.id.talks: startActivity(new Intent(podcast_Activity.this, podcast_Activity.class));
                        break;
                    case R.id.interviews: startActivity(new Intent(podcast_Activity.this, podcast_Activity.class));
                        break;
                    case R.id.podcasts: startActivity(new Intent(podcast_Activity.this, podcast_Activity.class));
                        break;
                }
                return false;
            }
        });





        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(podcast_Activity.this, "ok", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        podcastList.clear();
        podcastDB.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots != null && queryDocumentSnapshots.size() > 0){
                    for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots){
                        PodcastModel m = queryDocumentSnapshot.toObject(PodcastModel.class);

                        podcastList.add(m);
                    }

                    setAdapter(podcastList);

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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void itemClick(int position) {
//        Intent intent = new Intent(podcast_Activity.this, event_detail_activity.class);
//        intent.putExtra("EventTopic", eventList.get(position).getTopic().trim());
//        intent.putExtra("EventDescription", eventList.get(position).getEventDescription());
//        intent.putExtra("EventSpeaker", eventList.get(position).getSpeakerName());
//        intent.putExtra("EventDate", eventList.get(position).getDate());
//        intent.putExtra("EventTime", eventList.get(position).getDuration());
//        // intent.putExtra("EventImageLink",eventList.get(position).getImageUrl().trim());
//        intent.putExtra("EventOrganizer", eventList.get(position).getOrganizerName());
//        startActivity(intent);
    }
}