package com.c01;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.nishant.math.MathView;

import java.io.IOException;
import java.io.OutputStreamWriter;


/**
* The activity of editor.
*/
public class Editor extends AppCompatActivity {

    private static MathView mathView;
    private static Button next;
    private static TextView text;
    private static Button problemSet;

    private static String releaseDate = "";
    private static String dueDate = "";
    private static String endSemesterDate = "";
    private static int assign = 1;
    private static int assign_question = 1;

    private static final String serializeQuestionSet = "serial.txt";

    /**
    * Starts the activity.
    * @param savedInstanceState The data it most recently supplied on
    * @return No return value
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        Context context = this.getApplicationContext();

        mathView = (MathView) findViewById(R.id.math_view);
        text = (TextView) findViewById(R.id.input_view);
        mathView.setText("");
        next = (Button) findViewById(R.id.generate_problem);
        problemSet = (Button) findViewById(R.id.generate_problem_set);

        releaseDate = getIntent().getStringExtra("releaseDate");
        dueDate = getIntent().getStringExtra("dueDate");
        endSemesterDate = getIntent().getStringExtra("endSemesterDate");
        assign = Integer.valueOf(getIntent().getStringExtra("assign"));
        assign_question = Integer.valueOf(getIntent().getStringExtra("assign_question"));

        text.addTextChangedListener(new TextWatcher() {

            /**
            * Notifies when text has been changed within s.
            * @param s An editable
            * @return No return value
            */
            @Override
            public void afterTextChanged(Editable s) {
                mathView.setText(text.getText().toString());
                System.out.println("X : " + mathView.getX() + " Y: " + mathView.getY() + " Z: " + mathView.getZ());
            }
        });

        next.setOnClickListener(new View.OnClickListener() {

            /**
            * Responds when a click happened.
            * @param view The content to display
            * @exception e IOException, FileNotFoundException
            * @return No return value
            */
            public void onClick(View view) {
                String Text = (String) text.getText().toString();
                if (TextUtils.isEmpty(Text)) {
                    text.setError("No question entered");
                } else {
                    Intent i = new Intent(Editor.this, Choices.class);
                    i.putExtra("question", Text);
                    i.putExtra("releaseDate", releaseDate);
                    i.putExtra("dueDate", dueDate);
                    i.putExtra("endSemesterDate", endSemesterDate);
                    i.putExtra("assign", String.valueOf(assign));
                    i.putExtra("assign_question", String.valueOf(assign_question));
                    startActivity(i);
                }
            }
        });

        problemSet.setOnClickListener(new View.OnClickListener() {

            /**
            * Responds when a click happened.
            * @param view The content to display
            * @return No return value
            */
            @Override
            public void onClick(View view) {
                assign_question = 1;
                Toast.makeText(context.getApplicationContext(), "Problem Set " + assign +" has been finalized.",
                        Toast.LENGTH_LONG).show();
                assign++;
                serializeProblemSet(context);
            }
        });

    }

    private void serializeProblemSet(Context context) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(serializeQuestionSet, Context.MODE_PRIVATE));
            outputStreamWriter.write(assign + "_" + assign_question);
            outputStreamWriter.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    /**
    * Responds when user presses the back key.
    * @return No return value
    */
    @Override
    public void onBackPressed() {
        Intent i = new Intent(Editor.this, CreateProblemSet.class);
        startActivity(i);
    }
}
