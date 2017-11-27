package com.c01;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import Database.DatabaseDriver.DatabaseInsertHelper;
import Database.DatabaseDriver.DatabaseSelectHelper;
import Database.DatabaseDriver.DatabaseUpdateHelper;
import Exceptions.InvalidAssignmentException;
import Exceptions.InvalidIdException;
import Exceptions.InvalidMarkException;
import generics.EnumMapRoles;
import generics.Roles;
import user.User;

/**
* The activity for displaying marks.
*/
public class DialogMarks extends AppCompatActivity {

    /**
    * Starts the activity.
    * @param savedInstanceState The data it most recently supplied on
    * @return No return value
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_marks);
        Context context = this.getApplicationContext();
        displayMarks(context);
    }

    /**
    * Displays marks.
    * @param context The context containing the things being created
    * @exception InvalidMarkException, InvalidIdException, InvalidAssignmentException
    * @return No return value
    */
    public void displayMarks(Context context){
        int aNum = getIntent().getIntExtra("aNum", 0);
        String userName = getIntent().getStringExtra("name");

        EnumMapRoles roleMap = new EnumMapRoles(context);
        ArrayList<String> displayer = new ArrayList<>();
        int ctr = 1;
        boolean loop = true;
        try {
            while (loop) {
                User user = DatabaseSelectHelper.getUserDetails(ctr, context);
                if (user.getRoleId() == roleMap.get(Roles.STUDENT)) {
                    if (user.getName().equals(userName)){

                        Button button = (Button) findViewById(R.id.MarkSet);
                        context = this.getApplicationContext();
                        button.setOnClickListener(new View.OnClickListener() {

                            public void onClick(View view) {
                                final EditText edit =  (EditText) findViewById(R.id.NewMark);
                                String Mark = (String) edit.getText().toString();
                                if (TextUtils.isEmpty(Mark)) {
                                    onBackPressed();
                                } else {
                                    Double mark = Double.parseDouble(Mark);
                                    try {
                                        if (DatabaseSelectHelper.getAssignmentMark(user.getId(), aNum, getApplicationContext()) == -1){
                                            DatabaseInsertHelper.insertAssignmentMark(user.getId(), mark, aNum, getApplicationContext());
                                        } else {
                                            DatabaseUpdateHelper.updateAssignmentMark(user.getId(), mark, aNum, getApplicationContext());
                                        }
                                    } catch (InvalidMarkException e) {
                                        e.printStackTrace();
                                    } catch (InvalidIdException e) {
                                        e.printStackTrace();
                                    } catch (InvalidAssignmentException e) {
                                        e.printStackTrace();
                                    }
                                    Toast.makeText(getApplicationContext(), "Mark set", Toast.LENGTH_LONG).show();
                                    onBackPressed();
                                }
                            }
                        });

                    }
                    displayer.add(user.getName());
                }
                ctr++;
            }
        } catch (Exception e) {

        }

    }

    /**
    * Responds when user presses the back key.
    * @return No return value
    */
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), EditGrades.class);
        startActivity(intent);

    }
}
