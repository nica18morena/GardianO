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
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

import com.example.jessi.guardiano.DataObjects.Children;
import com.example.jessi.guardiano.DataObjects.Plan;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.example.jessi.guardiano.R.id.editPlanName;
import static com.example.jessi.guardiano.R.id.spinner_weekday_options;
import static com.example.jessi.guardiano.R.id.spinner_weekend_options;
import static com.example.jessi.guardiano.R.id.text_child_DOB;
import static com.example.jessi.guardiano.R.id.text_child_name;
import static java.security.AccessController.getContext;

//TODO: code clean up
public class ParentingPlanSetup extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private TextView weekendDropOffTimeText, weekendPickUpTimeText;
    private TextView tvPlanName, tvChildName, tvChildDOB;
    private EditText mPlanName, mChildName, mChildDOB;
    private TimePicker timePicker;
    private Button weekendDropoffTimeButton, weekendPickUpTimeButton;
    private int hour, minute;
    private String planName, planStart, childName, childDOB;
    static final int TIME_DIALOG_ID = 999;

    private FirebaseDatabase mfirebaseDatabase;
    private DatabaseReference mDatabaseReferenceP, mDatabaseReferenceC, mDatabaseReference;
    private ChildEventListener mChildEventListenerP, mChildEventListenerC, mChildEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parenting_plan_setup);

        //instantiate text views
        tvPlanName = (TextView) findViewById(R.id.textView_planName);
        tvChildName = (TextView) findViewById(R.id.textView_childName);
        tvChildDOB = (TextView) findViewById(R.id.textView_childDOB);

        //Database access
        mfirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mfirebaseDatabase.getReference();

        //attach db child listener
        dbChildEventListener();

        //Read and save text inputs
        mPlanName = (EditText) findViewById(R.id.editPlanName);
        this.editorActionListener(mPlanName);

        //TODO: Add planStart as input
        //EditText mPlanStart = (EditText) findViewById(R.id.editPlanStart);
        //planStart = mPlanName.getText().toString();
        mChildName = (EditText) findViewById(R.id.text_child_name);
        this.editorActionListener(mChildName);
        mChildDOB = (EditText) findViewById(R.id.text_child_DOB);
        this.editorActionListener(mChildDOB);

        // Spinner element
        Spinner spinnerWeekend = (Spinner) findViewById(R.id.spinner_weekend_options);
        this.initializeSpinner(spinnerWeekend);

        Spinner spinnerWeekday = (Spinner) findViewById(R.id.spinner_weekday_options);
        this.initializeSpinner(spinnerWeekday);

        //call bottom navigation
        this.bottomNavigationViewListener();
    }

    private void initializeSpinner(Spinner s) {

        List<String> categories = new ArrayList<String>();

        // Spinner click listener
        s.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        switch (s.getId()) {
            case spinner_weekend_options:

                categories.add("Every");
                categories.add("Every other");
                categories.add("First wknd of mos");
                categories.add("Second wknd of mos");
                categories.add("Third wknd of mos");
                categories.add("Fourth wknd of mos");
                categories.add("Fifth wknd of mos");
                categories.add("None");
                break;

            case spinner_weekday_options:
                //List<String> categories = new ArrayList<String>();
                categories.add("Every");
                categories.add("Every other");
                categories.add("First week of mos");
                categories.add("Second week of mos");
                categories.add("Third week of mos");
                categories.add("Fourth week of mos");
                categories.add("Fifth week of mos");
                categories.add("None");
                break;
        }
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        s.setAdapter(dataAdapter);
    }

    private void editorActionListener(EditText et) {

        et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                boolean done = false;
                Context context = getApplicationContext();
                if (actionId == EditorInfo.IME_ACTION_NEXT) {

                    String text = v.getText().toString();
                    Toast.makeText(context, text, Toast.LENGTH_SHORT).show();

                    switch (v.getId()) {
                        case editPlanName:
                            planName = text;
                            break;

                        /*case editPlanStart:
                            planStart = text;
                            break;*/

                        case text_child_name:
                            childName = text;
                            break;

                        case text_child_DOB:
                            childDOB = text;
                            break;
                    }
                } else if (actionId == EditorInfo.IME_ACTION_DONE) {
                    String text = v.getText().toString();
                    Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
                    done = true;

                    switch (v.getId()) {
                        case text_child_DOB:
                            childDOB = text;
                            break;
                    }
                }

                if (done) {
                    //v.clearFocus(); //todo:this doesnt work as expected, it trasfers the focus
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.RESULT_HIDDEN);
                    //hideSoftKeyboard();
                    Children children = new Children(childName, childDOB);
                    Plan plan = new Plan(planName);

                    //Update edit text fields to not be visible and text view visible
                   /* mPlanName.setVisibility(View.GONE);
                    mChildName.setVisibility(View.GONE);
                    mChildDOB.setVisibility(View.GONE);

                    tvPlanName.setVisibility(View.VISIBLE);
                    tvChildName.setVisibility(View.VISIBLE);
                    tvChildDOB.setVisibility(View.VISIBLE);*/

                    //Update DB with values
                    mDatabaseReference = mfirebaseDatabase.getReference().child("User/Plan");
                    mDatabaseReference.setValue(plan);
                    mDatabaseReference = mfirebaseDatabase.getReference().child("User/Children");
                    mDatabaseReference.setValue(children);

                    //attach db child listener
                    //dbChildEventListener();
                }

                return done;
            }
        });
    }
    private void dbChildEventListener() {

        mChildEventListener = new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                //hideSoftKeyboard();
                //tvChildDOB.setVisibility(View.VISIBLE);
                if(dataSnapshot.getKey().equals("Children")) {
                    mChildName.setVisibility(View.GONE);
                    mChildDOB.setVisibility(View.GONE);

                    tvChildName.setVisibility(View.VISIBLE);
                    tvChildDOB.setVisibility(View.VISIBLE);

                    Children children = dataSnapshot.getValue(Children.class);
                    tvChildName.setText(children.getChildName());
                    tvChildDOB.setText(children.getChildDOB());
                }
                if(dataSnapshot.getKey().equals("Plan")) {
                    mPlanName.setVisibility(View.GONE);
                    tvPlanName.setVisibility(View.VISIBLE);

                    Plan plan = dataSnapshot.getValue(Plan.class);
                    tvPlanName.setText(plan.getPlanName());
                    //tvChildDOB.setText(children.getChildDOB());
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mDatabaseReference = mfirebaseDatabase.getReference().child("User");
        mDatabaseReference.addChildEventListener(mChildEventListener);
    }

    private void hideSoftKeyboard(){
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.RESULT_HIDDEN);
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
                                Intent intent3 = new Intent(context, SettingsActivity.class);
                                startActivity(intent3);
                                break;
                        }
                        return true;
                    }
                });
    }

    public void onRadioButtonClicked(View view) {

        boolean checked = ((RadioButton) view).isChecked();

        LinearLayout llUnderSchoolAge = (LinearLayout) findViewById(R.id.layout_q_same_schedule);
        LinearLayout llSameSchedule1 = (LinearLayout) findViewById(R.id.layout_weekend_schedule);
        LinearLayout llSameSchedule2 = (LinearLayout) findViewById(R.id.layout_weekday_schedule);
        LinearLayout llWeekdaySelection = (LinearLayout) findViewById(R.id.layout_weekday_selection);
        LinearLayout llTimePicker1 = (LinearLayout) findViewById(R.id.layout_weekend_time_picker);
        LinearLayout llTimePicker2 = (LinearLayout) findViewById(R.id.layout_weekday_time_picker);

        Button buttonNext = (Button) findViewById(R.id.button_first_page);

        //Check which radio button was clicked
        switch (view.getId()) {
            case R.id.yes_radio_button:
                if (checked) {
                    //Make under school age schedule visible
                    llUnderSchoolAge.setVisibility(View.VISIBLE);
                    //TODO: Call DB
                    //Show next button
                    buttonNext.setVisibility(View.GONE);
                }
                break;
            case R.id.yes2_radio_button:
                if (checked) {
                    //Using the same school schedule, no futher action is needed
                    llSameSchedule1.setVisibility(View.GONE);
                    llTimePicker1.setVisibility(View.GONE);
                    llSameSchedule2.setVisibility(View.GONE);
                    llWeekdaySelection.setVisibility(View.GONE);
                    llTimePicker2.setVisibility(View.GONE);
                    //Show next button
                    buttonNext.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.no_radio_button:
                if (checked) {
                    //No futher action is needed, proceed to the next form, child is not under
                    //school age
                    llUnderSchoolAge.setVisibility(View.GONE);

                    llSameSchedule1.setVisibility(View.GONE);
                    llTimePicker1.setVisibility(View.GONE);
                    llSameSchedule2.setVisibility(View.GONE);
                    llWeekdaySelection.setVisibility(View.GONE);
                    llTimePicker2.setVisibility(View.GONE);

                    //Show next button
                    buttonNext.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.no2_radio_button:
                if (checked) {
                    //Child under school age and not using school schedule
                    llSameSchedule1.setVisibility(View.VISIBLE);
                    llTimePicker1.setVisibility(View.VISIBLE);
                    llSameSchedule2.setVisibility(View.VISIBLE);
                    llWeekdaySelection.setVisibility(View.VISIBLE);
                    llTimePicker2.setVisibility(View.VISIBLE);
                    //TODO: add DB call
                    //Show next button
                    buttonNext.setVisibility(View.VISIBLE);
                }
                break;
        }

    }

    public void addImageButtonClicked(View view) {

        //ImageButton ibAdd = (ImageButton) this.findViewById(R.id.image_button_add_remove);


    }
}
