package com.example.restoran;

import static android.text.TextUtils.isEmpty;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.restoran.Fragements.SigninFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetActivity extends AppCompatActivity {

    EditText etEmail;
    Button btnReset;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);

        etEmail = findViewById(R.id.foremail);
        btnReset = findViewById(R.id.btnReset);
        btnReset.setOnClickListener(v -> {
            if (SigninFragment.connectionAvailable(this)){
                String email = etEmail.getText().toString().trim();
                if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    etEmail.setError("Please enter valid email address!");
                    etEmail.requestFocus();
                }
                else{
                    FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                            .addOnSuccessListener(unused -> {
                                Toast.makeText(ForgetActivity.this, "Check your email inbox for reset email link.", Toast.LENGTH_SHORT).show();
                                finish();
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(ForgetActivity.this, "Failed to send reset email link!", Toast.LENGTH_SHORT).show();
                                Log.e("Failed to Send Email Error : ", "onCreate: " + e.getMessage() );
                            });
                }
            }
            else{
                Toast.makeText(ForgetActivity.this, "No internet connection!", Toast.LENGTH_SHORT).show();
            }

        });

    }
}