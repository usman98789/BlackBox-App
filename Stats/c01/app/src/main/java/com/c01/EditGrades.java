package com.c01;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import Database.DatabaseDriver.DatabaseInsertHelper;
import Database.DatabaseDriver.DatabaseSelectHelper;
import generics.EnumMapRoles;
import generics.Roles;
import user.User;

public class EditGrades extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_grades);
        Context context = this.getApplicationContext();
        displayMarks(context);

    }

    public void displayMarks(Context context){
        EnumMapRoles roleMap = new EnumMapRoles(context);
        ArrayList<String> displayer = new ArrayList<>();
        int ctr = 1;
        boolean loop = true;
        try {
            while (loop) {
                User user = DatabaseSelectHelper.getUserDetails(ctr, context);
                if (user.getRoleId() == roleMap.get(Roles.STUDENT)) {
                    displayer.add(user.getName());
                }
                ctr++;
            }
        } catch (Exception e) {

        }

        ListAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, displayer);
        ListView view = (ListView) findViewById(R.id.EditMark);
        TextView textView = (TextView)findViewById(R.id.textView7);

        view.setAdapter(adapter);
        view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(view.getContext(),ProblemSetList.class);
                String data = (String)parent.getItemAtPosition(position);
                i.putExtra("name", data);
                startActivity(i);
            }
        });

    }

}
