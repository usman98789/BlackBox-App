package com.c01;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.ivy.util.url.ApacheURLLister;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ViewProblems extends AppCompatActivity {
    ListAdapter myAdapter;
    ApacheURLLister lister;
    List serverDir;
    ListView fileList;
    DownloadManager downloadManager;
    String num;
    List newList = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_problems);
        num = getIntent().getStringExtra("num");
        Toast.makeText(getApplicationContext(), "Problem Set: " + num, Toast.LENGTH_LONG).show();
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
                        serverDir.set(i, temp);

                    }

                    Random randomGenerator = new Random();
                    int index = 0;
                    String temp;
                    String temp2;
                    while (newList.size() <= 5){
                        index = randomGenerator.nextInt(serverDir.size());
                        temp = serverDir.get(index).toString();

                        temp2 = temp.substring(12, 13);
                        if (( temp2.equals(num)) && !(newList.contains(temp))) {
                            newList.add(temp);
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });



        t.start();
        while(t.isAlive()){}

        if (!t.isAlive()) {

            myAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, newList);
            fileList = (ListView) findViewById(R.id.fileListView);
            fileList.setAdapter(myAdapter);

            fileList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    downloadManager = (DownloadManager)getSystemService(Context.DOWNLOAD_SERVICE);
                    Uri uri = Uri.parse("https://shev:Biscut123@megumin.ga/stats/assignments/" + fileList.getItemAtPosition(position).toString());
                    DownloadManager.Request request = new DownloadManager.Request(uri);
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    Long reference = downloadManager.enqueue(request);
                }
            });
        }
    }
}
