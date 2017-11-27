package com.c01;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.ivy.util.url.ApacheURLLister;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class ViewProblems extends AppCompatActivity {

    private static ListAdapter myAdapter;
    private static ApacheURLLister lister;
    private static List serverDir;
    private static List serverDirFiltered = new ArrayList();
    private static ListView fileList;
    private static DownloadManager downloadManager;
    private static String num;
    private static List newList = new ArrayList();
    private static Context context;

    private static final String isQuestionFile = "Q";
    private static final String isNoteFile = "N";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_problems);
        context = getApplicationContext();

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
                        if (canBeDisplayed(temp) && verifyFile(temp)) {
                            serverDirFiltered.add(serverDir.get(i));
                        }
                        System.out.println(temp);
                    }

                    Random randomGenerator = new Random();
                    int index = 0;
                    String temp;
                    String temp2;
                    System.out.println(serverDirFiltered.size());
                    int limit = serverDirFiltered.size();
                    while ((newList.size() < 2) && !(serverDirFiltered.size() == 0)){
                        index = randomGenerator.nextInt(serverDirFiltered.size());
                        temp = serverDirFiltered.get(index).toString();

                        temp2 = temp.substring(12, 13);
                        if (( temp2.equals(num)) && !(newList.contains(temp))) {
                            newList.add(temp);

                        }
                        serverDirFiltered.remove(index);
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
                    request.setDestinationInExternalFilesDir(context.getApplicationContext(), Environment.DIRECTORY_DOWNLOADS, fileList.getItemAtPosition(position).toString());
                    System.out.println(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).toString());
                    Long reference = downloadManager.enqueue(request);
                    serverDirFiltered.clear();
                }
            });
        }

    }

    private boolean canBeDisplayed(String fileTile) {
        String releaseMonth;
        int releaseDay;
        int releaseYear;

        String dueMonth;
        int dueDay;
        int dueYear;

        Calendar calendar = Calendar.getInstance();
        //calendar.set(2017, 0, 1); // used to quickly manually test
        Date current = calendar.getTime();

        Date releaseFile = null;
        Date endOfTerm = null;
        SimpleDateFormat formatter = new SimpleDateFormat("MMMM/dd/yyyy");

        String delim = "[_.]";
        String[] tokens = fileTile.split(delim);

        releaseMonth = tokens[5];
        releaseDay = Integer.parseInt(tokens[6]);
        releaseYear = Integer.parseInt(tokens[7]);

        String releaseDateSet = releaseMonth + "/" + releaseDay + "/" + releaseYear;
        try {
            releaseFile = formatter.parse(releaseDateSet);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        dueMonth = tokens[11];
        dueDay = Integer.parseInt(tokens[12]);
        dueYear = Integer.parseInt(tokens[13]);

        String dueDateSet = dueMonth + "/" + dueDay + "/" + dueYear;
        try {
            endOfTerm = formatter.parse(dueDateSet);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        System.out.println(current);
        System.out.println(releaseFile);
        System.out.println(endOfTerm);
        System.out.println(current.after(releaseFile));
        System.out.println(current.before(endOfTerm));
        return current.after(releaseFile) && current.before(endOfTerm);
    }

    private boolean verifyFile(String fileName) {
        String delim = "[_.]";
        String[] tokens = fileName.split(delim);
        if (((tokens[3].compareTo(isQuestionFile) == 0) && (tokens.length == 15))
                || (tokens[3].compareTo(isNoteFile) == 0)) {
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(ViewProblems.this, WhichProblemSet.class);
        newList.clear();
        startActivity(i);
    }
}
