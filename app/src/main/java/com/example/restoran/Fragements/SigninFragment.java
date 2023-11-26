package com.example.restoran.Fragements;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.restoran.LoginRegister;
import com.example.restoran.MainActivity;
import com.example.restoran.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class SigninFragment extends Fragment {

    Button btnSignIn,forgetpass;
    EditText etemail,etpass;
    CheckBox reme;

    LoginRegister activity;
    public SigninFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_signin, container, false);
        intializer(view);
        onClicks();
        return view;
    }

    public void setAcitivity(LoginRegister activity) {
        this.activity = activity;
    }


    private void onClicks() {
        btnSignIn.setOnClickListener(v -> {
            try {
                    if (allDataCorrect()){
                        if (connectionAvailable(requireContext())){
                            activity.showLoading();
                            btnSignIn.setEnabled(false);
                            FirebaseAuth.getInstance().signInWithEmailAndPassword(etemail.getText().toString().trim(),
                                etpass.getText().toString().trim())
                                .addOnSuccessListener(authResult -> {
                                    activity.hideLoading();
                                    if (reme.isChecked()){
                                        activity.keepLogged();
                                    }
                                    startActivity(new Intent(requireContext(), MainActivity.class));
                                    requireActivity().finish();
                                })
                                .addOnFailureListener(e -> {
                                    activity.hideLoading();
                                    btnSignIn.setEnabled(true);
                                });
                        }
                        else {
                            Toast.makeText(requireContext(), "No internet connection!", Toast.LENGTH_SHORT).show();
                        }
                    }
            }
            catch (Exception e){
                Log.e(TAG, "SIGNinFragment : " + e.getMessage() );
            }
        });
    }


    private boolean allDataCorrect() {
        String email = etemail.getText().toString().trim();
        String pass = etpass.getText().toString().trim();
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


    private void intializer(View view) {
        btnSignIn = view.findViewById(R.id.btnlogin);
        forgetpass = view.findViewById(R.id.btnforget);
        etemail = view.findViewById(R.id.etemail1);
        etpass = view.findViewById(R.id.etpass1);
        reme = view.findViewById(R.id.remem);
    }
}