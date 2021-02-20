package com.example.graspandbloom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.util.Objects;

public class contact_activity extends AppCompatActivity {
    private ActionBarDrawerToggle toggle;
    private DrawerLayout drawerLayout;

    private NavigationView navigationView;
    private Button signout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_activity);
        Toolbar toolbar = findViewById(R.id.toolbar_1);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        signout = findViewById(R.id.signout_button);
        drawerLayout = findViewById(R.id.drawerLayout_CA);
        toggle = new ActionBarDrawerToggle(contact_activity.this, drawerLayout, R.string.Drawer_open, R.string.Drawer_close);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nv_1);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.Home: startActivity(new Intent(contact_activity.this, podcast_Activity.class));
                        break;
                    case R.id.contactUs: startActivity(new Intent(contact_activity.this, contact_activity.class));
                        break;
                    case R.id.talks: startActivity(new Intent(contact_activity.this, podcast_Activity.class));
                        break;
                    case R.id.interviews: startActivity(new Intent(contact_activity.this, podcast_Activity.class));
                        break;
                    case R.id.podcasts: startActivity(new Intent(contact_activity.this, podcast_Activity.class));
                        break;
                }
                return false;
            }
        });





        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(contact_activity.this, "ok", Toast.LENGTH_SHORT).show();
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
}