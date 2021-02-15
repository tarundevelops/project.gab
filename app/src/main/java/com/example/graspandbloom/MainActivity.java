package com.example.graspandbloom;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class MainActivity extends AppCompatActivity {
private SignInButton start;
    private GoogleSignInClient mGoogleSignInClient;
    private String TAG ="MainActivity";
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    String user_id;
    private int RC_SIGN_IN = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        start = findViewById(R.id.sign_in_button);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        if(FirebaseAuth.getInstance().getCurrentUser()!=null)
        {
            Intent i = new Intent(MainActivity.this,podcast_Activity.class);
            startActivity(i);
            this.finish();
        }
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this,gso);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            signIn();

            }
        });
   }
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode != RESULT_CANCELED) {
            if (requestCode == RC_SIGN_IN)
            {
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                handleSignInResult(task);
            }
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> task) {
        try{
            GoogleSignInAccount acc = task.getResult(ApiException.class);
            Toast.makeText(getApplicationContext(),"Signing Success",Toast.LENGTH_SHORT).show();
            FirebaseGoogleAuth(acc);
        }catch(ApiException e)
        {
            Toast.makeText(getApplicationContext(),"Signing Failed",Toast.LENGTH_SHORT).show();
            // FirebaseGoogleAuth(null);
        }
    }

    private void FirebaseGoogleAuth(GoogleSignInAccount acc) {
        AuthCredential authCredential = GoogleAuthProvider.getCredential(acc.getIdToken(),null);
        mAuth.signInWithCredential(authCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(MainActivity.this,"Successful",Toast.LENGTH_SHORT).show();
                    FirebaseUser user = mAuth.getCurrentUser();
                    updateUI(user);



                }
                else
                {
                    Toast.makeText(MainActivity.this,"Failed",Toast.LENGTH_SHORT).show();
                    //   updateUI(null);
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void updateUI(FirebaseUser user) {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        if(account!=null)
        {    user_id = Objects.requireNonNull(user).getUid();
            String personName = account.getDisplayName();
            Uri img_uri = account.getPhotoUrl();
            String uri = img_uri.toString();
            String personEmail = account.getEmail();
            String personId = account.getId();
            Map<String, Object> users = new HashMap<>();
          users.put("User", new User(personName,personEmail,personId,uri));
            db.collection("UserDetails")
                    .add(users)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {

                        Intent i = new Intent(MainActivity.this,podcast_Activity.class);
                            startActivity(i);

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });




            //  Toast.makeText(this, personName+" "+personEmail, Toast.LENGTH_SHORT).show();
        }
    }

}