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


public class MainActivity extends AppCompatActivity {
    TextView loginInput;
    TextView passwordInput;
    Button loginButton;


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


        loginInput = (TextView) findViewById(R.id.loginInput);
        passwordInput = (TextView) findViewById(R.id.passwordInput);

        loginButton = (Button) findViewById(R.id.loginButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer id = Integer.parseInt(loginInput.getText().toString());
                String password = passwordInput.getText().toString();
                Intent nextpage;
                //TODO put in database stuff here for login
                if (id == 1 && password.equals("one")) {
                    nextpage = new Intent(MainActivity.this, MainMenu.class);
                    startActivity(nextpage);
//                    displayInformation("adfad");
                } else {
                    showAlert();
                }

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

    public void getCreds() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText idBox = new EditText(this);
        idBox.setHint("Enter ID of Customer");
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

