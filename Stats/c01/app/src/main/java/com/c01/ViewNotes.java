package com.c01;

import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.net.URL;
import java.util.List;
import org.apache.ivy.util.url.ApacheURLLister;

public class ViewNotes extends AppCompatActivity {


    private static ListAdapter myAdapter;
    private static ApacheURLLister lister;
    private static List serverDir;
    private static ListView fileList;
    private static DownloadManager downloadManager;
    private static Context context;
    private static int num;

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
        setContentView(R.layout.activity_view_notes);
        context = getApplicationContext();

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {


                try {
                    URL url = new URL("https://shev:Biscut123@megumin.ga/stats/notes");
                    lister = new ApacheURLLister();
                    serverDir = lister.listFiles(url);

                    for (int i = 0; i < serverDir.size(); i++) {
                        String temp = serverDir.get(i).toString();
                        temp = temp.replace("https://megumin.ga/stats/notes/", "");
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
            fileList.setAdapter(myAdapter);

            fileList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    downloadManager = (DownloadManager)getSystemService(Context.DOWNLOAD_SERVICE);
                    Uri uri = Uri.parse("https://shev:Biscut123@megumin.ga/stats/notes/" + fileList.getItemAtPosition(position).toString());
                    DownloadManager.Request request = new DownloadManager.Request(uri);
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    request.setDestinationInExternalFilesDir(context.getApplicationContext(), Environment.DIRECTORY_DOWNLOADS,fileList.getItemAtPosition(position).toString());
                    Long reference = downloadManager.enqueue(request);
                }
            });
        }

    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(ViewNotes.this, StudentMenu.class);
        num = getIntent().getIntExtra("userId", 0);
        i.putExtra("id", "" + num + "");
        startActivity(i);
    }
}
