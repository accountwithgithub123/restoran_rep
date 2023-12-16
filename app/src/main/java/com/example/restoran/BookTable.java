package com.example.restoran;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class BookTable extends AppCompatActivity {
    EditText etName, etEmail,  etNoGuests, etRequest;
    TextView etDate, etTime;
    LinearLayout laydate,laytime;
    Button btnSubmit, btnCancel;
    ScrollView sbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_table);
        initializer();
        clickListeners();

    }

    private void clickListeners() {
        laydate.setOnClickListener(v -> {
            showDatePicker(new DateCallback() {
                @Override
                public void onDateSelected(Date selectedDate) {
                    // Update etDate with the selected date
                    SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
                    etDate.setText(dateFormat.format(selectedDate));
                }
            });
        });
        laytime.setOnClickListener(v -> showTimePicker(new TimeCallback() {
            @Override
            public void onTimeSelected(Date selectedTime) {
                // Update etTime with the selected time
                SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.US);
                etTime.setText(timeFormat.format(selectedTime));
            }
        }));
        btnSubmit.setOnClickListener(v -> {
            validateForm();
            });
        btnCancel.setOnClickListener(v -> finish());
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
        String date = etDate.getText().toString().trim();
        String time = etTime.getText().toString().trim();
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
        else if (TextUtils.isEmpty(date)) {
            etDate.setError("Date is required");
            etDate.requestFocus();

        }

        // Validate time (assuming time is entered in a specific format)
        else if (TextUtils.isEmpty(time)) {
            etTime.setError("Time is required");
            etTime.requestFocus();

        }

        // Validate number of guests
        else if (TextUtils.isEmpty(noGuests) || !TextUtils.isDigitsOnly(noGuests) || Integer.parseInt(noGuests) <= 0) {
            etNoGuests.setError("Enter a valid number of guests");
            etNoGuests.requestFocus();

        }
        else {

            Toast.makeText(this, "Form submitted successfully", Toast.LENGTH_SHORT).show();
        }


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
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // Update etDate with the selected date
                calendar.set(year, monthOfYear, dayOfMonth);
                Date selectedDate = calendar.getTime();

                // Call the callback to return the selected date
                if (callback != null) {
                    callback.onDateSelected(selectedDate);
                }
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
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                // Check if the selected time is in the past
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);

                if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
                    // Selected time is in the past, show a message and prompt the user to select again
                    Toast.makeText(BookTable.this, "Please select a future time", Toast.LENGTH_SHORT).show();
                    showTimePicker(callback); // Recursively call the method to show the TimePicker again
                } else {
                    // Update etTime with the selected time
                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    calendar.set(Calendar.MINUTE, minute);
                    Date selectedTime = calendar.getTime();

                    // Call the callback to return the selected time
                    if (callback != null) {
                        callback.onTimeSelected(selectedTime);
                    }
                }
            }
        }, hour, minute, false); // The last parameter indicates whether the time is in 24-hour format

        // Show the dialog
        timePickerDialog.show();
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
        abar.setTitle("Online Booking");
        abar.setDisplayHomeAsUpEnabled(true);

    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("date", etDate.getText().toString());
        outState.putString("time", etTime.getText().toString());
        outState.putInt("scrol",sbar.getScrollY());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
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