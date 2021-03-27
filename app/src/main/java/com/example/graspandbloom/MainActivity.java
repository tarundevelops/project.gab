package com.example.graspandbloom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = FirebaseFirestore.getInstance();
        MobileAds.initialize(this);



        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            final String personEmail = user.getEmail();
            db.collection("UserDetails").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    int c = 0;
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        if (documentSnapshot.get("personEmail") != null) {
                            if (documentSnapshot.get("personEmail").equals(personEmail)) {
                                c++;
                            }
                        }
                    }
                    if (c == 1) {
                        Intent i = new Intent(MainActivity.this, podcast_Activity.class);
                        startActivity(i);

                        MainActivity.this.finish();
                    } else if (c == 0) {
                        String personName = user.getDisplayName();
                        Uri img_uri = user.getPhotoUrl();
                        String uri = img_uri.toString();
                        String personUid = user.getUid();
                        Map<String, String> newUser = new HashMap<>();
                        newUser.put("personName", personName);
                        newUser.put("personEmail", personEmail);
                        newUser.put("personUid", personUid);
                        newUser.put("img_uri", uri);


                        db.collection("UserDetails")
                                .add(newUser)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        Intent i = new Intent(MainActivity.this, podcast_Activity.class);
                                        startActivity(i);

                                        MainActivity.this.finish();

                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                    }
                                });
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });


        }else {
            Intent i = new Intent(MainActivity.this,SignIn_Activity.class);
            startActivity(i);

            MainActivity.this.finish();
        }

    }}

