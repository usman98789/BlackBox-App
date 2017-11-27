package com.c01;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Results extends AppCompatActivity {

    private static EditText title;
    private static TextView firstQuestion;
    private static TextView secondQuestion;
    private static TextView thirdQuestion;
    private static TextView fourthQuestion;
    private static TextView fifthQuestion;
    private static String[] feedback;
    private static Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        title = (EditText) findViewById(R.id.resultsText);
        firstQuestion = (TextView) findViewById(R.id.first);
        secondQuestion = (TextView) findViewById(R.id.second);
        thirdQuestion = (TextView) findViewById(R.id.third);
        fourthQuestion = (TextView) findViewById(R.id.fourth);
        fifthQuestion = (TextView) findViewById(R.id.fifth);
        TextView[] questions = {firstQuestion, secondQuestion, thirdQuestion, fourthQuestion, fifthQuestion};
        back = (Button) findViewById(R.id.backToMain);

        Intent intent = getIntent();
        feedback = intent.getStringArrayExtra("feedback");


        for (int i = 0; i < 5; i++) {
            if (i < feedback.length) {
                questions[i].setText(feedback[i]);
            } else {
                questions[i].setText("");
            }
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Results.this, StudentMenu.class);
                startActivity(i);
            }
        });
    }
}
