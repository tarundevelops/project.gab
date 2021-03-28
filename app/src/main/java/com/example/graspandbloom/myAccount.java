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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class myAccount extends AppCompatActivity {
FirebaseUser currentuser;
TextView userName,emailAddres;

FirebaseFirestore db;
    private ActionBarDrawerToggle toggle;
    private DrawerLayout drawerLayout;

    private NavigationView navigationView;
    private Button signout;

ImageView userImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);
        Toolbar toolbar = findViewById(R.id.toolbar_myAccnt);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        signout = findViewById(R.id.signout_button_myAccnt);
        drawerLayout = findViewById(R.id.drawerLayout_myAccnt);
        toggle = new ActionBarDrawerToggle(myAccount.this, drawerLayout, R.string.Drawer_open, R.string.Drawer_close);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nv_myAccnt);

        //View v = navigationView.inflateHeaderView(R.layout.drawerheader);


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.myAccount_id: startActivity(new Intent(myAccount.this, myAccount.class));
                    myAccount.this.finish();
                        break;
                    case R.id.contactUs: startActivity(new Intent(myAccount.this, contact_activity.class));
                    myAccount.this.finish();
                        break;

                    case R.id.podcasts: startActivity(new Intent(myAccount.this, podcast_Activity.class));
                    myAccount.this.finish();
                        break;
                }
                return false;
            }
        });


        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(myAccount.this, "ok", Toast.LENGTH_SHORT).show();
                FirebaseAuth auth=FirebaseAuth.getInstance();
                auth.signOut();
                Intent i = new Intent(myAccount.this,MainActivity.class);
                startActivity(i);
                myAccount.this.finish();

            }
        });
        currentuser= FirebaseAuth.getInstance().getCurrentUser();
        userName=findViewById(R.id.username);
        emailAddres=findViewById(R.id.emailaddress);
        userImage=findViewById(R.id.useriamge);
        db=FirebaseFirestore.getInstance();

        Picasso.get().load(currentuser.getPhotoUrl()).into(userImage);

        userName.setText("Name :"+currentuser.getDisplayName());
        emailAddres.setText("Email address :"+currentuser.getEmail());




    }

    public void deleteAccount(View view) {
        db.collection("UserDetails").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                for (QueryDocumentSnapshot documentSnapshot:queryDocumentSnapshots) {
                    if (documentSnapshot.get("personEmail").equals(currentuser.getEmail())){
                        db.collection("UserDetails").document(documentSnapshot.getReference().getId()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                currentuser.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        FirebaseAuth auth=FirebaseAuth.getInstance();
                                        auth.signOut();
                                        Intent i = new Intent(myAccount.this,MainActivity.class);
                                        startActivity(i);
                                        Toast.makeText(myAccount.this, "Account deleted successfully", Toast.LENGTH_SHORT).show();
                                        myAccount.this.finish();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(myAccount.this, "Failed to delete account. \n Please restart the app and try again to delete account.", Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                Toast.makeText(myAccount.this, "Failed to delete account. \n Please try again.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(myAccount.this, "Failed to delete account. \n Please try again.", Toast.LENGTH_SHORT).show();
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