package com.example.graspandbloom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class create_Account extends AppCompatActivity {
private EditText name, city_name, country_name,email,password, confPassword;
private Button btnCreate;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseUser user;
    private String user_name, user_email, user_city,user_country,user_password,conf_password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create__account);
        name = findViewById(R.id.userFullName);
        city_name = findViewById(R.id.userCityName);
        country_name = findViewById(R.id.userCountryName);
        email = findViewById(R.id.userEmailAddress);
        password = findViewById(R.id.userAccountPassword);
        confPassword = findViewById(R.id.userAccountConfirmPassword);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        btnCreate = findViewById(R.id.confirmCreateAccountButton);
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user_name = name.getText().toString().trim();
                user_city = city_name.getText().toString().trim();
                user_country = country_name.getText().toString().trim();
            user_email = email.getText().toString().trim();
            user_password = password.getText().toString().trim();
            conf_password = confPassword.getText().toString().trim();
            if(user_country.isEmpty() || user_name.isEmpty() || user_password.isEmpty() || user_email.isEmpty() || user_city.isEmpty())
            {
                Toast.makeText(create_Account.this, "Enter all the details", Toast.LENGTH_SHORT).show();
            }
            else
            {
                if(user_password.equals(conf_password))
                {
                    createAccount(user_email,user_password);
                }
                else
                {
                    Toast.makeText(create_Account.this, "Password didn't match", Toast.LENGTH_SHORT).show();
                }
            }
           
            }
        });
    }

    private void createAccount(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {


                             user = mAuth.getCurrentUser();
                           // saveData();
                          //  Toast.makeText(create_Account.this, "Authentication successful", Toast.LENGTH_SHORT).show();
                            sendVerificationEmail();
                        } else {


                            Toast.makeText(create_Account.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }


                    }
                });
    }

    private void sendVerificationEmail() {
      /*  user.sendEmailVerification()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {


                        if (task.isSuccessful()) {
                            Map<String, Object> users = new HashMap<>();
                            users.put("User", new User(user_name, user_email, user_city, user_country));
                            db.collection("UserDetails")
                                    .add(users)
                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {

                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                        }
                                    });
                            Toast.makeText(create_Account.this,
                                    "Verification email sent to " + user.getEmail(),
                                    Toast.LENGTH_SHORT).show();

                        } else {

                            Toast.makeText(create_Account.this,
                                    "Failed to send verification email.",
                                    Toast.LENGTH_SHORT).show();
                        }


                    }
                });*/
        user.sendEmailVerification()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Map<String, Object> users = new HashMap<>();
                        users.put("User", new User(user_name, user_email, user_city, user_country));
                        db.collection("UserDetails")
                                .add(users)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {

                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                    }
                                });
                        Toast.makeText(create_Account.this,
                                "Verification email sent to " + user.getEmail(),
                                Toast.LENGTH_SHORT).show();

                    }

                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(create_Account.this, "Please enter correct Email", Toast.LENGTH_SHORT).show();
                    }
                });
    }


   /* private void saveData() {
        if(user.isEmailVerified())
        {
            Toast.makeText(this, "Account created successfully", Toast.LENGTH_SHORT).show();
        Map<String, Object> users= new HashMap<>();
       users.put("User",new User(user_name,user_email,user_city,user_country));
        db.collection("UserDetails")
                .add(users)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });}
        else
        {
            Toast.makeText(this, "EMail verfication failed", Toast.LENGTH_SHORT).show();
        }

    }*/
}