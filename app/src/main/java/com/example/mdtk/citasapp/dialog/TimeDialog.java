package com.example.mdtk.citasapp.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Locale;

public class TimeDialog extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    private static EditText txt_time;
    int hour;
    int minute;
    public static TimeDialog newInstance(View view){
        txt_time=(EditText)view;
        return(new TimeDialog());
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Use the current time as the default time in the dialog
        final Calendar c = Calendar.getInstance(Locale.getDefault());
        hour = c.get(Calendar.HOUR);
        minute = c.get(Calendar.MINUTE);
        //Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,false);

    }


    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {
        txt_time.setText(i+":"+i1);
    }
}
