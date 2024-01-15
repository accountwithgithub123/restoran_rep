package com.example.restoran.Fragements;

import static android.app.Activity.RESULT_OK;
import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.restoran.ProgDialog;
import com.example.restoran.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class SginUpFragment extends Fragment {

    Button signUp;
    EditText etemail,etpass,etpassConf;
    CircleImageView profImg;
    Bitmap bitmap;

    ProgDialog prog;
    private String urlImg;

    public SginUpFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_sgin_up, container, false);
        initializer(view);
        onClicks();

        return view;
    }


    private void onClicks() {
        profImg.setOnClickListener(v -> {
            Intent galintent = new Intent(Intent.ACTION_PICK);
            galintent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galintent,2);
        });
        signUp.setOnClickListener(v -> {
            try {
                    if (allDataCorrect()){
                        if (SigninFragment.connectionAvailable(requireContext())){
                            if (bitmap!=null){
                                prog = new ProgDialog(requireContext());
                                prog.show();
                                signUp.setEnabled(false);
                                handleUpload(bitmap);
                            }
                            else {
                                Toast.makeText(requireContext(), "Please select your profile photo!", Toast.LENGTH_SHORT).show();
                            }
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

    private void handleUpload(Bitmap bitmap) {
        StorageReference ref = FirebaseStorage.getInstance().getReference()
                .child("Profile_Images")
                .child(etemail.getText().toString() + ".jpg");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,75,baos);
        ref.putBytes(baos.toByteArray())
                .addOnSuccessListener(taskSnapshot -> getDownloadUrl(ref))
                .addOnFailureListener(e -> Log.e(TAG, "onFailure: " + e.getMessage() ));
    }

    private void getDownloadUrl(StorageReference ref) {
        ref.getDownloadUrl()
                .addOnSuccessListener(uri -> {
                    urlImg = uri.toString();
                    Log.d(ContentValues.TAG, "onSuccess: Photo Uploaded Successfully!" + "IMG_URI: " + uri);
                    registerUser();
                })
                .addOnFailureListener(e -> Log.e(TAG, "onFailure: " + e.getMessage() ));
    }

    private void registerUser() {
        FirebaseAuth user = FirebaseAuth.getInstance();
        user.createUserWithEmailAndPassword(etemail.getText().toString().trim(),
                        etpass.getText().toString().trim())
                .addOnSuccessListener(authResult -> {
                    FirebaseDatabase.getInstance().getReference().child("ProfileImgLinks")
                            .child(Objects.requireNonNull(user.getUid())).setValue(urlImg)
                            .addOnSuccessListener(unused -> {
                                prog.dismiss();
                                Log.e(TAG, "SignUPFragment : Successfully Registered" );
                                Toast.makeText(requireContext(), "Successfully Registered!", Toast.LENGTH_SHORT).show();
                                clearEdtiTexts();
                                signUp.setEnabled(true);
                            });


                })
                .addOnFailureListener(e -> {
                    Toast.makeText(requireContext(), "Not Registered!", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "SignUPFragment : " + e.getMessage() );
                    prog.dismiss();
                    signUp.setEnabled(true);
                });
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==2 && resultCode==RESULT_OK){
            assert data != null;
            profImg.setImageURI(data.getData());
            try {
                bitmap = MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(),data.getData());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void clearEdtiTexts() {
        etpass.setText("");
        etpassConf.setText("");
        etemail.setText("");
        urlImg = null;
        bitmap = null;
        profImg.setImageResource(R.drawable.profile);
    }

    private boolean allDataCorrect() {
        String email = etemail.getText().toString().trim();
        String pass = etpass.getText().toString().trim();
        String passconf = etpassConf.getText().toString().trim();
        if (email.equals("")){
            etemail.setError("Enter email address!");
            etemail.requestFocus();
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            etemail.setError("Invalid email address!");
            etemail.requestFocus();
        }
        else if (pass.equals("")){
            etpass.setError("Enter password!");
            etpass.requestFocus();
        }
        else if (pass.length()<6){
            etpass.setError("Weak password!");
            etpass.requestFocus();
        }
        else if (passconf.equals("")){
            etpassConf.setError("Confirm password!");
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



    private void initializer(View view) {
        signUp = view.findViewById(R.id.btnSngUp);
        etemail = view.findViewById(R.id.etemail);
        etpass = view.findViewById(R.id.etpass);
        etpassConf = view.findViewById(R.id.etpassConfirm);
        profImg = view.findViewById(R.id.profile_image);
        bitmap = null;
        urlImg = "";
    }
}