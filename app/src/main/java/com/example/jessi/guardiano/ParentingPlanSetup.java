package com.example.jessi.guardiano;


import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
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
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

import static java.security.AccessController.getContext;

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
        bottomNavigationViewListener();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
        //Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
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
        final Context context = this;

        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(

                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_calendar:
                                Intent intent1 = new Intent(context, Calendar.class);
                                startActivity(intent1);
                                break;
                            case R.id.action_plan:
                                Intent intent2 = new Intent(context, ParentingPlanSetup.class);
                                startActivity(intent2);
                                break;
                            case R.id.action_settings:

                                break;
                        }
                        return true;
                    }
                });
    }

    public void onRadioButtonClicked(View view) {

        boolean checked = ((RadioButton) view).isChecked();

        LinearLayout llUnderSchoolAge   = (LinearLayout) findViewById(R.id.layout_q_same_schedule);
        LinearLayout llSameSchedule1    = (LinearLayout) findViewById(R.id.layout_weekend_schedule);
        LinearLayout llSameSchedule2    = (LinearLayout) findViewById(R.id.layout_weekend_schedule);
        LinearLayout llWeekdaySelection = (LinearLayout) findViewById(R.id.layout_weekday_selection);
        LinearLayout llTimePicker1      = (LinearLayout) findViewById(R.id.layout_weekend_time_picker);
        LinearLayout llTimePicker2      = (LinearLayout) findViewById(R.id.layout_weekday_time_picker);

        //Check which radio button was clicked
        switch(view.getId()) {
            case R.id.yes_radio_button:
                if (checked){
                    //something
                    llUnderSchoolAge.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.yes2_radio_button:
                if (checked) {
                    //something
                    llSameSchedule1.setVisibility(View.VISIBLE);
                    llTimePicker1.setVisibility(View.VISIBLE);
                    llSameSchedule2.setVisibility(View.VISIBLE);
                    llWeekdaySelection.setVisibility(View.VISIBLE);
                    llTimePicker2.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.no_radio_button:
                if (checked){
                    //something
                    llUnderSchoolAge.setVisibility(View.GONE);

                    llSameSchedule1.setVisibility(View.GONE);
                    llTimePicker1.setVisibility(View.GONE);
                    llSameSchedule2.setVisibility(View.GONE);
                    llWeekdaySelection.setVisibility(View.GONE);
                    llTimePicker2.setVisibility(View.GONE);
                }
                break;
            case R.id.no2_radio_button:
                if (checked) {
                    //something
                    llSameSchedule1.setVisibility(View.GONE);
                    llTimePicker1.setVisibility(View.GONE);
                    llSameSchedule2.setVisibility(View.GONE);
                    llWeekdaySelection.setVisibility(View.GONE);
                    llTimePicker2.setVisibility(View.GONE);
                }
                break;
        }

    }

}
