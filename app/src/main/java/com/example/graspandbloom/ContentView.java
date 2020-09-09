package com.example.graspandbloom;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class ContentView extends AppCompatActivity {
private recyclerv adapter;
private RecyclerView recyclerView;
private List<model> eventList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_view);
eventList = new ArrayList<>();
        model e1 = new model();
        e1.setEventName("Test name");
        e1.setDate("12");
        e1.setSpeakerName("Tarun");
        e1.setTime("2:00 P.M. - 2:45 P.M. (IST)");
        model e2 = new model();
        e2.setEventName("Test name");
        e2.setDate("9 September, 2020");
        e2.setSpeakerName("Tarun");
        eventList.add(e1);
        eventList.add(e2);
    recyclerView = findViewById(R.id.recyclerview);
    recyclerView.setHasFixedSize(true);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    adapter = new recyclerv(eventList, this);
    recyclerView.setAdapter(adapter);
    adapter.notifyDataSetChanged();
    }
}