package com.c01;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import Database.DatabaseDriver.DatabaseInsertHelper;
import Database.DatabaseDriver.DatabaseSelectHelper;
import Exceptions.InvalidNameException;
import user.Student;


public class MainMenu extends AppCompatActivity {
    String name;
    String address;
    int age;
    String password;

    String id;

    Button viewNotes;
    Intent nextpage;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        id = getIntent().getStringExtra("id");

        viewNotes = (Button) findViewById(R.id.viewNotesButton);




        Button inbox = (Button) findViewById(R.id.email);
        inbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainMenu.this, ViewAnnouncements.class);
                i.putExtra("id", id);
                startActivity(i);
            }
        });

        viewNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextpage = new Intent(MainMenu.this, ViewNotes.class);
                startActivity(nextpage);

            }
        });

        Button logout = (Button) findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainMenu.this, MainActivity.class);
                startActivity(i);
            }
        });

        Button set = (Button) findViewById(R.id.ProblemSet);
        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainMenu.this, StudentFileInbox.class);
                startActivity(i);
            }
        });



    }

    @Override
    public void onBackPressed() {
        // Do Here what ever you want do on back press;
    }
}

