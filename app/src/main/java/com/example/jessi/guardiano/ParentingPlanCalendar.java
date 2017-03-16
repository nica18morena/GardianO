package com.example.jessi.guardiano;

import android.content.Context;
import android.content.Intent;
import android.icu.util.Calendar;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.jessi.guardiano.CalendarAccess.CalendarAccess;

public class ParentingPlanCalendar extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        Context context = this;
        CalendarAccess calendarAccess = new CalendarAccess(context);
        calendarAccess.createCalendar();
        //CalendarAccess calendarAccess = CalendarAccess.getInstance(context);

        //add data
        //calendarAccess.addUnderSchoolAgeScheduleWeekday(context);
        //call bottom navigation
        bottomNavigationViewListener();
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
}
