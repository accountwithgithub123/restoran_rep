package com.example.restoran.Fragements;

import static android.app.Activity.RESULT_OK;
import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.restoran.ForgetActivity;
import com.example.restoran.KeepLogged;
import com.example.restoran.LoginRegister;
import com.example.restoran.MainActivity;
import com.example.restoran.ProgDialog;
import com.example.restoran.R;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;


public class SigninFragment extends Fragment {

    Button btnSignIn,forgetpass,btnReg;
    Button btnGoogle;
    EditText etemail,etpass;
    CheckBox reme;

    GoogleSignInClient mclient;
    LoginRegister activity;
    private static final int RC_SIGN_IN = 123;
    private static final int REQ_CODE = 10001;

    public SigninFragment() {
    }

    public SigninFragment(LoginRegister activity) {
        this.activity = activity;
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
                            ProgDialog prog = new ProgDialog(requireContext());
                            prog.show();
//                            activity.showLoading();
                            btnSignIn.setEnabled(false);
                            FirebaseAuth.getInstance().signInWithEmailAndPassword(etemail.getText().toString().trim(),
                                etpass.getText().toString().trim())
                                .addOnSuccessListener(authResult -> {
//                                    activity.hideLoading();
                                    prog.dismiss();
                                    if (reme.isChecked()){
                                        new KeepLogged(requireContext()).kepUserLoged();
//                                        if (activity!=null)
//                                            activity.keepLogged();
//                                        else
//                                            Log.e(TAG, "onClicks: Activity is null in SignIn KeepLoged not works");
                                    }
                                    startActivity(new Intent(requireContext(), MainActivity.class));
                                    requireActivity().finish();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
//                                    activity.hideLoading();
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
            List<AuthUI.IdpConfig> providers = Arrays.asList(
                   new AuthUI.IdpConfig.GoogleBuilder().build());

// Create and launch sign-in intent
            @SuppressLint("WrongConstant")
            Intent signInIntent = AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setAvailableProviders(providers)
                    .build().setFlags(PendingIntent.FLAG_IMMUTABLE);
            startActivityForResult(signInIntent,REQ_CODE);
        });
      /*


        btnGoogle.setOnClickListener(v -> {
            createRequest();
            signIn();
        });

       */
        /*
        btnReg.setOnClickListener(v -> {
            try {
                activity.switchTab();
            }
            catch (Exception e){
                Log.e(TAG, "onClicks: " + e.getMessage() );
            }
        });

         */
        forgetpass.setOnClickListener(v -> startActivity(new Intent(requireContext(), ForgetActivity.class)));
    }

    private void createRequest() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
         mclient = GoogleSignIn.getClient(activity,gso);
    }
    private void signIn() {
        Intent signInIntent = mclient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
        if (requestCode==REQ_CODE){
            if (resultCode==RESULT_OK){
//                activity.keepLogged();
                new KeepLogged(requireContext()).kepUserLoged();
                startActivity(new Intent(requireContext(),MainActivity.class));
                requireActivity().finish();
            }
            else{
                Toast.makeText(requireContext(), "request cancelled...", Toast.LENGTH_SHORT).show();
            }
        }

    }
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            if (completedTask.isSuccessful()){
                GoogleSignInAccount account = completedTask.getResult(ApiException.class);
                Intent intent = new Intent(requireContext(),MainActivity.class);
                intent.putExtra("email",account.getEmail());
                intent.putExtra("phurl", Objects.requireNonNull(account.getPhotoUrl()).toString());
                startActivity(intent);
    //            activity.keepLogged();
                    new KeepLogged(requireContext()).kepUserLoged();
                requireActivity().finish();
            }
            // e.g., account.getEmail(), account.getDisplayName(), etc.
        } catch (ApiException e) {
            // Handle failed sign-in
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
        }
    }
/*
if (usr.getMetadata().getCreationTimestamp() == usr.getMetadata().getLastSignInTimestamp()){
                    Toast.makeText(requireContext(), "Welcome to our app!", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(requireContext(), "Welcome Back!", Toast.LENGTH_SHORT).show();
                }
 */



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
//        btnReg = view.findViewById(R.id.btnregisterS);
        forgetpass = view.findViewById(R.id.btnforget);
        etemail = view.findViewById(R.id.etemail1);
        etpass = view.findViewById(R.id.etpass1);
        reme = view.findViewById(R.id.remem);
        btnGoogle = view.findViewById(R.id.btngoogle);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("act",activity);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (savedInstanceState != null) {
                activity = savedInstanceState.getParcelable("act", LoginRegister.class);
            }
        }
    }
}