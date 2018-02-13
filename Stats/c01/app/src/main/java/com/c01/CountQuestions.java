package com.c01;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
* The activity for counting questions.
*/
public class CountQuestions extends AppCompatActivity {
    Context context;
    String name;
    private static int problem_set_number = 0;
    private static int question_number = 0;

    /**
    * Starts the activity.
    * @param savedInstanceState The data it most recently supplied on
    * @return No return value
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_count_questions);

        Button button = (Button) findViewById(R.id.button);
        context = this.getApplicationContext();
        button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                EditText etNumber1 = (EditText) findViewById(R.id.editText);
                problem_set_number = Integer.valueOf(etNumber1.getText().toString());
                EditText etNumber2 = (EditText) findViewById(R.id.editText2);
                question_number = Integer.valueOf(etNumber2.getText().toString());
                if ((problem_set_number != 0) && (question_number != 0)) {
                    Intent i = new Intent(CountQuestions.this, DueDateSetup.class);
                    i.putExtra("assign", String.valueOf(problem_set_number));
                    i.putExtra("assign_question", String.valueOf(question_number));
                    startActivity(i);
                } else {
                    Toast.makeText(getApplicationContext(), "Fill both fields", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    /**
    * Responds when user presses the back key.
    * @return No return value
    */
    @Override
    public void onBackPressed() {
        Intent i = new Intent(CountQuestions.this, CreateProblemSet.class);
        startActivity(i);
    }
}
