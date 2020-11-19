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

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class upcomingLiveEvents_Activity extends AppCompatActivity implements recyclerv.onItemClickListener {
    private recyclerv adapter;
    private RecyclerView recyclerView;
    private List<model> eventList;

    private ActionBarDrawerToggle toggle;
    private DrawerLayout drawerLayout;

    private NavigationView navigationView;
    private Button signout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upcoming_liveevents);

        Toolbar toolbar = findViewById(R.id.toolbar_2);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        signout = findViewById(R.id.signout_button);
        drawerLayout = findViewById(R.id.drawerLayout_ULE);
        toggle = new ActionBarDrawerToggle(upcomingLiveEvents_Activity.this, drawerLayout, R.string.Drawer_open, R.string.Drawer_close);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nv_2);

        //View v = navigationView.inflateHeaderView(R.layout.drawerheader);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.Home: startActivity(new Intent(upcomingLiveEvents_Activity.this, home_Activity.class));
                        break;
                    case R.id.liveEvents: startActivity(new Intent(upcomingLiveEvents_Activity.this, upcomingLiveEvents_Activity.class));
                        break;
                    case R.id.talks: startActivity(new Intent(upcomingLiveEvents_Activity.this, upcomingLiveEvents_Activity.class));
                        break;
                    case R.id.interviews: startActivity(new Intent(upcomingLiveEvents_Activity.this, interviewsActivity.class));
                        break;
                    case R.id.podcasts: startActivity(new Intent(upcomingLiveEvents_Activity.this, upcomingLiveEvents_Activity.class));
                        break;
                }
                return false;
            }
        });
        eventList = new ArrayList<>();
        eventList.clear();
        model e1 = new model();
        e1.setEventName("Test name");
        e1.setDate("9 September 2020");
        e1.setSpeakerName("Tarun");
        e1.setTime("2:00 P.M. - 2:45 P.M. (IST)");
        model e2 = new model();
        e2.setEventName("Test name");
        e2.setDate("9 September, 2020");
        e2.setSpeakerName("Tarun");
        model e3 = new model();
        e3.setEventName("Test name");
        e3.setDate("9 September, 2020");
        e3.setSpeakerName("Tarun");
        model e4 = new model();
        e4.setEventName("Test name");
        e4.setDate("9 September, 2020");
        e4.setSpeakerName("Tarun");
        model e5 = new model();
        e5.setEventName("Test name");
        e5.setDate("9 September, 2020");
        e5.setSpeakerName("Tarun");
        eventList.add(e1);
        eventList.add(e2);
        eventList.add(e3);
        eventList.add(e4);
        eventList.add(e5);
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new recyclerv(eventList, this);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(upcomingLiveEvents_Activity.this, "ok", Toast.LENGTH_SHORT).show();
            }
        });
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
        Intent intent = new Intent(upcomingLiveEvents_Activity.this, event_detail_activity.class);
        intent.putExtra("EventTopic", eventList.get(position).getEventName().trim());
        intent.putExtra("EventDescription", eventList.get(position).getEventDescription());
        intent.putExtra("EventSpeaker", eventList.get(position).getSpeakerName());
        intent.putExtra("EventDate", eventList.get(position).getDate());
        intent.putExtra("EventTime", eventList.get(position).getDuration());
        // intent.putExtra("EventImageLink",eventList.get(position).getImageUrl().trim());
        intent.putExtra("EventOrganizer", eventList.get(position).getOrganizerName());
        startActivity(intent);
    }
}