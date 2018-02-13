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

import Database.DatabaseDriver.DatabaseSelectHelper;
import android.widget.Toast;
import Database.DatabaseDriver.DatabaseInsertHelper;

import android.content.Context;
import Exceptions.InvalidNameException;
import android.text.TextUtils;
import generics.EnumMapRoles;
import generics.Roles;
import user.*;

/**
* The activity for main activity
*/
public class MainActivity extends AppCompatActivity {

    String strUsernum;
    String strPass;
    Context context;
    String studentName;
    String profName;

    /**
    * Starts the activity.
    * @param savedInstanceState The data it most recently supplied on
    * @exception e InvalidNameException
    * @return No return value
    */
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
                int id = DatabaseInsertHelper.insertNewUser("ProfA", 40, roleMap.get(Roles.PROF), "123", this.getApplicationContext());
                int id4 = DatabaseInsertHelper.insertNewUser("Student1", 19, roleMap.get(Roles.STUDENT), "123", this.getApplicationContext());
                int id5 = DatabaseInsertHelper.insertNewUser("Student2", 21, roleMap.get(Roles.STUDENT), "123", this.getApplicationContext());
                Toast.makeText(getApplicationContext(), "New", Toast.LENGTH_LONG).show();
            } catch (InvalidNameException e) {
                Toast.makeText(getApplicationContext(), String.valueOf(e), Toast.LENGTH_LONG).show();
            }
        }

        Button loginButton = (Button) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {

            /**
            * Responds when a click happened.
            * @param view The content to display
            * @return No return value
            */
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
                        nextpage.putExtra("name", profName);
                        startActivity(nextpage);
                    } else if (user.getRoleId() == roleMap.get(Roles.STUDENT)) {
                        nextpage = new Intent(MainActivity.this, StudentMenu.class);
                        nextpage.putExtra("id", etUserName.getText().toString());
                        nextpage.putExtra("name", studentName);
                        startActivity(nextpage);
                    }
                }
            }
        });
    }

    /**
    * Creates a user.
    * @param context The context
    * @return boolean True if userID and password match.
    */
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

    /**
    * Login.
    * @param context The context
    * @return boolean True if login.
    */
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
            profName = prof.getName();
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
            studentName = student.getName();
            boolean passed = student.authenticate(pass, context);
            if (passed) {
                // Allow student
                return true;
            } else {
                etUserPass.setError("Invalid Student password");
            }
        } else {
            etUserName.setError("This UserId has not been registered");
        }
        return false;
    }

    /**
    * To display information.
    * @param info The information to be displayed
    * @return No return value
    */
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

    /**
    * Gets creds.
    * @return No return value
    */
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
            
            /**
            * Responds when a click happened.
            * @param dialogInterface The dialog that received the click
            * @param i The button that was clicked or the position of the item clicked
            * @return No return value
            */
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        dialog.show();
    }

    /**
    * Responds when user presses the back key.
    * @return No return value
    */
    @Override
    public void onBackPressed() {
        // Do Here what ever you want do on back press;
    }
}
