package com.c01;

import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.ivy.util.url.ApacheURLLister;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class StudentFileInbox extends AppCompatActivity {

    private static ListAdapter myAdapter;
    private static List<String> serverDir;
    private static ListView fileList;
    private static File files;
    private static File[] localDir;
    private static Context context;

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
        setContentView(R.layout.activity_student_file_inbox);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context = getApplicationContext();

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {


                try {
                    String dir = "/sdcard/Android/data/com.c01/files/Download";
                    files = new File(dir);
                    localDir = files.listFiles();
                    serverDir = new ArrayList<String>();

                    for (File file : localDir){
                        serverDir.add(file.getName());
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
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String fileName = fileList.getItemAtPosition(position).toString();
                    String delimNotReadable = "[.]";
                    String[] tokenNotReadable = fileName.split(delimNotReadable);

                    if (tokenNotReadable[tokenNotReadable.length - 1].compareTo("txt") == 0) {
                        int assign;
                        int assign_question;

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

                        Intent i = new Intent(StudentFileInbox.this, FileView.class);
                        i.putExtra("text", text);
                        startActivity(i);
                    } else {
                        Toast.makeText(context.getApplicationContext(), "File cannot be read",
                                Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Getting More Problem Sets", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Intent i = new Intent(StudentFileInbox.this, WhichProblemSet.class);
                startActivity(i);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(StudentFileInbox.this, MainMenu.class);
        startActivity(i);
    }

}
