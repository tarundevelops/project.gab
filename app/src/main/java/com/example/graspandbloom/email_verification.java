package com.example.graspandbloom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

public class email_verification extends AppCompatActivity {
private EditText etEmail;
private Button btnSendEmail, btnNext;
private TextView tvMessage;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_verification);
        etEmail = findViewById(R.id.etEmail);
        btnSendEmail = findViewById(R.id.btnSendEmail);
        btnNext = findViewById(R.id.btnNext);
        tvMessage = findViewById(R.id.tvMessage);
        tvMessage.setVisibility(View.INVISIBLE);
        mAuth = FirebaseAuth.getInstance();
        btnSendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString();
                if(email.length()== 0)
                {
                    Toast.makeText(email_verification.this, "Please enter Email", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    sendVerificationEmail(email);
                }
            }
        });
    }

    private void sendVerificationEmail(String email) {
   user = mAuth.getCurrentUser();
  user.sendEmailVerification()
          .addOnSuccessListener(new OnSuccessListener<Void>() {
              @Override
              public void onSuccess(Void aVoid) {
                  tvMessage.setVisibility(View.VISIBLE);
              }
          }).addOnFailureListener(new OnFailureListener() {
      @Override
      public void onFailure(@NonNull Exception e) {
          Toast.makeText(email_verification.this, "Retry", Toast.LENGTH_SHORT).show();
      }
  });

    }
}