package com.example.graspandbloom;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;



public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (FirebaseAuth.getInstance().getCurrentUser() != null) {

            Intent i1 = new Intent(MainActivity.this,podcast_Activity.class);
            startActivity(i1);

            MainActivity.this.finish();


        }else {
            Intent i2 = new Intent(MainActivity.this,SignIn_Activity.class);
            startActivity(i2);

            MainActivity.this.finish();
        }




    }


}

