package com.c01;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import Database.DatabaseDriver.DatabaseInsertHelper;
import Database.DatabaseDriver.DatabaseSelectHelper;
import generics.EnumMapRoles;
import generics.Roles;
import user.User;

public class PostAnnouncement extends AppCompatActivity {

    Context context;
    String announcement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_announcement);

        Button button = (Button) findViewById(R.id.post);
        context = this.getApplicationContext();
        button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                checkEmpty(context);
            }
        });
    }

    public boolean checkEmpty(Context context) {
        boolean empty = false;

        EditText text = (EditText) findViewById(R.id.announceText);
        announcement = text.getText().toString();

        if (TextUtils.isEmpty(announcement)) {
            text.setError("No text entered");
            empty = true;
        }
        boolean passed = false;
        if (!empty) {
            post(context);
            passed = true;
        }
        return passed;
    }

    public void post(Context context) {
        EditText text = (EditText) findViewById(R.id.announceText);
        announcement = text.getText().toString();

        EnumMapRoles roleMap = new EnumMapRoles(context);

        int ctr = 1;
        boolean loop = true;
        try {
            while (loop) {
                User user = DatabaseSelectHelper.getUserDetails(ctr, context);
                if (user.getRoleId() == roleMap.get(Roles.STUDENT)) {
                    DatabaseInsertHelper.insertMessage(ctr, announcement, context);
                }
                ctr++;
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Announcement posted to all students", Toast.LENGTH_LONG).show();
        }


    }
}
