package com.example.graspandbloom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;
import java.util.Objects;

public class myAccount extends AppCompatActivity {
public static FirebaseUser currentuser=FirebaseAuth.getInstance().getCurrentUser();
TextView userName,emailAddres;

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
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.black));
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        navigationView = findViewById(R.id.nv_myAccnt);



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
                    case R.id.tnc:
                        Intent openURL = new Intent(android.content.Intent.ACTION_VIEW);
                        openURL.setData(Uri.parse("https://decib.in/terms-and-conditions/"));
                        startActivity(openURL);
                        break;
                    case R.id.privacyPolicy:
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.addCategory(Intent.CATEGORY_BROWSABLE);
                        intent.setData(Uri.parse("https://decib.in/privacy-policy/"));
                        startActivity(intent);
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
                Intent i = new Intent(myAccount.this,SignIn_Activity.class);
                startActivity(i);
                myAccount.this.finish();

            }
        });

        userName=findViewById(R.id.username);
        emailAddres=findViewById(R.id.emailaddress);
        userImage=findViewById(R.id.useriamge);


        Picasso.get().load(currentuser.getPhotoUrl()).into(userImage);

        userName.setText("Name : "+currentuser.getDisplayName());
        emailAddres.setText("Email address : "+currentuser.getEmail());




    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}