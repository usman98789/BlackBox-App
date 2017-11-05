package com.c01;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.lang.reflect.Array;
import java.util.ArrayList;

import Database.DatabaseDriver.DatabaseSelectHelper;


/**
 * Created by usman on 03/11/17.
 */

public class viewGrades extends AppCompatActivity {

    private static final String TAG = "ListDataActivity";

    DatabaseSelectHelper mDatabaseSelectHelper;

    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_grades);
        mListView = (ListView) findViewById(R.id.nav_view_grades);
        mDatabaseSelectHelper = new DatabaseSelectHelper();
        Context context = this.getApplicationContext();
        displayListView(context);
    }

    public void displayListView(Context context){
        ArrayList<String> listData = new ArrayList<>();
        int studentRoleid = 0;
        // get student ROle id
        for (int i = 0; i < DatabaseSelectHelper.getRoles(context).size(); i++){
            if (DatabaseSelectHelper.getRoles(context).get(i).toString().equalsIgnoreCase("STUDENT")){
                studentRoleid = DatabaseSelectHelper.getRoles(context).get(i);
            }
        }

        // loop over students and add to list

    }

}
