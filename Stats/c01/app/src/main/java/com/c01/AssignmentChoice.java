package com.c01;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AssignmentChoice extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment_choice);

        Button attemptRand = (Button) findViewById(R.id.attemptRandom);
        attemptRand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nextpage = new Intent(AssignmentChoice.this, AnswerProblems.class);
                startActivity(nextpage);
            }
        });
    }


}
