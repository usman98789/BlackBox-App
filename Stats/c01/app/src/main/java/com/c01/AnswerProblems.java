package com.c01;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.nishant.math.MathView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import FileOperations.TextFileReader;

public class AnswerProblems extends AppCompatActivity {

    private static EditText questionText;
    private static Button next;
    private static RadioGroup choices;
    private static int counter, feedbackCounter;
    private static String correctAnswer, feedback;
    private static String[] feedbackArr = new String[5];
    private static String[] tempFilesPath = new String[5];
    private static int num;
    private static MathView mathView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_problems);
        Intent intent = getIntent();

        //questionText = (EditText) findViewById(R.id.questionText);
        choices = (RadioGroup) findViewById(R.id.choices);
        next = (Button) findViewById(R.id.nextButton);
        mathView = (MathView) findViewById(R.id.math_view_assignment);

        String path = "/sdcard/Android/data/com.c01/files/Download/";
        File f = new File(path);
        File file[] = f.listFiles();
        file = filterList(file);

        for (int i = 0; i < file.length; i++) {
            Log.d("supertest", file[i].getAbsolutePath());
        }


        int size = 0;
        if (file == null) {
            showAlert();

        } else {
            Log.d("supertest", file.length + "");
            if (file.length > 5) {
                List<File> temp = Arrays.asList(file);
                Collections.shuffle(temp);
                temp.toArray(file);
                for (int i = 0; i < 5; i++) {
                    tempFilesPath[i] = file[i].getAbsolutePath();
                }

            } else {
                size = file.length;
                tempFilesPath = new String[size];
                feedbackArr = new String[size];
                for (int i = 0; i < tempFilesPath.length; i++) {
                    tempFilesPath[i] = file[i].getAbsolutePath();
                }
            }

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

                //questionText.setText(contents[0]);
                mathView.setText(contents[0].toString());
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

                    if (counter == tempFilesPath.length) {
                        //assignment complete get results
                        if (answer.equals(correctAnswer)) {
                            feedbackArr[feedbackCounter] = "Question " + (feedbackCounter + 1) + " is correct";
                        } else {
                            feedbackArr[feedbackCounter] = "Question " + (feedbackCounter + 1) + " is wrong: " + feedback;
                        }
                        counter = 0;
                        feedbackCounter = 0;
                        int id = getIntent().getIntExtra("userId", 0);
                        Intent i = new Intent(AnswerProblems.this, Results.class);
                        i.putExtra("feedback", feedbackArr);
                        i.putExtra("userId", id);
                        i.putExtra("num", num);
                        startActivity(i);
                    } else {
                        if (answer.equals(correctAnswer)) {
                            feedbackArr[feedbackCounter] = "Question " + (feedbackCounter + 1) + " is correct";
                        } else {
                            feedbackArr[feedbackCounter] = "Question " + (feedbackCounter + 1) + " is wrong: " + feedback;
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

                            //questionText.setText(contents[0]);
                            mathView.setText(contents[0].toString());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
//        String[] tempFilesPath = {path + "testQuestion.txt", path + "testQuestion2.txt", path + "testQuestion3.txt", path + "testQuestion4.txt", path + "testQuestion5.txt"};



    }

    public void showAlert() {
        final AlertDialog.Builder myAlert = new AlertDialog.Builder(this);
        myAlert.setMessage("You don't have any problem sets saved in your downloads folder.")
                .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        Intent mainMenuIntent = new Intent(AnswerProblems.this, StudentMenu.class);
                        startActivity(mainMenuIntent);
                    }
                });
        myAlert.show();
    }

    private static void shuffleArray(int[] array)
    {
        int index;
        Random random = new Random();
        for (int i = array.length - 1; i > 0; i--)
        {
            index = random.nextInt(i + 1);
            if (index != i)
            {
                array[index] ^= array[i];
                array[i] ^= array[index];
                array[index] ^= array[i];
            }
        }
    }

    public File[] filterList(File[] files) {
        List<File> templist = Arrays.asList(files);
        List<File> newList = new ArrayList<File>();
        Intent intent = getIntent();
        num = intent.getIntExtra("num", 0);
        int newSize = 0;
        String temp2;
        String temp;
        int index = 0;
        while (index != templist.size()){
            temp = templist.get(index).getName();
            temp2 = temp.substring(12, 13);
            if (( temp2.equals(String.valueOf(num))) && !(newList.contains(temp))) {
                newList.add(templist.get(index));
                newSize++;
            }
            index++;
        }
        File[] filtered = new File[newSize];
        newList.toArray(filtered);
        return filtered;
    }

    public String[] setInformation(String[] infoArr) {
        String[] temp = {infoArr[1], infoArr[2], infoArr[3]};
        Collections.shuffle(Arrays.asList(temp));
        return temp;
    }



}
