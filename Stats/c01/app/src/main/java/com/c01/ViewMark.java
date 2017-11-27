package com.c01;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import Database.DatabaseDriver.DatabaseInsertHelper;
import Database.DatabaseDriver.DatabaseSelectHelper;
import generics.EnumMapRoles;
import generics.Roles;
import user.User;

/**
* The activity for viewing marks.
*/
public class ViewMark extends AppCompatActivity {

    /**
    * Starts the activity.
    * @param savedInstanceState The data it most recently supplied on
    * @return No return value
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_mark);
        Context context = this.getApplicationContext();
        displayMarks(context);

    }

    /**
    * Displays marks.
    * @param context The context containing the things being created
    * @exception e Any exception
    * @return No return value
    */
    public void displayMarks(Context context){
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
                    if (DatabaseSelectHelper.getMark(user.getId(), context) != -1){
                        markHolder = String.valueOf(DatabaseSelectHelper.getMark(user.getId(), context));
                    } else {
                        markHolder = "N/A";
                    }
                    wholeHolder = user.getName() + ": " + markHolder;
                    displayer.add(wholeHolder);
                }
                ctr++;
            }
        } catch (Exception e) {

        }

        ListAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, displayer);
        ListView view = (ListView) findViewById(R.id.viewMarkId);
        view.setAdapter(adapter);

    }

}
