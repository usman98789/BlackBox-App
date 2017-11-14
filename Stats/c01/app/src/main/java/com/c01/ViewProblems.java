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

import org.apache.ivy.util.url.ApacheURLLister;

import java.net.URL;
import java.util.List;

public class ViewProblems extends AppCompatActivity {
    ListAdapter myAdapter;
    ApacheURLLister lister;
    List serverDir;
    ListView fileList;
    DownloadManager downloadManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_problems);

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
                    Uri uri = Uri.parse("https://shev:Biscut123@megumin.ga/stats/assignments/" + fileList.getItemAtPosition(position).toString());
                    DownloadManager.Request request = new DownloadManager.Request(uri);
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    Long reference = downloadManager.enqueue(request);
                }
            });
        }
    }
}
