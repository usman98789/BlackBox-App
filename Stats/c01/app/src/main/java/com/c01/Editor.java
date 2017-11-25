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


public class Editor extends AppCompatActivity {

    private static MathView mathView;
    private static Button next;
    private static TextView text;
    private static Button problemSet;
    private static int assign = 1;
    private static int assign_question = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        Context context = this.getApplicationContext();
        assign = Integer.valueOf(getIntent().getStringExtra("assign"));
        assign_question = Integer.valueOf(getIntent().getStringExtra("assign_question"));

        mathView = (MathView) findViewById(R.id.math_view);
        text = (TextView) findViewById(R.id.input_view);
        mathView.setText("");
        next = (Button) findViewById(R.id.generate_problem);
        problemSet = (Button) findViewById(R.id.generate_problem_set);
        String path = "/data/data/com.c01/files/";

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
                    i.putExtra("assign", String.valueOf(assign));
                    i.putExtra("assign_question", String.valueOf(assign_question));
                    startActivity(i);
                }
            }
        });

        problemSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context.getApplicationContext(), "Problem Set " + assign +" has been finalized.",
                        Toast.LENGTH_LONG).show();
            }
        });

    }


    @Override
    public void onBackPressed() {
        Intent i = new Intent(Editor.this, CountQuestions.class);
        startActivity(i);
    }
}
