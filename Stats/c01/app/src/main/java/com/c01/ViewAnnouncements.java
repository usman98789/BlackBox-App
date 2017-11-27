package com.c01;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import Database.DatabaseDriver.DatabaseSelectHelper;

public class ViewAnnouncements extends AppCompatActivity {

    private ListView mainListView;
    private ArrayAdapter<String> listAdapter;
    Context context;
    int id = -1;
    ArrayList<String> accountBalance = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_announcements);

        context = this.getApplicationContext();
        mainListView = (ListView) findViewById(R.id.mainListView);
        id = Integer.valueOf(getIntent().getStringExtra("id"));

        List<String> messages = new ArrayList<>();
        // get a list of all of this user's message ids
        List<Integer> messageIds = DatabaseSelectHelper.getAllMessageIds(id, context);
        messages.add("  ");
        for (Integer iD : messageIds) {
            messages.add(DatabaseSelectHelper.getSpecificMessage(iD, context));
        }

        listAdapter = new ArrayAdapter<String>(this, R.layout.simplerow, messages);

        mainListView.setAdapter(listAdapter);


        Button Exit = (Button) findViewById(R.id.Exit1);
        Exit.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(ViewAnnouncements.this, StudentMenu.class);
                i.putExtra("id", String.valueOf(id));
                startActivity(i);
            }
        });

    }


}
