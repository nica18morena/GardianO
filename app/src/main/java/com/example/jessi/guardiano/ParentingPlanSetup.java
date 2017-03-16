package com.example.jessi.guardiano;


import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.jessi.guardiano.CalendarAccess.CalendarAccess;
import com.example.jessi.guardiano.DataObjects.Children;
import com.example.jessi.guardiano.DataObjects.Plan;
import com.example.jessi.guardiano.DataObjects.UnderSchoolAgeScheduleWeekday;
import com.example.jessi.guardiano.DataObjects.UnderSchoolAgeScheduleWeekend;
//import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.example.jessi.guardiano.R.id.button_drop_off;
import static com.example.jessi.guardiano.R.id.button_drop_off_weekday;
import static com.example.jessi.guardiano.R.id.button_pick_up;
import static com.example.jessi.guardiano.R.id.button_pick_up_weekday;
import static com.example.jessi.guardiano.R.id.checkbox_Friday;
import static com.example.jessi.guardiano.R.id.checkbox_Monaday;
import static com.example.jessi.guardiano.R.id.checkbox_Thursday;
import static com.example.jessi.guardiano.R.id.checkbox_Tuesday;
import static com.example.jessi.guardiano.R.id.checkbox_Wednesday;
import static com.example.jessi.guardiano.R.id.editPlanName;
import static com.example.jessi.guardiano.R.id.spinner_weekday_options;
import static com.example.jessi.guardiano.R.id.spinner_weekend_options;
import static com.example.jessi.guardiano.R.id.text_child_DOB;
import static com.example.jessi.guardiano.R.id.text_child_name;
import static java.security.AccessController.doPrivilegedWithCombiner;

//TODO: code clean up
public class ParentingPlanSetup extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private final String PLAN_NAME = "PARENTINGPLANNAME";
    private final String CHILD_NAME = "CHILDNAME";    private final String CHILD_DOB = "CHILDDOB";

    private static final String TAG = "ParentingPlanSetup";
    private static final String WRITE_CALENDAR = "android.permission.WRITE_CALENDAR";
    private static final int MY_PERMISSIONS_REQUEST_WRITE_CALENDAR = 1;
    private TextView tvweekendDropOffTimeText, tvweekendPickUpTimeText, tvweekdayDropOffTimeText, tvweekdayPickUpTimeText;
    private TextView tvPlanName, tvChildName, tvChildDOB;
    private EditText mPlanName, mChildName, mChildDOB;
    private Spinner sweekendFrequency, sweekdayFrequency, spinnerWeekend, spinnerWeekday;
    private TimePicker timePicker;
    private Button weekendDropoffTimeButton, weekendPickUpTimeButton, buttonNext;
    private int hour, minute;
    private long CAL_ID, USAWeekdayEventId, USAWeekendEventId;
    private String planName, planStart, childName, childDOB, weekendFrequency, weekdayFrequency;
    private String weekendDropOffTime, weekendPickUpTime, weekdayDropOffTime, weekdayPickUpTime;
    static final int TIME_DIALOG_ID = 999;
    private CheckBox cbmonday, cbtuesday, cbwednesday, cbthursday, cbfriday;
    private boolean mon, tues, wed, thur, fri;
    private FirebaseDatabase mfirebaseDatabase;
    private DatabaseReference mDatabaseReferenceP, mDatabaseReferenceC, mDatabaseReference;
    private ChildEventListener mChildEventListenerP, mChildEventListenerC, mChildEventListener;
    private LinearLayout llUnderSchoolAge, llSameSchedule1, llSameSchedule2, llWeekdaySelection, llTimePicker1, llTimePicker2 ;
    private RadioButton yesRadioButton, yesRadioButton2, noRadioButton, noRadioButton2;
    private CalendarAccess calendarAccess;
    private static final int RC_SIGN_IN = 1;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parenting_plan_setup);

        //instantiate text views
        tvPlanName = (TextView) findViewById(R.id.textView_planName);
        tvChildName = (TextView) findViewById(R.id.textView_childName);
        tvChildDOB = (TextView) findViewById(R.id.textView_childDOB);
        tvweekdayDropOffTimeText = (TextView) findViewById(R.id.text_drop_off_weekday);
        tvweekdayPickUpTimeText = (TextView) findViewById(R.id.text_pick_up_weekday);
        tvweekendDropOffTimeText = (TextView) findViewById(R.id.text_drop_off);
        tvweekendPickUpTimeText = (TextView) findViewById(R.id.text_pick_up);

        //Initiate layouts
        llUnderSchoolAge = (LinearLayout) findViewById(R.id.layout_q_same_schedule);
        llSameSchedule1 = (LinearLayout) findViewById(R.id.layout_weekend_schedule);
        llSameSchedule2 = (LinearLayout) findViewById(R.id.layout_weekday_schedule);
        llWeekdaySelection = (LinearLayout) findViewById(R.id.layout_weekday_selection);
        llTimePicker1 = (LinearLayout) findViewById(R.id.layout_weekend_time_picker);
        llTimePicker2 = (LinearLayout) findViewById(R.id.layout_weekday_time_picker);

        //initiate buttons
        buttonNext = (Button) findViewById(R.id.button_first_page);

        //Initialize RadioButtons
        yesRadioButton = (RadioButton) findViewById(R.id.yes_radio_button);
        yesRadioButton2 = (RadioButton) findViewById(R.id.yes2_radio_button);
        noRadioButton = (RadioButton) findViewById(R.id.no_radio_button);
        noRadioButton2 = (RadioButton) findViewById(R.id.no2_radio_button);

        //Initialize weekday checkboxes
        cbmonday = (CheckBox) findViewById(R.id.checkbox_Monaday);
        cbtuesday = (CheckBox) findViewById(R.id.checkbox_Tuesday);
        cbwednesday = (CheckBox) findViewById(R.id.checkbox_Wednesday);
        cbthursday = (CheckBox) findViewById(R.id.checkbox_Thursday);
        cbfriday = (CheckBox) findViewById(R.id.checkbox_Friday);
        /*if(savedInstanceState != null){
            planName = savedInstanceState.getString(PLAN_NAME);
            tvPlanName.setText(planName);
        }*/
        // Spinner element
        spinnerWeekend = (Spinner) findViewById(R.id.spinner_weekend_options);
        this.initializeSpinner(spinnerWeekend);

        spinnerWeekday = (Spinner) findViewById(R.id.spinner_weekday_options);
        this.initializeSpinner(spinnerWeekday);

        //Database access
        mfirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mfirebaseDatabase.getReference();

        mFirebaseAuth = FirebaseAuth.getInstance();

        //attach db child listener
        dbChildEventListener();
        firebaseAuthListener();
        //Calendar instance
        Context context = this;
        calendarAccess = new CalendarAccess(context);
        //TODO: Find a place to check if calendar exists.
        //calendarAccess.createCalendar();

        //Read and save text inputs
        mPlanName = (EditText) findViewById(R.id.editPlanName);
        this.editorActionListener(mPlanName);

        //EditText mPlanStart = (EditText) findViewById(R.id.editPlanStart);
        //planStart = mPlanName.getText().toString();
        mChildName = (EditText) findViewById(R.id.text_child_name);
        this.editorActionListener(mChildName);
        mChildDOB = (EditText) findViewById(R.id.text_child_DOB);
        this.editorActionListener(mChildDOB);

        //call bottom navigation
        //this.bottomNavigationViewListener();

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,WRITE_CALENDAR)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    WRITE_CALENDAR)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,new String[]{WRITE_CALENDAR},
                        MY_PERMISSIONS_REQUEST_WRITE_CALENDAR);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }

        new InsertParentingSchedule().execute();


    }

    private void firebaseAuthListener(){
        mAuthStateListener = new FirebaseAuth.AuthStateListener(){
          @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth){
              FirebaseUser user = firebaseAuth.getCurrentUser();
              Context context = getApplicationContext();

              if (user != null) {
                  //user is signed in
                  Toast.makeText(context, "You're now signed in. Welcome to GuardianO", Toast.LENGTH_SHORT).show();
                  Log.d(TAG, "onAuthStateChanged:signed_in: " + user.getUid());
              }
              else{
                  //User is signed out
                  GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                          .requestIdToken(getString(R.string.default_web_client_id))
                          .requestEmail()
                          .build();
                  Log.d(TAG, "onAuthStateChanged:signed_out");
              }
          }
        };
    }
@Override
protected void onResume(){
    super.onResume();
    mFirebaseAuth.addAuthStateListener(mAuthStateListener);
}
    @Override
    protected void onPause(){
        super.onPause();
        if(mAuthStateListener != null){
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_CALENDAR: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // calendar-related task you need to do.
                    new InsertParentingSchedule().execute();
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(PLAN_NAME, planName);
    }
    @Override
    public void onRestoreInstanceState(Bundle savedState){
        super.onRestoreInstanceState(savedState);
        //tvPlanName = savedState.getString("tvplanName");
        planName = savedState.getString(PLAN_NAME);
        mPlanName = (EditText)findViewById(R.id.editPlanName);
        mPlanName.setText(planName);
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
    //TODO: Temperarly save state here
                boolean done = false;
                Context context = getApplicationContext();
                if (actionId == EditorInfo.IME_ACTION_NEXT) {

                    String text = v.getText().toString();
                    //Todo: Remove this toast
                    //Toast.makeText(context, text, Toast.LENGTH_SHORT).show();

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
                    //TODO: remove this toast
                    // Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
                    done = true;

                    switch (v.getId()) {
                        case text_child_DOB:
                            childDOB = text;
                            break;
                    }
                }

                if (done) {

                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.RESULT_HIDDEN);
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
                    CAL_ID = plan.getPlanCalendar();
                    //tvChildDOB.setText(children.getChildDOB());
                }
                if(dataSnapshot.getKey().equals("UnderSchoolAgeScheduleWeekend")) {
                    yesRadioButton.setChecked(true);
                    noRadioButton.setChecked(false);
                    llUnderSchoolAge.setVisibility(View.VISIBLE);
                    yesRadioButton2.setChecked(false);
                    noRadioButton2.setChecked(true);
                    llSameSchedule1.setVisibility(View.VISIBLE);
                    llTimePicker1.setVisibility(View.VISIBLE);

                    UnderSchoolAgeScheduleWeekend underSchoolAgeScheduleWeekend = dataSnapshot.getValue(UnderSchoolAgeScheduleWeekend.class);
                    int selection = getSpinnerValueIndex(spinnerWeekend, underSchoolAgeScheduleWeekend.getFrequency());

                    spinnerWeekend.setSelection(selection);
                    tvweekendDropOffTimeText.setText(underSchoolAgeScheduleWeekend.getDropOffTime());
                    tvweekendPickUpTimeText.setText(underSchoolAgeScheduleWeekend.getPickUpTime());
                }
                //TODO: Set questions visible, and set weekday frequency, dropoff and pick up times
                if(dataSnapshot.getKey().equals("UnderSchoolAgeScheduleWeekday")) {
                    llSameSchedule2.setVisibility(View.VISIBLE);
                    llWeekdaySelection.setVisibility(View.VISIBLE);
                    llTimePicker2.setVisibility(View.VISIBLE);
                    buttonNext.setVisibility(View.VISIBLE);

                    UnderSchoolAgeScheduleWeekday underSchoolAgeScheduleWeekday = dataSnapshot.getValue(UnderSchoolAgeScheduleWeekday.class);
                    int selection = getSpinnerValueIndex(spinnerWeekday, underSchoolAgeScheduleWeekday.getFrequency());

                    spinnerWeekday.setSelection(selection);
                    cbmonday.setChecked(underSchoolAgeScheduleWeekday.getMonday());
                    cbtuesday.setChecked(underSchoolAgeScheduleWeekday.getTuesday());
                    cbwednesday.setChecked(underSchoolAgeScheduleWeekday.getWednesday());
                    cbthursday.setChecked(underSchoolAgeScheduleWeekday.getThursday());
                    cbfriday.setChecked(underSchoolAgeScheduleWeekday.getFriday());
                    tvweekdayDropOffTimeText.setText(underSchoolAgeScheduleWeekday.getDropOffTime());
                    tvweekdayPickUpTimeText.setText(underSchoolAgeScheduleWeekday.getPickUpTime());

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

    private int getSpinnerValueIndex(Spinner spinner, String value) {
        int index = 0;
        for (int i = 0; i < spinner.getCount(); i++){
            if (spinner.getItemAtPosition(i).equals(value)){
                index = i;
                break;
            }
        }
        return index;
    }

    private void hideSoftKeyboard(){
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.RESULT_HIDDEN);
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();

        switch (parent.getId()) {
            case spinner_weekday_options:
                weekdayFrequency = item;
                break;
            case spinner_weekend_options:
                weekendFrequency = item;
                break;
        }
        // Showing selected spinner item/ add spinner selection
        // to DB
        //Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // when nothing is selected do nothing. Empty on purpose.
    }

    public void showTimePickerDialog(View v) {
        //DialogFragment newFragment = new TimePickerFragment();
        TimePickerFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");

        switch (v.getId()) {
            case button_drop_off:
                newFragment.setTvText(tvweekendDropOffTimeText);
                //weekendDropOffTime = tvweekendDropOffTimeText.getText().toString();
                break;
            case button_pick_up:
                newFragment.setTvText(tvweekendPickUpTimeText);
                //weekdayDropOffTime = time;
                break;
            case button_drop_off_weekday:
                newFragment.setTvText(tvweekdayDropOffTimeText);
                //weekdayDropOffTime = time;
                break;
            case button_pick_up_weekday:
                newFragment.setTvText(tvweekdayPickUpTimeText);
                //weekdayPickUpTime = time;
                break;
        }
    }

    public void onNextButtonClicked(View v) {

        //Set values to views
        weekendDropOffTime = tvweekendDropOffTimeText.getText().toString();
        weekendPickUpTime = tvweekendPickUpTimeText.getText().toString();
        weekdayDropOffTime = tvweekdayDropOffTimeText.getText().toString();
        weekdayPickUpTime = tvweekdayPickUpTimeText.getText().toString();

        //Create a calendar if no ID exists
        if(CAL_ID == 0){

            CAL_ID = calendarAccess.createCalendar();
            calendarAccess.setCalId(CAL_ID);
        }

        //TODO: Call all DB transactions here. Figure out save state in case app closes local save
        Children children = new Children(childName, childDOB);
        Plan plan = new Plan(planName, CAL_ID);

        USAWeekdayEventId = calendarAccess.createUnderSchoolAgeScheduleWeekday(weekdayFrequency,mon, tues, wed, thur, fri, weekdayPickUpTime, weekdayDropOffTime);
        UnderSchoolAgeScheduleWeekday underSchoolAgeScheduleWeekday = new UnderSchoolAgeScheduleWeekday(weekdayFrequency,mon, tues, wed, thur, fri, weekdayPickUpTime, weekdayDropOffTime, USAWeekdayEventId);
        USAWeekendEventId = calendarAccess.createUnderSchoolAgeScheduleWeekend(weekendFrequency,weekendPickUpTime, weekendDropOffTime);
        UnderSchoolAgeScheduleWeekend underSchoolAgeScheduleWeekend = new UnderSchoolAgeScheduleWeekend(weekendFrequency,weekendPickUpTime, weekendDropOffTime);
        Toast.makeText(getApplicationContext(), "Calendar events have been added", Toast.LENGTH_SHORT).show();
        //can restore the data. WHen next is clicked all transactions are pushed to db.
        //Call to save Plan name
        mDatabaseReference = mfirebaseDatabase.getReference().child("User/Plan");
        mDatabaseReference.setValue(plan);

        //Call to save child info
        mDatabaseReference = mfirebaseDatabase.getReference().child("User/Children");
        mDatabaseReference.setValue(children);

        //Call to save Under age weekend schedule
        mDatabaseReference = mfirebaseDatabase.getReference().child("User/UnderSchoolAgeScheduleWeekend");
        mDatabaseReference.setValue(underSchoolAgeScheduleWeekend);

        //Call to save under age weekday schedule
        mDatabaseReference = mfirebaseDatabase.getReference().child("User/UnderSchoolAgeScheduleWeekday");
        mDatabaseReference.setValue(underSchoolAgeScheduleWeekday);

    }
    public void bottomNavigationViewListener() {

        final Context context = this;

        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(

                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_calendar:
                                Intent intent1 = new Intent(context, ParentingPlanCalendar.class);
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

        //Check which radio button was clicked
        switch (view.getId()) {
            case R.id.yes_radio_button:
                if (checked) {
                    //Make use school schedule  Q visible
                    llUnderSchoolAge.setVisibility(View.VISIBLE);
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

                    //Show next button
                    buttonNext.setVisibility(View.VISIBLE);
                }
                break;
        }

    }

    public void onWeekdayCheckboxClicked(View view) {
        boolean checked = ((CheckBox) view).isChecked();

        //Check which checkbox was clicked
        switch (view.getId()) {
            case checkbox_Monaday:
                mon = checked;
                break;
            case checkbox_Tuesday:
                tues = checked;
                break;
            case checkbox_Wednesday:
                wed = checked;
                break;
            case checkbox_Thursday:
                thur = checked;
                break;
            case checkbox_Friday:
                fri= checked;
                break;
        }
    }
    public void addImageButtonClicked(View view) {

        //ImageButton ibAdd = (ImageButton) this.findViewById(R.id.image_button_add_remove);


    }
    public class InsertParentingSchedule extends AsyncTask<Void, Void, Cursor> {
        @Override
        protected Cursor doInBackground(Void... params) {
           /* //Get the content resolver
            ContentResolver resolver = getContentResolver();
            if(checkPermission("android.permission.WRITE_CALENDAR",android.os.Process.myPid(), android.os.Process.myUid())
            {
                Cursor cursor = resolver.bulkInsert(CalendarContract.CONTENT_URI, );

            }*/
            Cursor calCursor;
            String[] projection =
                    new String[]{
                            CalendarContract.Calendars._ID,
                            CalendarContract.Calendars.NAME,
                            CalendarContract.Calendars.ACCOUNT_NAME,
                            CalendarContract.Calendars.ACCOUNT_TYPE};
            try {
                calCursor = getContentResolver().query(CalendarContract.Calendars.CONTENT_URI,
                        projection,
                        CalendarContract.Calendars.VISIBLE + " = 1",
                        null,
                        CalendarContract.Calendars._ID + " ASC");
                if (calCursor.moveToFirst()) {
                    do {
                        long id = calCursor.getLong(0);
                        String displayName = calCursor.getString(1);
                        Log.i(TAG, "Calendar names: " + displayName + "," + id);
                    } while (calCursor.moveToNext());
                }
            }
            catch (SecurityException e){
                Log.e(TAG, "Error occured: " + e.getStackTrace());
               return null;
            }

            return calCursor;
        }
        // Invoked on UI thread
        @Override
        protected void onPostExecute(Cursor cursor) {
            super.onPostExecute(cursor);

           if(cursor == null){
               Toast.makeText(getApplicationContext(),"Permission to calendar needed", Toast.LENGTH_SHORT).show();
           }
        }
    }
}
