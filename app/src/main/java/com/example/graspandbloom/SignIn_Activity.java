package com.example.graspandbloom;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;


public class SignIn_Activity extends AppCompatActivity {
    private SignInButton start;
    private GoogleSignInClient mGoogleSignInClient;
    private String TAG ="MainActivity";
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private int RC_SIGN_IN = 1;
    AlertDialog.Builder builder;
    AlertDialog dialog;
    private ProgressBar pb;
    private TextView textView2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_);
        start = findViewById(R.id.sign_in_button);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        pb=findViewById(R.id.pbId);
        textView2 = findViewById(R.id.textView2);

       String message = "By signing in, you are accepting our Terms and Conditions and Privacy Policy.";
        SpannableString spannable1 = new SpannableString(message);
        spannable1.setSpan(new ForegroundColorSpan(
                        ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary)),
                37, 57, 0);
        ClickableSpan clickableSpan1 = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View textView) {
                Intent openURL = new Intent(android.content.Intent.ACTION_VIEW);
                openURL.setData(Uri.parse("https://decib.in/terms-and-conditions/"));
                startActivity(openURL);
            }
        };
        spannable1.setSpan(clickableSpan1,37,57,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);


        SpannableString spannable2 = new SpannableString(spannable1);
        spannable2.setSpan(new ForegroundColorSpan(
                        ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary)),
                61, 76, 0);
        ClickableSpan clickableSpan2 = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View textView) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("https://decib.in/privacy-policy/"));
                startActivity(intent);
            }
        };
        spannable2.setSpan(clickableSpan2,61,76, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
       textView2.setText(spannable2);
       textView2.setMovementMethod(LinkMovementMethod.getInstance());

        builder=new AlertDialog.Builder(this);
        View view =getLayoutInflater().inflate(R.layout.privacy_policy_terms_conditions,null);

        builder.setView(view);
        dialog=builder.create();
        dialog.setCanceledOnTouchOutside(false);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this,gso);


        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.show();



            }
        });
    }
    public void accepted(View view){
        dialog.dismiss();
        signIn();
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
                pb.setVisibility(View.VISIBLE);
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                handleSignInResult(task);
            }
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> task) {
        try{
            final GoogleSignInAccount acc = task.getResult(ApiException.class);
            Toast.makeText(getApplicationContext(),"Signing Success",Toast.LENGTH_SHORT).show();
            FirebaseGoogleAuth(acc);

        }catch(ApiException e)
        {
            Toast.makeText(getApplicationContext(),"Signing Failed",Toast.LENGTH_SHORT).show();
            pb.setVisibility(View.INVISIBLE);
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
                    Toast.makeText(SignIn_Activity.this,"Successful",Toast.LENGTH_SHORT).show();
                    FirebaseUser user = mAuth.getCurrentUser();
                    updateUI(user);



                }
                else
                {
                    Toast.makeText(SignIn_Activity.this,"Failed",Toast.LENGTH_SHORT).show();
                    pb.setVisibility(View.INVISIBLE);
                    //   updateUI(null);
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void updateUI(final FirebaseUser user) {
        final GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        if(account!=null)
        {
            final String personEmail = account.getEmail();
            db.collection("UserDetails").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    int c=0;
                    for (QueryDocumentSnapshot documentSnapshot:queryDocumentSnapshots) {
                        if (documentSnapshot.get("personEmail")!=null){
                            if (documentSnapshot.get("personEmail").equals(personEmail)){
                                c++;
                            }}
                    }
                    if (c==1){
                        Intent i = new Intent(SignIn_Activity.this,podcast_Activity.class);

                        pb.setVisibility(View.INVISIBLE);
                        startActivity(i);

                        SignIn_Activity.this.finish();
                    }
                    else if (c==0){
                        String personName = account.getDisplayName();
                        Uri img_uri = account.getPhotoUrl();
                        String uri = img_uri.toString();
                        String personUid = user.getUid();
                        Map<String, String> newUser = new HashMap<>();
                        newUser.put("personName",personName);
                        newUser.put("personEmail",personEmail);
                        newUser.put("personUid",personUid);
                        newUser.put("img_uri",uri);


                        db.collection("UserDetails")
                                .add(newUser)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        Intent i = new Intent(SignIn_Activity.this,podcast_Activity.class);
                                        pb.setVisibility(View.INVISIBLE);

                                        startActivity(i);

                                        SignIn_Activity.this.finish();

                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        pb.setVisibility(View.INVISIBLE);
                                    }
                                });}
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    pb.setVisibility(View.INVISIBLE);
                }
            });



            //  Toast.makeText(this, personName+" "+personEmail, Toast.LENGTH_SHORT).show();
        }
    }

    public void declined(View view) {

        dialog.dismiss();
    }
}