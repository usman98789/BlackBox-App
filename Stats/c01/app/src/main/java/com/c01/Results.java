package com.c01;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import Database.DatabaseDriver.DatabaseInsertHelper;
import Database.DatabaseDriver.DatabaseSelectHelper;
import Database.DatabaseDriver.DatabaseUpdateHelper;
import Exceptions.InvalidAssignmentException;
import Exceptions.InvalidIdException;
import Exceptions.InvalidMarkException;

/**
* The activity for results.
*/
public class Results extends AppCompatActivity {

    private static EditText title;
    private static TextView firstQuestion;
    private static TextView secondQuestion;
    private static TextView thirdQuestion;
    private static TextView fourthQuestion;
    private static TextView fifthQuestion;
    private static String[] feedback;
    private static Button back;
    private static int total;
    private static int num;

    /**
    * Starts the activity.
    * @param savedInstanceState The data it most recently supplied on
    * @exception e InvalidMarkException, InvalidIDException, InvalidAssignmentException
    * @return No return value
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        Intent intent = getIntent();

        title = (EditText) findViewById(R.id.resultsText);
        firstQuestion = (TextView) findViewById(R.id.first);
        secondQuestion = (TextView) findViewById(R.id.second);
        thirdQuestion = (TextView) findViewById(R.id.third);
        fourthQuestion = (TextView) findViewById(R.id.fourth);
        fifthQuestion = (TextView) findViewById(R.id.fifth);
        TextView[] questions = {firstQuestion, secondQuestion, thirdQuestion, fourthQuestion, fifthQuestion};
        back = (Button) findViewById(R.id.backToMain);
        num = intent.getIntExtra("num", 0);

        feedback = intent.getStringArrayExtra("feedback");
        double Fracmark = 0;
        double mark = 0.0;

        for (int i = 0; i < 5; i++) {
            if (i < feedback.length) {
                questions[i].setText(feedback[i]);
            } else {
                questions[i].setText("");
            }
        }

        Fracmark = 0;
        for (int i = 0; i < feedback.length; i++){
            if (feedback[i].contains("correct")){
                Fracmark += 1;
            }
            total += 1;
        }
        System.out.println("frac" + Fracmark);
        System.out.println("total" + total);
        mark = (Fracmark/total) * 100;
        int id = getIntent().getIntExtra("userId", 0);
        try {
            if (DatabaseSelectHelper.getAssignmentMark(id, num, getApplicationContext()) == -1){
                DatabaseInsertHelper.insertAssignmentMark(id, mark, num, getApplicationContext());
            } else {
                DatabaseUpdateHelper.updateAssignmentMark(id, mark, num, getApplicationContext());
            }
            Toast.makeText(getApplicationContext(), String.valueOf(mark), Toast.LENGTH_LONG).show();
        } catch (InvalidMarkException e) {
            Toast.makeText(getApplicationContext(), "Student added1", Toast.LENGTH_LONG).show();
        } catch (InvalidIdException e) {
            Toast.makeText(getApplicationContext(), "Student added2", Toast.LENGTH_LONG).show();
        } catch (InvalidAssignmentException e) {
            Toast.makeText(getApplicationContext(), "Student added3", Toast.LENGTH_LONG).show();
        }
        System.out.println("id " + id);
        
        back.setOnClickListener(new View.OnClickListener() {
            /**
            * Responds when a click happened.
            * @param view The content to display
            * @return No return value
            */
            @Override
            public void onClick(View v) {
                Intent newi = new Intent(Results.this, StudentMenu.class);
                newi.putExtra("id",String.valueOf(id));
                System.out.println("INSIDE RESULTS ID IS " + id);
                startActivity(newi);
            }
        });
    }
}
