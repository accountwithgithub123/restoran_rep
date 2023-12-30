package com.example.restoran.Fragements;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;
import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.restoran.ForgetActivity;
import com.example.restoran.MainActivity;
import com.example.restoran.ProgDialog;
import com.example.restoran.R;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Collections;
import java.util.List;


public class SigninFragment extends Fragment {

    Button btnSignIn,forgetpass;
    Button btnGoogle;
    EditText etemail,etpass;
    CheckBox reme;
    private static final int REQ_CODE = 10001;

    public SigninFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_signin, container, false);
        intializer(view);
        onClicks();

        return view;
    }


    private void onClicks() {
        btnSignIn.setOnClickListener(v -> {
            try {
                if (allDataCorrect()){
                    if (connectionAvailable(requireContext())){
                            ProgDialog prog = new ProgDialog(requireContext());
                            prog.show();
                            btnSignIn.setEnabled(false);
                            FirebaseAuth.getInstance().signInWithEmailAndPassword(etemail.getText().toString().trim(),
                                etpass.getText().toString().trim())
                                .addOnSuccessListener(authResult -> {
                                    prog.dismiss();
                                    if (reme.isChecked()){
                                        kepUserLoged();
                                    }
                                    startActivity(new Intent(requireContext(), MainActivity.class));
                                    requireActivity().finish();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                    prog.dismiss();
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
        btnGoogle.setOnClickListener(v -> {
            List<AuthUI.IdpConfig> providers = Collections.singletonList(
                    new AuthUI.IdpConfig.GoogleBuilder().build());

            // Create and launch sign-in intent
            @SuppressLint("WrongConstant")
            Intent signInIntent = AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setAvailableProviders(providers)
                    .build().setFlags(PendingIntent.FLAG_IMMUTABLE);
            startActivityForResult(signInIntent,REQ_CODE);
        });
        forgetpass.setOnClickListener(v -> startActivity(new Intent(requireContext(), ForgetActivity.class)));
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==REQ_CODE){
            if (resultCode==RESULT_OK){
                kepUserLoged();
                startActivity(new Intent(requireContext(),MainActivity.class));
                requireActivity().finish();
            }
            else{
                Toast.makeText(requireContext(), "Request cancelled!", Toast.LENGTH_SHORT).show();
            }
        }

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
    public void kepUserLoged() {
        SharedPreferences.Editor editor = requireContext().getSharedPreferences("Loged",MODE_PRIVATE).edit();
        editor.putBoolean("isloged",true);
        editor.apply();
    }

    private void intializer(View view) {
        btnSignIn = view.findViewById(R.id.btnlogin);
        forgetpass = view.findViewById(R.id.btnforget);
        etemail = view.findViewById(R.id.etemail1);
        etpass = view.findViewById(R.id.etpass1);
        reme = view.findViewById(R.id.remem);
        btnGoogle = view.findViewById(R.id.btngoogle);
    }
}