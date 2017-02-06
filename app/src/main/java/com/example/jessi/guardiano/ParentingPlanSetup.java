package com.example.jessi.guardiano;


import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;
//TODO: code clean up
public class ParentingPlanSetup extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private TextView weekendDropOffTimeText, weekendPickUpTimeText;
    private TimePicker timePicker;
    private Button weekendDropoffTimeButton, weekendPickUpTimeButton;
    private int hour, minute;
    static final int TIME_DIALOG_ID = 999;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parenting_plan_setup);
        //TODO:Remove redundant code
        // Spinner element
        Spinner spinnerWeekend = (Spinner) findViewById(R.id.spinner_weekend_options);
        Spinner spinnerWeekday = (Spinner) findViewById(R.id.spinner_weekday_options);

        // Spinner click listener
        spinnerWeekend.setOnItemSelectedListener(this);
        spinnerWeekday.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        List<String> categoriesWeekend = new ArrayList<String>();
        categoriesWeekend.add("Every");
        categoriesWeekend.add("Every other");
        categoriesWeekend.add("First wknd of mos");
        categoriesWeekend.add("Second wknd of mos");
        categoriesWeekend.add("Third wknd of mos");
        categoriesWeekend.add("Fourth wknd of mos");
        categoriesWeekend.add("Fifth wknd of mos");
        categoriesWeekend.add("None");

        List<String> categoriesWeekdays = new ArrayList<String>();
        categoriesWeekdays.add("Every");
        categoriesWeekdays.add("Every other");
        categoriesWeekdays.add("First week of mos");
        categoriesWeekdays.add("Second week of mos");
        categoriesWeekdays.add("Third week of mos");
        categoriesWeekdays.add("Fourth week of mos");
        categoriesWeekdays.add("Fifth week of mos");
        categoriesWeekdays.add("None");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapterWeekend = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categoriesWeekend);
        ArrayAdapter<String> dataAdapterWeekday = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categoriesWeekdays);

        // Drop down layout style - list view with radio button
        dataAdapterWeekend.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapterWeekday.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinnerWeekend.setAdapter(dataAdapterWeekend);
        spinnerWeekday.setAdapter(dataAdapterWeekend);

        //call bottom navigation
        //bottomNavigationViewListener();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    public void bottomNavigationViewListener() {
        //TODO: get the cases to call different Activity (pages)
        final TextView textCalendar = (TextView) findViewById(R.id.action_calendar);
        final TextView textPlan = (TextView) findViewById(R.id.action_plan);
        final TextView textSettings = (TextView) findViewById(R.id.action_settings);

        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_calendar:
                                textCalendar.setVisibility(View.VISIBLE);
                                textPlan.setVisibility(View.GONE);
                                textSettings.setVisibility(View.GONE);
                                break;
                            case R.id.action_plan:
                                textCalendar.setVisibility(View.GONE);
                                textPlan.setVisibility(View.VISIBLE);
                                textSettings.setVisibility(View.GONE);
                                break;
                            case R.id.action_settings:
                                textCalendar.setVisibility(View.GONE);
                                textPlan.setVisibility(View.GONE);
                                textSettings.setVisibility(View.VISIBLE);
                                break;
                        }
                        return true;
                    }
                });
    }

    /*public void addListeneronButton(){

        weekendDropoffTimeButton = (Button) findViewById(R.id.button_drop_off);

        weekendDropoffTimeButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                showDialog(TIME_DIALOG_ID);

            }

        });
    }*/
    /*@Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case TIME_DIALOG_ID:
                // set time picker as current time
                return new TimePickerDialog(this,
                        timePickerListener, hour, minute,false);

        }
        return null;
    }*/

    /*private TimePickerDialog.OnTimeSetListener timePickerListener =
            new TimePickerDialog.OnTimeSetListener() {
                public void onTimeSet(TimePicker view, int selectedHour,
                                      int selectedMinute) {
                    hour = selectedHour;
                    minute = selectedMinute;

                    // set current time into textview
                    weekendDropOffTimeText.setText(new StringBuilder().append(pad(hour))
                            .append(":").append(pad(minute)));

                    // set current time into timepicker
                    timePicker.setCurrentHour(hour);
                    timePicker.setCurrentMinute(minute);

                }
            };*/
    private static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }
}
