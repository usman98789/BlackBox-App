package com.c01;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import java.net.URL;
import java.util.List;
import org.apache.ivy.util.url.ApacheURLLister;

/**
  The activity for creating problem sets.
*/
public class CreateProblemSet extends AppCompatActivity {

    private static ListAdapter myAdapter;
    private static List serverDir;
    private static ListView fileList;
    private static File files;
    private static File[] localDir;
    private static ApacheURLLister lister;
    private static Context context;

    /**
    * Shows alert when id or password incorrect.
    * @return No return value
    */
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

    /**
    * Starts the activity.
    * @param savedInstanceState the data it most recently supplied on
    * @return No return value
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_problem_set);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context = getApplicationContext();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        Drawable myFabSrc = getResources().getDrawable(android.R.drawable.ic_input_add);
        Drawable willBeWhite = myFabSrc.getConstantState().newDrawable();
        willBeWhite.mutate().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
        fab.setImageDrawable(willBeWhite);

        Thread t = new Thread(new Runnable() {
            @Override
            
            /**
            * Lists files' name properly.
            * @exception e
            * @return No return value
            */
            public void run() {

                try {
                    URL url = new URL("https://shev:Biscut123@megumin.ga/stats/assignments");
                    lister = new ApacheURLLister();
                    serverDir = lister.listFiles(url);

                    for (int i = 0; i < serverDir.size(); i++) {
                        String temp = serverDir.get(i).toString();
                        temp = temp.replace("https://megumin.ga/stats/assignments/", "");
                        System.out.println(temp);
                        serverDir.set(i, temp);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        t.start();
        while(t.isAlive()){}

        if (!t.isAlive()) {

            myAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, serverDir);
            fileList = (ListView) findViewById(R.id.fileListView);
            if (serverDir.size() > 0) {
                fileList.setAdapter(myAdapter);
            }

            fileList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                
                /**
                * Responds when an item in AdapterView has been clicked.
                * @param parent the AdapterView where the click happened
                * @param view the view whin the AdapterView that was clicked
                * @param position the position of the view in the adapter
                * @param id the row id of the item that was clicked
                * @return No return value
                */
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    int assign;
                    int assign_question;

                    String fileName = fileList.getItemAtPosition(position).toString();
                    String delim = "[_.]";
                    String[] tokens = fileName.split(delim);
                    String text = "";
                    assign = Integer.parseInt(tokens[2]);
                    assign_question = Integer.parseInt(tokens[4]);

                    System.out.println("Problem Set -> " + assign + " Question Number -> " + assign_question);

                    try {
                        InputStream inputStream = context.openFileInput("Problem_Set_" + assign + "_Q_" + assign_question + ".txt");
                        if (inputStream != null) {
                            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                            String receiveString = "";
                            StringBuilder stringBuilder = new StringBuilder();

                            while ((receiveString = bufferedReader.readLine()) != null) {
                                stringBuilder.append(receiveString);
                            }

                            text = stringBuilder.toString();
                            inputStream.close();
                            Toast.makeText(context.getApplicationContext(), stringBuilder.toString(),
                                    Toast.LENGTH_LONG).show();
                        }
                    } catch (FileNotFoundException e) {
                        Log.e("login activity", "File not found: " + e.toString());
                    } catch (IOException e) {
                        Log.e("login activity", "Can not read file: " + e.toString());
                    }

                    Intent i = new Intent(CreateProblemSet.this, EditFileContent.class);
                    i.putExtra("assign", assign);
                    i.putExtra("assign_question", assign_question);
                    i.putExtra("text", text);
                    startActivity(i);
                }
            });
        }

        fab.setOnClickListener(new View.OnClickListener() {
            
            /**
            * Responds when a click happened.
            * @param View view
            * @return No return value
            */
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Staring Up LaTeX file", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Intent i = new Intent(CreateProblemSet.this, Editor.class);
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
        Intent i = new Intent(CreateProblemSet.this, InstructorMenu.class);
        startActivity(i);
    }
}
