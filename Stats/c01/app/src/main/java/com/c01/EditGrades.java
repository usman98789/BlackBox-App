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

/**
* The activity of editting grades.
*/
public class EditGrades extends AppCompatActivity {

    /**
    * Starts the activity.
    * @param savedInstanceState The data it most recently supplied on
    * @return No return value
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_grades);
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
            
            /**
            * Responds when an item in AdapterView has been clicked.
            * @param parent The AdapterView where the click happened
            * @param view The view whin the AdapterView that was clicked
            * @param position The position of the view in the adapter
            * @param id The row id of the item that was clicked
            * @exception e Any exception
            * @return No return value
            */
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(view.getContext(),ProblemSetList.class);
                String data = (String) parent.getItemAtPosition(position);
                i.putExtra("name", data);
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
        Intent intent = new Intent(getApplicationContext(), InstructorMenu.class);
        startActivity(intent);
    }
}
