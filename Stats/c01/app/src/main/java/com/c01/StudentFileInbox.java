package com.c01;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StudentFileInbox extends AppCompatActivity {

    private static ListAdapter myAdapter;
    private static List<String> serverDir;
    private static ListView fileList;
    private static File files;
    private static File[] localDir;
    private static Context context;

    private static final String isQuestionFile = "Q";
    private static final String isNoteFile = "N";

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
                    getFiles();
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

                        // remove cache extension from file name and parse file name
                        String delim = "[_.]";
                        String[] tokens = fileName.split(delim);
                        String text = "";

                        if (tokens[3].compareTo(isQuestionFile) == 0) {
                            System.out.println("File text : " + fileName);
                            int assign = Integer.parseInt(tokens[2]);
                            int assign_question = Integer.parseInt(tokens[4]);
                            System.out.println("Problem Set -> " + assign + " Question Number -> " + assign_question);
                            text = getSelectedFile(fileName);
                        } else if (tokens[3].compareTo(isNoteFile) == 0) {
                            System.out.println("File text : " + fileName);
                            text = getSelectedFile(fileName);
                        }
                        ViewFile(text);
                    } else {
                        displayToastErrorMessage();
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

    private String getSelectedFile(String fileName) {
        try {
            File fileQuestion = new File("/sdcard/Android/data/com.c01/files/Download/" + fileName);
            if (fileQuestion != null) {
                BufferedReader bufferedReader = new BufferedReader(new FileReader(fileQuestion));
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();
                while ((receiveString = bufferedReader.readLine()) != null) {
                    stringBuilder.append(receiveString);
                }
                displayToastMessage(stringBuilder.toString());
                return stringBuilder.toString();
            }
        } catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }
        return "File was corrupted. " + "Please download this file again.";
    }

    private void getFiles() {
        try {
            String dir = "/sdcard/Android/data/com.c01/files/Download";
            files = new File(dir);
            localDir = files.listFiles();
            serverDir = new ArrayList<String>();

            for (File file : localDir) {
                serverDir.add(file.getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void ViewFile(String text) {
        Intent i = new Intent(StudentFileInbox.this, FileView.class);
        i.putExtra("text", text);
        startActivity(i);
    }

    private void displayToastErrorMessage() {
        Toast.makeText(context.getApplicationContext(), "File cannot be read", Toast.LENGTH_LONG).show();
    }

    private void displayToastMessage(String string) {
        Toast.makeText(context.getApplicationContext(), string, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(StudentFileInbox.this, StudentMenu.class);
        startActivity(i);
    }

}
