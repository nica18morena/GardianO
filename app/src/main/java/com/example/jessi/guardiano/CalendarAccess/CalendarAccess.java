package com.example.jessi.guardiano.CalendarAccess;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.util.Log;

import com.example.jessi.guardiano.DataObjects.UnderSchoolAgeScheduleWeekday;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import static android.R.attr.description;

/**
 * Created by jessi on 3/7/2017.
 */

public class CalendarAccess {
    private static final String TAG = "CalendarAccess";
    private static int PROJECTION_ID_INDEX = 0;
    private static int PROJECTION_ACCOUNT_NAME_INDEX = 0;
    private static int PROJECTION_DISPLAY_NAME_INDEX = 0;
    private static int PROJECTION_OWNER_ACCOUNT_INDEX = 0;
    /**The main/basic URI for the android calendars table*/
    private static Uri CAL_URI = null;
    private static String ACCOUNT_NAME = null;
    private static String CALENDAR_NAME = null;
    private static long CAL_ID;
    /**The main/basic URI for the android events table*/
    private static Uri EVENT_URI = null;
    private static CalendarAccess mInstance = null;
    private static ContentResolver contentResolver;

   public CalendarAccess(Context ctx){
   //public CalendarAccess(){
        PROJECTION_ID_INDEX = 0;
        PROJECTION_ACCOUNT_NAME_INDEX = 1;
        PROJECTION_DISPLAY_NAME_INDEX = 2;
        PROJECTION_OWNER_ACCOUNT_INDEX = 3;
        /**The main/basic URI for the android calendars table*/
        CAL_URI = CalendarContract.Calendars.CONTENT_URI;
        ACCOUNT_NAME = "GuardianO";
        CALENDAR_NAME = "Parenting Schedule";
        /**The main/basic URI for the android events table*/
        EVENT_URI = CalendarContract.Events.CONTENT_URI;
       contentResolver = ctx.getContentResolver();
       // mInstance.createCalendar(ctx);
    }

    /*public static CalendarAccess getInstance(Context ctx){

        if (mInstance == null){
            mInstance =  new CalendarAccess(ctx);
        }
        return mInstance;
    }*/
    /**Builds the Uri for your Calendar in android database (as a Sync Adapter)*/
    private static Uri buildCalUri() {
        return CAL_URI
                .buildUpon()
                .appendQueryParameter(CalendarContract.CALLER_IS_SYNCADAPTER, "true")
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_NAME, ACCOUNT_NAME)
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_TYPE,
                        CalendarContract.ACCOUNT_TYPE_LOCAL)
                .build();
    }

    /**Creates the values the new calendar will have*/
    private static ContentValues buildNewCalContentValues() {

        final ContentValues cv = new ContentValues();
        cv.put(CalendarContract.Calendars.ACCOUNT_NAME, ACCOUNT_NAME);
        cv.put(CalendarContract.Calendars.ACCOUNT_TYPE, CalendarContract.ACCOUNT_TYPE_LOCAL);
        cv.put(CalendarContract.Calendars.NAME, CALENDAR_NAME);
        cv.put(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME, CALENDAR_NAME);
        cv.put(CalendarContract.Calendars.CALENDAR_COLOR, 0xEA8561);
        //user can only read the calendar
        cv.put(CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL, CalendarContract.Calendars.CAL_ACCESS_OWNER);
        cv.put(CalendarContract.Calendars.OWNER_ACCOUNT, ACCOUNT_NAME);
        cv.put(CalendarContract.Calendars.CALENDAR_TIME_ZONE, "America/Los_Angeles");
        cv.put(CalendarContract.Calendars.VISIBLE, 1);
        cv.put(CalendarContract.Calendars.SYNC_EVENTS, 1);
        return cv;
    }
    /**Create and insert new calendar into android database
     * @param ctx The context (e.g. activity)
     */
    public long createCalendar() {
        //contentResolver = ctx.getContentResolver();
        final ContentValues cv = buildNewCalContentValues();

        Uri.Builder builder = CalendarContract.Calendars.CONTENT_URI.buildUpon();
        builder.appendQueryParameter(CalendarContract.Calendars.ACCOUNT_NAME, "com.example.jessi.guardiano");
        builder.appendQueryParameter(CalendarContract.Calendars.ACCOUNT_TYPE, CalendarContract.ACCOUNT_TYPE_LOCAL);
        builder.appendQueryParameter(CalendarContract.CALLER_IS_SYNCADAPTER, "true");

        Uri uri = contentResolver.insert(builder.build(), cv);
        CAL_ID = Long.parseLong(uri.getLastPathSegment());
        /*Uri calUri = buildCalUri();
        //insert the calendar into the database
        //cr.insert(calUri, cv);

        Uri newUri = cr.insert(buildCalUri(), cv);
        CAL_ID = Long.parseLong(newUri.getLastPathSegment());*/
        //setEvent();
        return CAL_ID;
    }

    public void setEvent(){
        if (CAL_ID == -1){
            return;
        }

        Calendar calendar = new GregorianCalendar(2017, 3, 14);
        calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        long start = calendar.getTimeInMillis();
        ContentValues values = new ContentValues();

        values.put(CalendarContract.Events.DTSTART, start);
        values.put(CalendarContract.Events.DTEND, start);
        values.put(CalendarContract.Events.RRULE, "FREQ=DAILY;COUNT=20;BYDAY=MO,TU,WE,TH,FR;WKST=MO");
        values.put(CalendarContract.Events.TITLE, "My Time");
        values.put(CalendarContract.Events.EVENT_LOCATION, "Home");
        values.put(CalendarContract.Events.CALENDAR_ID, CAL_ID);
        values.put(CalendarContract.Events.EVENT_TIMEZONE, "America/Los_Angeles");
        values.put(CalendarContract.Events.DESCRIPTION, "Time to spend with kiddos");
        values.put(CalendarContract.Events.ACCESS_LEVEL, CalendarContract.Events.ACCESS_PRIVATE);
        values.put(CalendarContract.Events.SELF_ATTENDEE_STATUS, CalendarContract.Events.STATUS_CONFIRMED);
        values.put(CalendarContract.Events.ALL_DAY, 1);
        values.put(CalendarContract.Events.ORGANIZER, "Bwahhahaha");
        values.put(CalendarContract.Events.GUESTS_CAN_INVITE_OTHERS, 1);
        values.put(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY);

        try{
            Uri uri = contentResolver.insert(CalendarContract.Events.CONTENT_URI, values);
            long eventID = new Long(uri.getLastPathSegment());
        }
        catch (SecurityException e){
            Log.e(TAG, "Error occured: " + e.getStackTrace());
        }
    }
    public void addUnderSchoolAgeScheduleWeekday(//UnderSchoolAgeScheduleWeekday schedule,
                                                        Context ctx){
        //Get schedule information
        /*schedule.getDropOffTime();
        schedule.getPickUpTime();

        schedule.getFrequency();*/
        String recurrence = "FREQ=WEEKLY;COUNT=10;WKST=SU";

        String title = "My Time";
        String description = "My time with kid";
        ContentResolver cr = ctx.getContentResolver();
        ContentValues cv = new ContentValues();
        cv.put(CalendarContract.Events.CALENDAR_ID, CAL_ID);
        cv.put(CalendarContract.Events.TITLE, title);
        cv.put(CalendarContract.Events.RRULE,recurrence);
        cv.put(CalendarContract.Events.RDATE, description);

        //cv.put(CalendarContract.Events.DTSTART, dtstart);
        //cv.put(CalendarContract.Events.DTEND, dtend);
        //cv.put(CalendarContract.Events.EVENT_LOCATION, location);
        cv.put(CalendarContract.Events.DESCRIPTION, description);
        cv.put(CalendarContract.Events.EVENT_TIMEZONE, "America/Los_Angeles");
        cr.insert(EVENT_URI, cv);
    }
    public static void addUnderSchoolAgeScheduleWeekend(){

    }

    /**Builds the Uri for events (as a Sync Adapter)*/
    public static Uri buildEventUri() {
        return EVENT_URI
                .buildUpon()
                .appendQueryParameter(CalendarContract.CALLER_IS_SYNCADAPTER, "true")
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_NAME, ACCOUNT_NAME)
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_TYPE,
                        CalendarContract.ACCOUNT_TYPE_LOCAL)
                .build();
    }

    /**Finds an event based on the ID
     * @param ctx The context (e.g. activity)
     * @param id The id of the event to be found
     */
    public static void getEventByID(Context ctx, long id) {
        ContentResolver cr = ctx.getContentResolver();
        //Projection array for query (the values you want)
        final String[] PROJECTION = new String[] {
                CalendarContract.Events._ID,
                CalendarContract.Events.TITLE,
                CalendarContract.Events.DESCRIPTION,
                CalendarContract.Events.EVENT_LOCATION,
                CalendarContract.Events.DTSTART,
                CalendarContract.Events.DTEND,
        };
        final int ID_INDEX = 0, TITLE_INDEX = 1, DESC_INDEX = 2, LOCATION_INDEX = 3,
                START_INDEX = 4, END_INDEX = 5;
        long start_millis=0, end_millis=0;
        String title=null, description=null, location=null;
        final String selection = "("+ CalendarContract.Events.OWNER_ACCOUNT+" = ? AND "+ CalendarContract.Events._ID+" = ?)";
        final String[] selectionArgs = new String[] {ACCOUNT_NAME, id+""};
        Cursor cursor = cr.query(buildEventUri(), PROJECTION, selection, selectionArgs, null);
        //at most one event will be returned because event ids are unique in the table
        if (cursor.moveToFirst()) {
            id = cursor.getLong(ID_INDEX);
            title = cursor.getString(TITLE_INDEX);
            description = cursor.getString(DESC_INDEX);
            location = cursor.getString(LOCATION_INDEX);
            start_millis = cursor.getLong(START_INDEX);
            end_millis = cursor.getLong(END_INDEX);

            //do something with the values...

        }
        cursor.close();
    }
}
