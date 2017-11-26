package com.c01;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import java.net.URL;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.ivy.util.url.ApacheURLLister;

public class CreateProblemSet extends AppCompatActivity {

    private static int assign;
    private static int assign_question;
    private static String text = "";

    private static DownloadManager downloadManager;
    private static ListAdapter myAdapter;
    private static List serverDir;
    private static ListView fileList;
    private static ApacheURLLister lister;
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
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String fileName = fileList.getItemAtPosition(position).toString();

                    // remove cache from file name
                    String delim = "[_.]";
                    String[] tokens = fileName.split(delim);
                    assign = Integer.parseInt(tokens[2]);
                    assign_question = Integer.parseInt(tokens[4]);

                    System.out.println("Problem Set -> " + assign + " Question Number -> " + assign_question);
                    System.out.println(fileName);
                    if (fileExist(fileName) == false) {
                        printToast(context, "Please wait, receiving problem set file ...");
                        downloadFile(position);
                        waitForFileDownload(fileName);
                    }
                    readFile();
                    editFile (fileName);
                }
            });
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Staring Up LaTeX file", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Intent i = new Intent(CreateProblemSet.this, DueDateSetup.class);
                startActivity(i);
            }
        });
    }

    private boolean fileExist (String fileName) {
        String dir = "/sdcard/Android/data/com.c01/files/Download/" + fileName;
        File file = new File(dir);
        return file.exists();
    }

    private void readFile () {
        try {
            File fileQuestion = new File("/sdcard/Android/data/com.c01/files/Download/"
                    + "Problem_Set_" + assign + "_Q_" + assign_question + ".txt");
            if (fileQuestion != null) {
                BufferedReader bufferedReader = new BufferedReader(new FileReader(fileQuestion));
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();
                while ((receiveString = bufferedReader.readLine()) != null) {
                    stringBuilder.append(receiveString);
                }
                bufferedReader.close();
                text = stringBuilder.toString();
                Toast.makeText(context.getApplicationContext(), text, Toast.LENGTH_LONG).show();
            }
        } catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }
    }

    private void editFile (String fileName) {
        Intent i = new Intent(CreateProblemSet.this, EditFileContent.class);
        i.putExtra("assign", assign);
        i.putExtra("assign_question", assign_question);
        i.putExtra("text", text);
        String dir = "/sdcard/Android/data/com.c01/files/Download/" + fileName;
        i.putExtra("oldFile", dir);
        startActivity(i);
    }

    private void downloadFile (int position) {
        downloadManager = (DownloadManager)getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse("https://shev:Biscut123@megumin.ga/stats/assignments/" + fileList.getItemAtPosition(position).toString());
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(context.getApplicationContext(), Environment.DIRECTORY_DOWNLOADS ,fileList.getItemAtPosition(position).toString());
        Long reference = downloadManager.enqueue(request);
        registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    private void waitForFileDownload(String fileName) {
        while (fileExist(fileName)) {}
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static BroadcastReceiver onComplete = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {}
        }
    };

    private void printToast(Context context, String string) {
        Toast.makeText(context.getApplicationContext(), string, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(CreateProblemSet.this, InstructorMenu.class);
        startActivity(i);
    }
}

