package com.example.graspandbloom;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
private Button  start;
    int AUTH_REQUEST_CODE = 1000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (FirebaseAuth.getInstance().getCurrentUser() !=null)
        {
            startActivity (new Intent(this, Login.class));
            this.finish();
        }




        start = findViewById(R.id.startButton);





        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleLoginRegister();
            }
        });
    }
    public void handleLoginRegister() {
        List<AuthUI.IdpConfig> provider = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build()

        );
        Intent intent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(provider)
                .setTosAndPrivacyPolicyUrls("https://example.com","https://example.com")

                .build();
        startActivityForResult(intent,AUTH_REQUEST_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==AUTH_REQUEST_CODE)
        {
            if(resultCode==RESULT_OK) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                if (user.getMetadata().getCreationTimestamp() == user.getMetadata().getLastSignInTimestamp()) {
                    Toast.makeText(this, "Welcome new User", Toast.LENGTH_SHORT).show();

                }
                else
                {
                    Toast.makeText(this, "Welcome Back", Toast.LENGTH_SHORT).show();
                }
                Intent intent = new Intent(this,Login.class);
                startActivity(intent);
                this.finish();

            }
            else
            {
                IdpResponse response = IdpResponse.fromResultIntent(data);
                if(response == null)
                {
                    Log.d(TAG, "onActivityResult: the user has cancelled the sign in request" );
                }
                else
                {
                    Log.e(TAG,"onActivityResult: ", response.getError());
                }
            }
        }
    }

}

