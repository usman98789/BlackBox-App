package com.c01;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import user.Student;

/**
* The activity for student menu.
*/
public class StudentMenu extends AppCompatActivity {

    String id;

    Button viewNotes;

    Intent nextpage;

    /**
    * Starts the activity.
    * @param savedInstanceState The data it most recently supplied on
    * @return No return value
    */
    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_menu);
//        TextView text = (TextView) findViewById(R.id.studentTitle);
//        final String name = getIntent().getStringExtra("name");
//        text.setText("Welcome " + name);
        id = getIntent().getStringExtra("id");
        System.out.println("Menu ID : " + id);
        viewNotes = (Button) findViewById(R.id.viewNotesButton);
        Button inbox = (Button) findViewById(R.id.email);
        inbox.setOnClickListener(new View.OnClickListener() {

            /**
            * Responds when a click happened.
            * @param view The content to display
            * @return No return value
            */
            @Override
            public void onClick(View view) {
                Intent i = new Intent(StudentMenu.this, ViewAnnouncements.class);
                i.putExtra("id", id);
                startActivity(i);
            }
        });

        viewNotes.setOnClickListener(new View.OnClickListener() {

            /**
            * Responds when a click happened.
            * @param v The content to display
            * @return No return value
            */
            @Override
            public void onClick(View v) {
                nextpage = new Intent(StudentMenu.this, ViewNotes.class);
                nextpage.putExtra("userId", Integer.valueOf(id));
                startActivity(nextpage);
            }
        });

        Button logout = (Button) findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {

            /**
            * Responds when a click happened.
            * @param view The content to display
            * @return No return value
            */
            @Override
            public void onClick(View view) {
                Intent i = new Intent(StudentMenu.this, MainActivity.class);
                startActivity(i);
            }
        });

        Button set = (Button) findViewById(R.id.ProblemSet);
        set.setOnClickListener(new View.OnClickListener() {

            /**
            * Responds when a click happened.
            * @param view The content to display
            * @return No return value
            */
            @Override
            public void onClick(View view) {
                Intent i = new Intent(StudentMenu.this, StudentFileInbox.class);

                i.putExtra("userId", Integer.valueOf(id));
                startActivity(i);
            }
        });

        Button myGrades = (Button) findViewById(R.id.CheckGradesButton);
        myGrades.setOnClickListener(new View.OnClickListener() {

            /**
            * Responds when a click happened.
            * @param view The content to display
            * @return No return value
            */
            @Override
            public void onClick(View view) {
                Intent i = new Intent(StudentMenu.this, StudentGrades.class);
                i.putExtra("userId", id);
                startActivity(i);
            }
        });

        Button assignment = (Button) findViewById(R.id.addAssignmentButton);
        assignment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(StudentMenu.this, AssignmentChoice.class);
                i.putExtra("userId", Integer.valueOf(id));
                startActivity(i);
            }
        });
    }

    /**
    * Responds when user presses the back key.
    * @return No return value
    */
    @Override
    public void onBackPressed() {
        // Do Here what ever you want do on back press;
    }
}
