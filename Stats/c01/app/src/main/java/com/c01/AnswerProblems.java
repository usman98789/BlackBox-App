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
import android.widget.Toast;

import com.nishant.math.MathView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import FileOperations.TextFileReader;

/**
* The activity for answering problems.
*/
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

    /**
    * Starts the activity.
    * @param savedInstanceState The data it most recently supplied on
    * @exception e IOException
    * @return No return value
    */
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


        int size = 0;
        if (file == null) {
            showAlert();

        } else if (file.length == 0){
            showAlert();
        } else {
            file = filterList(file);
            if (file == null) {
                showAlert();
            } else if (file.length == 0) {
                showAlert();
            } else {
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

                    mathView.setText(contents[0].toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                next.setOnClickListener(new View.OnClickListener() {

                    /**
                     * Responds when a click happened.
                     * @param v The content to display
                     * @exception e IOException
                     * @return No return value
                     */
                    @Override
                    public void onClick(View v) {
                        int radioID = choices.getCheckedRadioButtonId();
                        View radioButton = choices.findViewById(radioID);
                        int idx = choices.indexOfChild(radioButton);
                        RadioButton r = (RadioButton) choices.getChildAt(idx);


                        if (r == null) {
                            Toast.makeText(getApplicationContext(), "Choose an option", Toast.LENGTH_LONG).show();
                        } else {
                            if (counter == tempFilesPath.length) {
                                String answer = r.getText().toString();
                                // Assignment complete get results
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
                                String answer = r.getText().toString();
                                if (answer.equals(correctAnswer)) {
                                    feedbackArr[feedbackCounter] = "Question " + (feedbackCounter + 1) + " is correct";
                                } else {
                                    feedbackArr[feedbackCounter] = "Question " + (feedbackCounter + 1) + " is wrong: " + feedback;
                                }
                                feedbackCounter++;

                                try {
                                    String[] contents = t.readFile(tempFilesPath[counter]);
                                    counter++;
                                    String[] temp;
                                    correctAnswer = contents[4];
                                    feedback = contents[5];
                                    temp = setInformation(contents);
                                    for (int i = 0; i < choices.getChildCount(); i++) {
                                        ((RadioButton) choices.getChildAt(i)).setText(temp[i]);
                                    }
                                    mathView.setText(contents[0].toString());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                });
            }
        }

    }

    /**
    * Shows an alert when no saved files in download folder.
    * @return No return value
    */
    public void showAlert() {
        final AlertDialog.Builder myAlert = new AlertDialog.Builder(this);
        myAlert.setMessage("You don't have any problem sets saved in your downloads folder.")
                .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        Intent mainMenuIntent = new Intent(AnswerProblems.this, StudentMenu.class);
                        int id = getIntent().getIntExtra("userId", 0);
                        mainMenuIntent.putExtra("id", ""+id+"");

                        startActivity(mainMenuIntent);
                    }
                });
        myAlert.show();
    }

    /**
    * Shuffles an array.
    * @param array Integer array
    * @return No return value
    */
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

    /**
    * Filters a list.
    * @param files File of files
    * @return File Filtered files
    */
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

    /**
    * Sets information.
    * @param infoArr Array of string information
    * @return String[] Array of string information, shuffled
    */
    public String[] setInformation(String[] infoArr) {
        String[] temp = {infoArr[1], infoArr[2], infoArr[3]};
        Collections.shuffle(Arrays.asList(temp));
        return temp;
    }

}
