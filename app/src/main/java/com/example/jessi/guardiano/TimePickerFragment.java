package com.example.jessi.guardiano;

import android.app.Dialog;
import android.app.TimePickerDialog;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * Created by jessi on 2/5/2017.
 */

public class TimePickerFragment extends DialogFragment
                                    implements TimePickerDialog.OnTimeSetListener{
    private int pickerHour, pickerMin;
    private String am_pm;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        // Do something with the time chosen by the user

        am_pm = hourOfDay > 12? "PM" : "AM";

        pickerHour = hourOfDay > 12? hourOfDay - 12 : hourOfDay;
        pickerMin = minute;

        TextView tv = (TextView) getActivity().findViewById(R.id.text_drop_off);
        tv.setText(String.valueOf(hourOfDay) +":" + String.valueOf(minute) + " "+ am_pm);
    }
}
