package com.example.restoran;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.restoran.Fragements.SigninFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class BookTable extends AppCompatActivity {
    EditText etName, etEmail,  etNoGuests, etRequest;
    TextView etDate, etTime;
    LinearLayout laydate,laytime;
    Button btnSubmit, btnCancel;
    String timeSel,selDate = "";
    ScrollView sbar;
    OrderFirebase order;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_table);
        initializer();
        clickListeners();
            if (getIntent()!=null){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    order = getIntent().getParcelableExtra("obj",OrderFirebase.class);
                }
                else {
                    order = getIntent().getParcelableExtra("obj");
                }
                if (order!=null){
                    btnSubmit.setText("Update Order");
                    etName.setText(order.getName());
                    etEmail.setText(order.getEmail());
                    etDate.setText(order.getDate());
                    selDate = order.getDate();
                    etTime.setText(order.getTime());
                    timeSel = order.getTime();
                    etNoGuests.setText(order.getNumberOfGuests());
                    etRequest.setText(order.getRequest());
                    Log.e("Intent Data : ", "onCreate: Order Object was not null" );
                }
                else
                    Log.e("Intent Data : ", "onCreate: Order Object was null" );
            }
    }
    private void initializer() {
        etName = findViewById(R.id.etNamebt);
        etEmail = findViewById(R.id.etembt);
        etDate = findViewById(R.id.etdatebt);
        etTime = findViewById(R.id.ettimebt);
        etNoGuests = findViewById(R.id.etguestbt);
        etRequest = findViewById(R.id.etrequestbt);
        btnSubmit = findViewById(R.id.btnsubmit);
        btnCancel = findViewById(R.id.btnCancel);
        laydate = findViewById(R.id.laydatebt);
        laytime = findViewById(R.id.laytimebt);
        sbar = findViewById(R.id.sbar);
        ActionBar abar = getSupportActionBar();
        assert abar != null;
        abar.setTitle("Reservation");
        abar.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        abar.setElevation(0.0f);
        abar.setDisplayHomeAsUpEnabled(true);

    }
    private void clickListeners() {
        laydate.setOnClickListener(v -> showDatePicker(selectedDate -> {
            // Update etDate with the selected date
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
            selDate = dateFormat.format(selectedDate);
            etDate.setText(selDate);
        }));
        laytime.setOnClickListener(v ->{
            if (selDate.isEmpty())
                Toast.makeText(this, "First select date!", Toast.LENGTH_SHORT).show();
            else {
                showTimePicker(selectedTime -> {
                    // Update etTime with the selected time
                    SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.US);
                    timeSel = timeFormat.format(selectedTime);
                    etTime.setText(timeSel);
                });
            }
                }
                );

        btnSubmit.setOnClickListener(v -> validateForm());
        btnCancel.setOnClickListener(v -> finish());
    }

    private boolean todayDate(){
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String cdate =  sd.format(new Date());
        int cyear = Integer.parseInt(cdate.substring(0,4));
        int cmonth = Integer.parseInt(cdate.substring(5,7));
        int cday = Integer.parseInt(cdate.substring(8));
        Log.e("BookTable", "CurDate: " + cyear + "-" + cmonth + "-" + cday );
        Log.e("BookTable", "SelDate: " + Integer.parseInt(selDate.substring(6)) + "-" + Integer.parseInt(selDate.substring(0,2)) + "-" + Integer.parseInt(selDate.substring(3,5)) );

        return cday == Integer.parseInt(selDate.substring(3, 5)) && cmonth == Integer.parseInt(selDate.substring(0, 2))
                && cyear == Integer.parseInt(selDate.substring(6));
    }

    private void validateForm() {
        // Reset any previous error messages
        etName.setError(null);
        etEmail.setError(null);
        etDate.setError(null);
        etTime.setError(null);
        etNoGuests.setError(null);
        etRequest.setError(null);

        // Get values from EditText fields
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String noGuests = etNoGuests.getText().toString().trim();
        String request = etRequest.getText().toString().trim();

        // Validate name
        if (TextUtils.isEmpty(name)) {
            etName.setError("Name is required");
            etName.requestFocus();

        }

        // Validate email
        else if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Enter a valid email address");
            etEmail.requestFocus();

        }

        // Validate date (assuming date is entered in a specific format)
        else if (TextUtils.isEmpty(selDate)) {
            etDate.setError("Date is required");
            etDate.requestFocus();

        }

        // Validate time (assuming time is entered in a specific format)
        else if (TextUtils.isEmpty(timeSel)) {
            etTime.setError("Time is required");
            etTime.requestFocus();

        }
        else if (TextUtils.isEmpty(noGuests) || !TextUtils.isDigitsOnly(noGuests) || Integer.parseInt(noGuests) <= 0) {
            etNoGuests.setError("Enter a valid number of guests");
            etNoGuests.requestFocus();

        }
        else {
            if (SigninFragment.connectionAvailable(this)){
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    Log.e("Booking Table ", "validateForm: User is not null with UID = "+ user.getUid() );
                    ProgDialog prog = new ProgDialog(BookTable.this);
                        prog.show();
                    btnSubmit.setEnabled(false);
                    String fbKey;
                    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference()
                            .child("ordersDetail").child(user.getUid());
                    if (btnSubmit.getText().equals("Update Order")){
                        fbKey  = order.getFbKey();
                    }
                    else{
                        fbKey = myRef.push().getKey();
                    }
                    assert fbKey != null;
                    myRef.child(fbKey).setValue(new OrderFirebase(fbKey,name,email,selDate,timeSel,noGuests,request))
                            .addOnSuccessListener(unused -> {
                                prog.dismiss();
                                Toast.makeText(this, "Order placed successfully.", Toast.LENGTH_SHORT).show();
                                Log.e("Booking Table ", "Order Placed Successfully! ");
                                finish();
                            })
                            .addOnFailureListener(e -> {
                                prog.dismiss();
                                Toast.makeText(this, "Error while placing order!", Toast.LENGTH_SHORT).show();
                                Log.e("Booking Table ", "Error while placing order! " + e.getMessage());
                                btnSubmit.setEnabled(true);
                            });
                }
                else{
                    Log.e("Booking Table ", "User is null" );
                }
            }
            else{
                Toast.makeText(this, "No internet connection!", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void clearFocus() {
        etName.clearFocus();
        etEmail.clearFocus();
        etRequest.clearFocus();
        etNoGuests.clearFocus();
    }

    interface DateCallback {
        void onDateSelected(Date selectedDate);
    }
    private void showDatePicker(final DateCallback callback) {
        // Get the current date
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Create and show DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year1, monthOfYear, dayOfMonth) -> {
            // Update etDate with the selected date
            calendar.set(year1, monthOfYear, dayOfMonth);
            Date selectedDate = calendar.getTime();

            // Call the callback to return the selected date
            if (callback != null) {
                callback.onDateSelected(selectedDate);
            }
        }, year, month, day);

        // Set the minimum date to the current date to prevent selection of past dates
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

        // Show the dialog
        datePickerDialog.show();
    }

    interface TimeCallback {
        void onTimeSelected(Date selectedTime);
    }
    private void showTimePicker(final TimeCallback callback) {
        // Get the current time
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        // Create and show TimePickerDialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view, hourOfDay, minute1) -> {
            // Check if the selected time is in the past
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendar.set(Calendar.MINUTE, minute1);

            if (calendar.getTimeInMillis() < System.currentTimeMillis() && todayDate()) {
                // Selected time is in the past, show a message and prompt the user to select again
                Toast.makeText(BookTable.this, "Please select a future time", Toast.LENGTH_SHORT).show();
                showTimePicker(callback); // Recursively call the method to show the TimePicker again
            } else {
                // Update etTime with the selected time
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute1);
                Date selectedTime = calendar.getTime();
                // Call the callback to return the selected time
                if (callback != null) {
                    callback.onTimeSelected(selectedTime);
                }
            }
        }, hour, minute, false);
        timePickerDialog.show();
    }



    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("selDate", selDate);
        outState.putString("selTime", timeSel);
        outState.putString("date", etDate.getText().toString());
        outState.putString("time", etTime.getText().toString());
        outState.putInt("scrol",sbar.getScrollY());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        clearFocus();
        selDate = savedInstanceState.getString("selDate");
        timeSel = savedInstanceState.getString("selTime");
        etDate.setText(savedInstanceState.getString("date"));
        etTime.setText(savedInstanceState.getString("time"));
        sbar.setScrollY(savedInstanceState.getInt("scrol",0));
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

}