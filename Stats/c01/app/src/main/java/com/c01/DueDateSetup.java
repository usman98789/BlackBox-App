package com.c01;

import android.app.Dialog;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.app.DatePickerDialog;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class DueDateSetup extends AppCompatActivity implements DatePickerDialog.OnDateSetListener  {

    private static String releaseDate = "";
    private static String dueDate = "";
    private static String endSemesterDate = "";
    private static String buttonSelect = "";

    private static Button releaseSetup;
    private static Button dueSetup;
    private static Button endSetup;

    private static int assign = 0;
    private static int assign_question = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_due_date_setup);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        releaseSetup = (Button) findViewById(R.id.picDate);
        dueSetup = (Button) findViewById(R.id.picDate2);
        endSetup = (Button) findViewById(R.id.picDate3);
        assign = Integer.valueOf(getIntent().getStringExtra("assign"));
        assign_question = Integer.valueOf(getIntent().getStringExtra("assign_question"));

        releaseSetup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonSelect = "Release";
                datePickerReleaseDate(view);
            }
        });

        dueSetup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonSelect = "Due";
                datePickerDueDate(view);
            }
        });

        endSetup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonSelect = "End";
                datePickerEndOfSemester(view);
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Date Setup complete", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Intent i = new Intent(DueDateSetup.this, Editor.class);
                i.putExtra("assign", String.valueOf(assign));
                i.putExtra("assign_question", String.valueOf(assign_question));
                i.putExtra("releaseDate", releaseDate);
                i.putExtra("dueDate", dueDate);
                i.putExtra("endSemesterDate", endSemesterDate);
                startActivity(i);
            }
        });
    }

    public void datePickerReleaseDate(View view){
        DatePickerFragment fragment = new DatePickerFragment();
        fragment.show(getSupportFragmentManager(), "date");
    }

    public void datePickerDueDate(View view){
        DatePickerFragment fragment = new DatePickerFragment();
        fragment.show(getSupportFragmentManager(), "date");
    }

    public void datePickerEndOfSemester(View view){
        DatePickerFragment fragment = new DatePickerFragment();
        fragment.show(getSupportFragmentManager(), "date");
    }

    private void setDateOnRelease(final Calendar calendar) {
        final DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);
        releaseDate = dateFormat.format(calendar.getTime()).toString();
    }

    private void setDateOnDueDate(final Calendar calendar) {
        final DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);
        dueDate = dateFormat.format(calendar.getTime()).toString();
    }
    private void setDateOnEndSemester(final Calendar calendar) {
        final DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);
        endSemesterDate = dateFormat.format(calendar.getTime()).toString();
    }

    /**
     * To receive a callback when the user sets the date.
     * @param view
     * @param year
     * @param month
     * @param day
     */
    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        Calendar cal = new GregorianCalendar(year, month, day);
        if (buttonSelect.compareTo("Release") == 0) {
            setDateOnRelease(cal);
        } else if (buttonSelect.compareTo("Due") == 0) {
            setDateOnDueDate(cal);
        } else if (buttonSelect.compareTo("End") == 0) {
            setDateOnEndSemester(cal);
        }
        buttonSelect = "";
    }

    /**
     * Create a DatePickerFragment class that extends DialogFragment.
     * Define the onCreateDialog() method to return an instance of DatePickerDialog
     */
    public static class DatePickerFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            return new DatePickerDialog(getActivity(), R.style.MyDatePickerTheme,(DatePickerDialog.OnDateSetListener)
                            getActivity(), year, month, day);
        }

    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(DueDateSetup.this, CreateProblemSet.class);
        startActivity(i);
    }
}
