package com.c01;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
    private static RadioButton choiceOne;
    private static RadioButton choiceTwo;
    private static RadioButton choiceThree;
    private static RadioButton choiceFour;
    private static Button next;
    private static RadioGroup choices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        questionText = (EditText) findViewById(R.id.questionText);
        choices = (RadioGroup) findViewById(R.id.choices);
        choiceOne = (RadioButton) findViewById(R.id.choiceOne);
        choiceTwo = (RadioButton) findViewById(R.id.choiceTwo);
        choiceThree = (RadioButton) findViewById(R.id.choiceThree);
        choiceFour = (RadioButton) findViewById(R.id.choiceFour);
        next = (Button) findViewById(R.id.nextButton);
        String tempFilePath = "/storage/emulated/0/Android/data/com.c01/files/Download/testQuestion.txt";
        TextFileReader t = new TextFileReader();
        String correctAnswer, feedback;
        try {
            String[] contents = t.readFile(tempFilePath);
            String [] temp;
            correctAnswer = contents[4];
            feedback = contents[5];
            temp = setInformation(contents);
            for (int i = 0; i < choices.getChildCount(); i++) {
                ((RadioButton) choices.getChildAt(i)).setText(temp[i]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_problems);

        choiceOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deselectOthers(1);
            }
        });

        choiceTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deselectOthers(2);
            }
        });

        choiceThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deselectOthers(3);
            }
        });

        choiceFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deselectOthers(4);
            }
        });
    }

    public void deselectOthers(int selected){
        if (selected != 1 && choiceOne.isChecked()) {
            choiceOne.setChecked(false);
        }
        if (selected != 2 && choiceTwo.isChecked()) {
            choiceTwo.setChecked(false);
        }
        if (selected != 3 && choiceThree.isChecked()) {
            choiceThree.setChecked(false);
        }
        if (selected != 4 && choiceFour.isChecked()) {
            choiceFour.setChecked(false);
        }
    }

    public String[] setInformation(String[] infoArr) {
        String[] temp = {infoArr[1], infoArr[2], infoArr[3], infoArr[4]};
        Collections.shuffle(Arrays.asList(temp));
        return temp;
    }
}
