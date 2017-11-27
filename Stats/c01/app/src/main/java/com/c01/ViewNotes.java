package com.c01;

import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
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

/**
* The activity for viewing notes.
*/
public class ViewNotes extends AppCompatActivity {

    private static ListAdapter myAdapter;
    private static ApacheURLLister lister;
    private static List serverDir;
    private static ListView fileList;
    private static DownloadManager downloadManager;
    private static Context context;

    /**
    * Shows an alert when ID or password incorrect.
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
    * @param savedInstanceState The data it most recently supplied on
    * @return No return value
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_notes);
        context = getApplicationContext();

        Thread t = new Thread(new Runnable() {
            
            /**
            * Gets proper file url.
            * @exception e Any exception
            * @return No return value
            */
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
                
                /**
                * Responds when an item in AdapterView has been clicked.
                * @param parent The AdapterView where the click happened
                * @param view The view whin the AdapterView that was clicked
                * @param position The position of the view in the adapter
                * @param id The row id of the item that was clicked
                * @exception e Any exception
                * @return No return value
                */
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
}
