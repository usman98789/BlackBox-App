package com.c01;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import Database.DatabaseDriver.DatabaseInsertHelper;
import Exceptions.InvalidNameException;
import generics.EnumMapRoles;
import generics.Roles;


public class addStudent extends AppCompatActivity {

    Context context;
    String name;
    int age;
    String pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);

        Button button = (Button) findViewById(R.id.addStudent);
        context = this.getApplicationContext();
        button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                if (useridauthen1(context)) {
                    Intent i = new Intent(addStudent.this, StudentMenu.class);
                    Toast.makeText(getApplicationContext(), "Student added", Toast.LENGTH_LONG).show();
                    startActivity(i);
                }
            }
        });
    }

    public boolean useridauthen1(Context context) {
        boolean useridAuthen = true;
        boolean passwordAuthen = true;
        boolean ageAuthen = true;

        EditText etUserName = (EditText) findViewById(R.id.name);
        name = etUserName.getText().toString();
        EditText etAge = (EditText) findViewById(R.id.Age);
        age = Integer.valueOf(etAge.getText().toString());
        EditText etPassword = (EditText) findViewById(R.id.password);
        pass = etPassword.getText().toString();

        if (TextUtils.isEmpty(name)) {
            etUserName.setError("No UserId");
            useridAuthen = false;
        }
        if (TextUtils.isEmpty(String.valueOf(age))) {
            etAge.setError("No age");
            ageAuthen = false;
        }
        if (TextUtils.isEmpty(pass)) {
            etPassword.setError("No password");
            passwordAuthen = false;
        }
        boolean passed = false;
        if (useridAuthen && passwordAuthen && ageAuthen) {
            passed = insertUser(context);
        }
        return (passed);
    }

    private boolean insertUser(Context context) {

        int success = -1;
        EnumMapRoles roleMap = new EnumMapRoles(context);
        // Insert info into database
        try {
            success = DatabaseInsertHelper.insertNewUser(name, age, roleMap.get(Roles.STUDENT), pass, context);
        } catch (InvalidNameException e) {
            e.printStackTrace();
        }
        // If insert failed, print statement
        if (success == -1) {
            Toast.makeText(getApplicationContext(), "Student was not added", Toast.LENGTH_LONG).show();
            return false;
        } else {
            Toast.makeText(getApplicationContext(), "Student id is: " + String.valueOf(success),
                    Toast.LENGTH_LONG).show();
            return true;
        }


    }
}
