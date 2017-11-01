package com.c01;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import Database.DatabaseDriver.DatabaseSelectHelper;
import android.widget.Toast;
import Database.DatabaseDriver.DatabaseInsertHelper;

import android.content.Context;
import Exceptions.InvalidNameException;
import android.text.TextUtils;
import generics.EnumMapRoles;
import generics.Roles;
import user.*;




public class MainActivity extends AppCompatActivity {

    String strUsernum;
    String strPass;
    Context context;


    public void showAlert() {
        final AlertDialog.Builder myAlert = new AlertDialog.Builder(this);
        myAlert.setMessage("ID or password was incorrect")
                .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        myAlert.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this.getApplicationContext();

        if (DatabaseSelectHelper.getRoles(this.getApplicationContext()).size() < 1) {
            try {
                DatabaseInsertHelper.insertRole("PROF", this.getApplicationContext());
                DatabaseInsertHelper.insertRole("STUDENT", this.getApplicationContext());
                EnumMapRoles roleMap = new EnumMapRoles(this.getApplicationContext());
                int id = DatabaseInsertHelper.insertNewUser("ProfA", 40, "123street", roleMap.get(Roles.PROF), "123", this.getApplicationContext());
                int id3 = DatabaseInsertHelper.insertNewUser("StudentA", 19, "123street", roleMap.get(Roles.STUDENT), "123", this.getApplicationContext());
                Toast.makeText(getApplicationContext(), String.valueOf(id), Toast.LENGTH_LONG).show();
            } catch (InvalidNameException e) {
                Toast.makeText(getApplicationContext(), String.valueOf(e), Toast.LENGTH_LONG).show();
            }
        }

        Button loginButton = (Button) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent nextpage;
                if (useridauthen1(context)) {
                    EditText etUserName = (EditText) findViewById(R.id.loginInput);
                    int id = Integer.valueOf(etUserName.getText().toString());
                    EnumMapRoles roleMap = new EnumMapRoles(context);
                    User user = DatabaseSelectHelper.getUserDetails(id, context);
                    if (user.getRoleId() == roleMap.get(Roles.PROF)) {
                        nextpage = new Intent(MainActivity.this, InstructorMenu.class);
                        startActivity(nextpage);
                    } else if (user.getRoleId() == roleMap.get(Roles.STUDENT)) {
                        nextpage = new Intent(MainActivity.this, MainMenu.class);
                        startActivity(nextpage);
                    }
                }
            }
        });
    }

    private boolean useridauthen1(Context context) {
        EditText etUserName = (EditText) findViewById(R.id.loginInput);
        strUsernum = etUserName.getText().toString();
        EditText etUserPass = (EditText) findViewById(R.id.passwordInput);
        strPass = etUserPass.getText().toString();
        boolean useridauthen = true;
        boolean passwordauthen = true;
        if (TextUtils.isEmpty(strUsernum)) {
            etUserName.setError("No UserId");
            useridauthen = false;
        }
        if (TextUtils.isEmpty(strPass)) {
            etUserPass.setError("No password");
            passwordauthen = false;
        }
        boolean passed = false;
        if (passwordauthen && useridauthen) {
            passed = login(context);
        }
        return (passed);
    }

    private boolean login(Context context) {
        int id = 0;
        String pass;
        EditText etUserName = (EditText) findViewById(R.id.loginInput);
        id = Integer.valueOf(etUserName.getText().toString());
        //Toast.makeText(getApplicationContext(), String.valueOf(id), Toast.LENGTH_LONG).show();
        EditText etUserPass = (EditText) findViewById(R.id.passwordInput);
        pass = etUserPass.getText().toString();
        // Create a map of all roles in the database
        EnumMapRoles roleMap = new EnumMapRoles(context);
        User user = DatabaseSelectHelper.getUserDetails(id, context);
        //Toast.makeText(getApplicationContext(), String.valueOf(user), Toast.LENGTH_LONG).show();
        if ((user != null) && (user.getRoleId() == roleMap.get(Roles.PROF))) {
            // Authenticate inputed password
            Prof prof = (Prof) DatabaseSelectHelper.getUserDetails
                    (id, context);
            boolean passed = prof.authenticate(pass, context);
            if (passed) {
                // Allow Prof
                return true;
            } else {
                etUserPass.setError("Invalid Prof password");
            }
        } else if ((user != null) && (user.getRoleId() == roleMap.get(Roles.STUDENT))) {
            // Authenticate inputed password
            Student student = (Student) DatabaseSelectHelper.getUserDetails
                    (id, context);
            boolean passed = student.authenticate(pass, context);
            if (passed) {
                // Allow student
                return true;
            } else {
                etUserPass.setError("Invalid Student password");
            }
        }
        return false;
    }




    public void displayInformation(String info) {
        final AlertDialog.Builder myAlert = new AlertDialog.Builder(this);
        myAlert.setMessage(info);
        myAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        myAlert.show();
    }

    public void getCreds() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText idBox = new EditText(this);
        idBox.setHint("Enter ID of Student");
        layout.addView(idBox);

        final EditText passwordBox = new EditText(this);
        passwordBox.setHint("Enter password");
        layout.addView(passwordBox);


        dialog.setView(layout);

        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
//                id = Integer.parseInt(idBox.getText().toString());
//                password = passwordBox.getText().toString();
            }
        });
        dialog.show();
    }


}

