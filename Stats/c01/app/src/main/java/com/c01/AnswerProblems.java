package com.c01;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

import FileOperations.TextFileReader;

public class AnswerProblems extends AppCompatActivity {

    private static EditText questionText;
    private static Button next;
    private static RadioGroup choices;
    private static int counter, feedbackCounter;
    private static String correctAnswer, feedback;
    private static String[] feedbackArr = new String[5];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_problems);


        questionText = (EditText) findViewById(R.id.questionText);
        choices = (RadioGroup) findViewById(R.id.choices);
        next = (Button) findViewById(R.id.nextButton);
        String path = "/storage/emulated/0/Android/data/com.c01/files/Download/";
        String[] tempFilesPath = {path + "testQuestion.txt", path + "testQuestion2.txt", path + "testQuestion3.txt", path + "testQuestion4.txt", path + "testQuestion5.txt"};
        counter = 0;
        feedbackCounter = 0;
        TextFileReader t = new TextFileReader();


        try {
            String[] contents = t.readFile(tempFilesPath[counter]);
            counter ++;
            String [] temp;
            correctAnswer = contents[4];
            feedback = contents[5];
            temp = setInformation(contents);
            for (int i = 0; i < choices.getChildCount(); i++) {
                ((RadioButton) choices.getChildAt(i)).setText(temp[i]);
            }

            questionText.setText(contents[0]);
        } catch (IOException e) {
            e.printStackTrace();
        }

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int radioID = choices.getCheckedRadioButtonId();
                View radioButton = choices.findViewById(radioID);
                int idx = choices.indexOfChild(radioButton);
                RadioButton r = (RadioButton) choices.getChildAt(idx);
                String answer = r.getText().toString();

                if (counter == 5) {
                    //assignment complete get results
                    if (answer.equals(correctAnswer)) {
                        feedbackArr[feedbackCounter] = "Question " + (feedbackCounter + 1) + " is correct";
                    } else {
                        feedbackArr[feedbackCounter] = "Question " + (feedbackCounter + 1) + " is incorrect: " + feedback;
                    }
                    counter = 0;
                    feedbackCounter = 0;
                    Log.d("supertest", feedbackArr[0]);
                    Log.d("supertest", feedbackArr[1]);
                    Log.d("supertest", feedbackArr[2]);
                    Log.d("supertest", feedbackArr[3]);
                    Log.d("supertest", feedbackArr[4]);

                    Intent i = new Intent(AnswerProblems.this, Results.class);
                    i.putExtra("feedback", feedbackArr);
                    startActivity(i);
                } else {
                    if (answer.equals(correctAnswer)) {
                        feedbackArr[feedbackCounter] = "Question " + (feedbackCounter + 1) + " is correct";
                    } else {
                        feedbackArr[feedbackCounter] = "Incorrect: " + feedback;
                    }
                    feedbackCounter++;

                    try {
                        String[] contents = t.readFile(tempFilesPath[counter]);
                        counter ++;
                        String [] temp;
                        correctAnswer = contents[4];
                        feedback = contents[5];
                        temp = setInformation(contents);
                        for (int i = 0; i < choices.getChildCount(); i++) {
                            ((RadioButton) choices.getChildAt(i)).setText(temp[i]);
                        }

                        questionText.setText(contents[0]);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }

    public String[] setInformation(String[] infoArr) {
        String[] temp = {infoArr[1], infoArr[2], infoArr[3]};
        Collections.shuffle(Arrays.asList(temp));
        return temp;
    }
}
