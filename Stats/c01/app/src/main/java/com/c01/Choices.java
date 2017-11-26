package com.c01;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.nishant.math.MathView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Choices extends AppCompatActivity {

    private static Button question;
    private static int assign = 1;
    private static int assign_question = 1;

    private static String name;
    private static String releaseDate = "";
    private static String dueDate = "";
    private static String endSemesterDate = "";
    private static final String serializeQuestionSet = "serial.txt";

    private static Boolean canDo = false;
    private static File f;
    private static InputStream is;

    private static String text;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choices);
        Context context = this.getApplicationContext();
        text = getIntent().getStringExtra("question");

        question = (Button) findViewById(R.id.generate_problem);
        String path = "/data/data/com.c01/files/";

        releaseDate = getIntent().getStringExtra("releaseDate");
        System.out.println("No longer a date " + releaseDate);
        releaseDate = releaseDate.replaceAll("[ ]", "_");
        releaseDate = releaseDate.replaceAll("[,]", "");

        dueDate = getIntent().getStringExtra("dueDate");
        dueDate = dueDate.replaceAll("[ ]", "_");
        dueDate = dueDate.replaceAll("[,]", "");
//Prove the claim :$$V-E+F=2$$
        endSemesterDate = getIntent().getStringExtra("endSemesterDate");
        endSemesterDate = endSemesterDate.replaceAll("[ ]", "_");
        endSemesterDate = endSemesterDate.replaceAll("[,]", "");
        initSerializeProblemSet(context);

        question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!check()) {

                } else {

                    EditText etOne = (EditText) findViewById(R.id.choice1);
                    String choice1 = etOne.getText().toString();
                    EditText etTwo = (EditText) findViewById(R.id.choice2);
                    String choice2 = etTwo.getText().toString();
                    EditText etThree = (EditText) findViewById(R.id.choice3);
                    String choice3 = etThree.getText().toString();
                    EditText etAnswer = (EditText) findViewById(R.id.answer);
                    String answer = etAnswer.getText().toString();
                    EditText etFeed = (EditText) findViewById(R.id.feedback);
                    String feedback = etFeed.getText().toString();

                    text += "_";
                    text += choice1;
                    text += "_";
                    text += choice2;
                    text += "_";
                    text += choice3;
                    text += "_";
                    text += answer;
                    text += "_";
                    text += feedback;

                    printEditorBanner(context);
                    writeQuestionToFile(context);
                    readQuestionFile(context);
                    assign_question++;

                    //Uploading txt to server
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (ActivityCompat.checkSelfPermission(Choices.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
                        } else {
                            canDo = true;
                        }
                    }

                    if (canDo) {
                        Log.d("supertest", "running");
                        try {
                            f = new File(path + name);

                            Log.d("supertest", "in cache dir");
                            Thread t = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    String content_type = getMimeType(f.getPath());
                                    Log.d("warblegarble", f.getPath());

                                    OkHttpClient client = new OkHttpClient();
                                    RequestBody file_body = RequestBody.create(MediaType.parse(content_type), f);
                                    String file_path = f.getAbsolutePath();

                                    RequestBody request_body = new MultipartBody.Builder()
                                            .setType(MultipartBody.FORM)
                                            .addFormDataPart("type", content_type)
                                            .addFormDataPart("uploaded_file", file_path.substring(file_path.lastIndexOf("/") + 1), file_body)
                                            .build();

                                    Log.d("warblegarble", "woah");
                                    Request request = new Request.Builder()
                                            .url("https://shev:Biscut123@megumin.ga/stats/save_file_assignment.php")
                                            .post(request_body)
                                            .build();
                                    try {
                                        Log.d("warblegarble", "running request");
                                        Response response = client.newCall(request).execute();

                                        if (!response.isSuccessful()) {
                                            throw new IOException("Error : " + response);
                                        }
                                        Log.d("warblegarble", "request passed");
                                    } catch (IOException e) {
                                        Log.d("warblegarble", "request failed");
                                        e.printStackTrace();
                                    }
                                }
                            });

                            t.start();
                            Log.d("supertest", "done");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 100 && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
            canDo = true;
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
            }
        }
    }


    private String getMimeType(String path) {
        String extension = MimeTypeMap.getFileExtensionFromUrl(path);

        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
    }

    public boolean check() {
        boolean one = true;
        boolean two = true;
        boolean three = true;
        boolean four = true;
        boolean five = true;

        EditText etOne = (EditText) findViewById(R.id.choice1);
        String choice1 = etOne.getText().toString();
        EditText etTwo = (EditText) findViewById(R.id.choice2);
        String choice2 = etTwo.getText().toString();
        EditText etThree = (EditText) findViewById(R.id.choice3);
        String choice3 = etThree.getText().toString();
        EditText etAnswer = (EditText) findViewById(R.id.answer);
        String answer = etAnswer.getText().toString();
        EditText etFeed = (EditText) findViewById(R.id.feedback);
        String feedback = etFeed.getText().toString();

        if (TextUtils.isEmpty(choice1)) {
            etOne.setError("No choice");
            one = false;
        }
        if (TextUtils.isEmpty(choice2)) {
            etTwo.setError("No choice");
            two = false;
        }
        if (TextUtils.isEmpty(choice3)) {
            etThree.setError("No choice");
            three = false;
        }
        if (TextUtils.isEmpty(answer)) {
            etAnswer.setError("No answer");
            four = false;
        }
        if (TextUtils.isEmpty(feedback)) {
            etFeed.setError("No feedback");
            five = false;
        }
        boolean passed = false;
        if (one && two && three && four && five) {
            passed = true;
        }
        return (passed);
    }


    private void readQuestionFile(Context context) {
        try {
            InputStream inputStream = context.openFileInput("Problem_Set_" + assign + "_Q_"
                    + assign_question + "_" + releaseDate + "_" + dueDate + "_" + endSemesterDate + ".txt");
            is = inputStream;
            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveText = "";
                StringBuilder fileText = new StringBuilder();
                while ((receiveText = bufferedReader.readLine()) != null) {
                    fileText.append(receiveText);
                }
                inputStream.close();
                printToast(context, fileText.toString());
            }
            File fileSet = new File ("Problem_Set_" + assign + "_Q_" + assign_question + "_" + releaseDate + "_" + dueDate + "_" + endSemesterDate + ".txt");
            fileSet.delete();
        } catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }
    }

    private void writeQuestionToFile(Context context) {
        try {
            name = "Problem_Set_" + assign + "_Q_" + assign_question + "_" + releaseDate + "_" + dueDate + "_" + endSemesterDate + ".txt";
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("Problem_Set_" + assign + "_Q_"
                    + assign_question + "_" + releaseDate + "_" + dueDate + "_" + endSemesterDate + ".txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(text);
            outputStreamWriter.close();

        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    private void initSerializeProblemSet(Context context) {
        try {
            InputStream inputStream = context.openFileInput(serializeQuestionSet);
            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveText = "";
                StringBuilder fileText = new StringBuilder();
                while ((receiveText = bufferedReader.readLine()) != null) {
                    fileText.append(receiveText);
                }
                inputStream.close();

                String delim = "[_.]";
                String content = fileText.toString();
                String[] tokens = content.split(delim);
                assign = Integer.parseInt(tokens[0]);
                assign_question = Integer.parseInt(tokens[1]);

            }
        } catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }
    }

    private void printEditorBanner(Context context) {
        Toast.makeText(context.getApplicationContext(), "Creating problem", Toast.LENGTH_LONG).show();
        Toast.makeText(context.getApplicationContext(), "Problem_Set_" + assign + "_Q_"
                + assign_question + "_" + releaseDate + "_" + dueDate + "_" + endSemesterDate + ".txt", Toast.LENGTH_LONG).show();
    }

    private void printToast(Context context, String string) {
        Toast.makeText(context.getApplicationContext(), string, Toast.LENGTH_LONG).show();
    }


    @Override
    public void onBackPressed() {
        Intent i = new Intent(Choices.this, Editor.class);
        i.putExtra("assign", String.valueOf(assign));
        i.putExtra("assign_question", String.valueOf(assign_question));
        startActivity(i);
    }
}
