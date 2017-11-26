package com.c01;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nishant.math.MathView;

import java.io.IOException;
import java.io.OutputStreamWriter;


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

        System.out.println("101010101010101010101 " + releaseDate);
        System.out.println("101010101010101010101 " + dueDate);
        System.out.println("101010101010101010101 " + endSemesterDate);

        text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mathView.setText(text.getText().toString());
                System.out.println("X : " + mathView.getX() + " Y: " + mathView.getY() + " Z: " + mathView.getZ());
            }
        });

        next.setOnClickListener(new View.OnClickListener() {

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
                    startActivity(i);
                }
            }
        });

        problemSet.setOnClickListener(new View.OnClickListener() {
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

    @Override
    public void onBackPressed() {
        Intent i = new Intent(Editor.this, CreateProblemSet.class);
        startActivity(i);
    }
}
