package com.c01;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Button;


public class MainMenu extends AppCompatActivity {
    String name;
    String address;
    int age;
    String password;
    Button addStudent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        addStudent = (Button) findViewById(R.id.addStudentButton);

        addStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainMenu.this, addStudent.class);
                startActivity(i);
            }
        });
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

//    public void getCreds() {
//        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
//        LinearLayout layout = new LinearLayout(this);
//        layout.setOrientation(LinearLayout.VERTICAL);
//
//        final EditText nameBox = new EditText(this);
//        nameBox.setHint("Enter name of student");
//        layout.addView(nameBox);
//        dialog.setView(layout);
//
//        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                name = nameBox.getText().toString();
//            }
//        });
//        dialog.show();
//    }


}
