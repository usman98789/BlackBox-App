package com.c01;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import Database.DatabaseDriver.DatabaseSelectHelper;
import generics.EnumMapRoles;
import generics.Roles;
import user.User;

public class ProblemSetList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problem_set_list);
        Context context = this.getApplicationContext();
        displayMarks(context);

    }

    public void displayMarks(Context context){
        String userName;
        userName = getIntent().getExtras().getString("name");

        EnumMapRoles roleMap = new EnumMapRoles(context);
        ArrayList<String> displayer = new ArrayList<>();
        int ctr = 1;
        String markHolder = "N/A";
        String wholeHolder = "";
        boolean loop = true;
        try {
            while (loop) {
                User user = DatabaseSelectHelper.getUserDetails(ctr, context);
                if (user.getRoleId() == roleMap.get(Roles.STUDENT)) {
                    if (user.getName().equals(userName)){
                        for (int i = 1; i < 5; i++){
                            if (DatabaseSelectHelper.getAssignmentMark(user.getId(), i, context) != -1){
                                markHolder = String.valueOf(DatabaseSelectHelper.getAssignmentMark(user.getId(), i, context));
                            } else {
                                markHolder = "N/A";
                            }
                            wholeHolder = "Assignment " + i + ":" + markHolder;
                            displayer.add(wholeHolder);
                        }
                    }
                }
                ctr++;
            }
        } catch (Exception e) {

        }

        ListAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, displayer);
        ListView view = (ListView) findViewById(R.id.ProblemSetList);
        view.setAdapter(adapter);
    }
}
