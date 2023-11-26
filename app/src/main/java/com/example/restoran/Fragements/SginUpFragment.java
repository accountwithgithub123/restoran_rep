package com.example.restoran.Fragements;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.PatternMatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.restoran.LoginRegister;
import com.example.restoran.MainActivity;
import com.example.restoran.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class SginUpFragment extends Fragment {

    Button btnSignin, signUp;
    EditText etemail,etpass,etpassConf;

    LoginRegister activity;
    public SginUpFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_sgin_up, container, false);
        initializer(view);
        onClicks();
        return view;
    }

    public void setAcitivity(LoginRegister activity) {
        this.activity = activity;
    }

    private void onClicks() {
        btnSignin.setOnClickListener(v ->{
            activity.switchTab();
        });
        signUp.setOnClickListener(v -> {
            try {
                    if (allDataCorrect()){
                        if (connectionAvailable(requireContext())){
                        activity.showLoading();
                        signUp.setEnabled(false);
                        FirebaseAuth.getInstance().createUserWithEmailAndPassword(etemail.getText().toString().trim(),
                                        etpass.getText().toString().trim())
                                .addOnSuccessListener(authResult -> {
                                    activity.hideLoading();
                                    Log.e(TAG, "SignUPFragment : Successfully Registered" );
                                    Toast.makeText(requireContext(), "Successfully Registered!", Toast.LENGTH_SHORT).show();
                                    clearEdtiTexts();
                                    activity.switchTab();
                                    signUp.setEnabled(true);

                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(requireContext(), "Not Registered!", Toast.LENGTH_SHORT).show();
                                    Log.e(TAG, "SignUPFragment : " + e.getMessage() );
                                    activity.hideLoading();
                                    signUp.setEnabled(true);
                                });
                        }
                        else {
                            Toast.makeText(requireContext(), "No internet connection!", Toast.LENGTH_SHORT).show();
                        }
                    }
            }
            catch (Exception e){
                Log.e(TAG, "SignUpFragment : " + e.getMessage() );
            }
        });


    }

    private void clearEdtiTexts() {
        etpass.setText("");
        etpassConf.setText("");
        etemail.setText("");
    }

    private boolean allDataCorrect() {
        String email = etemail.getText().toString().trim();
        String pass = etpass.getText().toString().trim();
        String passconf = etpassConf.getText().toString().trim();
        if (email.equals("")){
            etemail.setError("Please enter email address!");
            etemail.requestFocus();
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            etemail.setError("Invalid email address!");
            etemail.requestFocus();
        }
        else if (pass.equals("")){
            etpass.setError("Please enter password!");
            etpass.requestFocus();
        }
        else if (pass.length()<6){
            etpass.setError("Weak password!");
            etpass.requestFocus();
        }
        else if (passconf.equals("")){
            etpassConf.setError("Please confirm password!");
            etpassConf.requestFocus();
        }
        else if (!passconf.equals(pass)){
            etpassConf.setError("Password doesn't match!");
            etpassConf.requestFocus();
        }
        else
            return true;
        return false;
    }

    public static boolean connectionAvailable(Context context){
        int [] ntype = {ConnectivityManager.TYPE_MOBILE,ConnectivityManager.TYPE_WIFI};
        try{
            ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
            for(int networkType : ntype){
                NetworkInfo activeNinfo = cm.getActiveNetworkInfo();
                if (activeNinfo!=null && activeNinfo.getType()==networkType)
                    return true;
            }
        }
        catch (Exception e){
            return false;
        }
        return false;
    }


    private void initializer(View view) {
        btnSignin = view.findViewById(R.id.btnloginS);
        signUp = view.findViewById(R.id.btnSngUp);
        etemail = view.findViewById(R.id.etemail);
        etpass = view.findViewById(R.id.etpass);
        etpassConf = view.findViewById(R.id.etpassConfirm);
    }
}