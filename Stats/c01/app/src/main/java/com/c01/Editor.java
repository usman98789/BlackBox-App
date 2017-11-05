package com.c01;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nishant.math.MathView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import maximsblog.blogspot.com.jlatexmath.*;
import maximsblog.blogspot.com.jlatexmath.core.AjLatexMath;
import maximsblog.blogspot.com.jlatexmath.core.Insets;
import maximsblog.blogspot.com.jlatexmath.core.TeXConstants;
import maximsblog.blogspot.com.jlatexmath.core.TeXFormula;
import maximsblog.blogspot.com.jlatexmath.core.TeXIcon;

public class Editor extends AppCompatActivity {

    private static MathView mathView;
    private static Button question;
    private static Button problemSet;
    private static TextView text;
    private static int assign = 0;
    private static int assign_question = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        Context context = this.getApplicationContext();
        AjLatexMath.init(context);

        //generate = (Button) findViewById(R.id.generate_file_botton);
        mathView = (MathView) findViewById(R.id.math_view);
        text = (TextView) findViewById(R.id.input_view);
        mathView.setText("");
        question = (Button) findViewById(R.id.generate_question);
        problemSet = (Button) findViewById(R.id.generate_problem_set);

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

        question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context.getApplicationContext(), "Creating problem",
                        Toast.LENGTH_LONG).show();
                Toast.makeText(context.getApplicationContext(), "Problem_Set_" + assign + ".txt",
                        Toast.LENGTH_LONG).show();
                try {
                    OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("Problem_Set_" + assign + ".txt", Context.MODE_PRIVATE));
                    outputStreamWriter.write(text.getText().toString());
                    outputStreamWriter.close();
                }
                catch (IOException e) {
                    Log.e("Exception", "File write failed: " + e.toString());
                }

                String ret = "";
                try {
                    InputStream inputStream = context.openFileInput("Problem_Set_" + assign + ".txt");

                    if ( inputStream != null ) {
                        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                        String receiveString = "";
                        StringBuilder stringBuilder = new StringBuilder();

                        while ( (receiveString = bufferedReader.readLine()) != null ) {
                            stringBuilder.append(receiveString);
                        }

                        inputStream.close();
                        ret = stringBuilder.toString();
                        Toast.makeText(context.getApplicationContext(), ret,
                                Toast.LENGTH_LONG).show();
                    }
                }
                catch (FileNotFoundException e) {
                    Log.e("login activity", "File not found: " + e.toString());
                } catch (IOException e) {
                    Log.e("login activity", "Can not read file: " + e.toString());
                }
            }
        });


        problemSet.setOnClickListener(new View.OnClickListener()

            {
                @Override
                public void onClick (View view){

            }
            });
        }

        private List<File> getListFiles (File parentDir){
            ArrayList<File> inFiles = new ArrayList<File>();
            File[] files = parentDir.listFiles();
            for (File file : files) {
                if (file.isDirectory()) {
                    inFiles.addAll(getListFiles(file));
                } else {
                    if (file.getName().endsWith(".csv")) {
                        inFiles.add(file);
                    }
                }
            }
            return inFiles;
        }
    }
