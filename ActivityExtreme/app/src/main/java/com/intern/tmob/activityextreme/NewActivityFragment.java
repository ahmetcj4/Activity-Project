package com.intern.tmob.activityextreme;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.mustafa.myapplication.backend.myApi.MyApi;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class NewActivityFragment extends Fragment {
    private static MyApi myApiService = null;
    Spinner spinner;
    Button newdate,newtime,newcreate;
    static TextView date,time,title,details;
    static String activityType="",activityTitle="", activityDetails="", activityDate="",activityTime="";
    static String activityName=SplashActivityFragment.mProfile.getFirstName();
    static String activitySurname=SplashActivityFragment.mProfile.getLastName();
    static String activityFid=SplashActivityFragment.mProfile.getId();
//    static String activityName="",activitySurname="",activityFid="";
    View rootView;
    public NewActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_new, container, false);
        findViewsByID();
        addItemsOnSpinner();
        return rootView;
    }

    private void findViewsByID() {
        spinner = (Spinner) rootView.findViewById(R.id.spinner);
        date = (TextView) rootView.findViewById(R.id.activityDate);
        time = (TextView) rootView.findViewById(R.id.activityTime);
        details = (TextView) rootView.findViewById(R.id.details);
        title = (TextView) rootView.findViewById(R.id.title);
        newdate = (Button) rootView.findViewById(R.id.new_date);
        newtime = (Button) rootView.findViewById(R.id.new_time);
        newcreate = (Button) rootView.findViewById(R.id.createActivity);

        newdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getFragmentManager(), "datePicker");
            }
        });

        newtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new TimePickerFragment();
                newFragment.show(getFragmentManager(), "timePicker");
            }
        });

        newcreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityTitle = title.getText().toString();
                activityDetails = details.getText().toString();
                activityType = spinner.getSelectedItem().toString();
                if (check())
                    new ActivityCreate().execute(getActivity());
            }
        });
    }

    private void addItemsOnSpinner() {
        List<String> list = new ArrayList<String>();
        list.add("Spor");
        list.add("Kültür-Sanat");
        list.add("Gezi");
        list.add("Eğlence");
        list.add("Ders");
        list.add("Araç-Vasıta");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,list);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
    }

    private boolean check() {
        if(activityType == "" || activityTitle == "" || activityDetails == "" || activityTime == ""
                || activityDate == "") {
            Toast.makeText(getActivity(), "Tüm alanları doldurmanız gerekiyor.", Toast.LENGTH_LONG).show();
            return false;
        }
        Calendar c = Calendar.getInstance();
        String sDate = c.get(Calendar.YEAR) + "-" + c.get(Calendar.MONTH)
                + "-" + c.get(Calendar.DAY_OF_MONTH) + " " + c.get(Calendar.HOUR_OF_DAY)
                + ":" + c.get(Calendar.MINUTE);
        if(sDate.compareTo(activityDate + ' ' + activityTime)>0){
            Toast.makeText(getActivity(), "Gecmis tarih giremezsiniz", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

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
            activityTime = "" + hourOfDay + ":" + minute;
            time.setText(activityTime);
        }
    }
    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            activityDate = "" + year + "-" + month + "-" + day;
            date.setText("" + day + "." + month + "." + year);
        }
    }
    private class ActivityCreate extends AsyncTask<Context,Void,String> {
        Context mContext;
        @Override
        protected String doInBackground(Context... params) {
            if(myApiService == null) {  // Only do this once
                MyApi.Builder builder = new MyApi.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                        .setRootUrl("https://absolute-disk-105007.appspot.com/_ah/api/");
                myApiService = builder.build();
            }
            mContext = params[0];

            try {
                Log.i("asdfasdf",SplashActivityFragment.mProfile.getProfilePictureUri(100,100).toString());
                myApiService.createActivity(activityType,activityTitle, activityDetails,
                        activityDate,activityTime,activityName,activitySurname,activityFid,
                        SplashActivityFragment.mProfile.getProfilePictureUri(100,100).toString()).execute();
            } catch (IOException e) {
                return "Error";
            }
            return "Aktivite eklendi.";
        }

        @Override
        protected void onPostExecute(String s) {
            Toast.makeText(mContext,s,Toast.LENGTH_LONG).show();
        }
    }
}
