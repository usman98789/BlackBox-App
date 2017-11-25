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
    private static Boolean canDo = false;
    private static String name;
    private static File f;
    private static InputStream is;
    private static String text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choices);
        Context context = this.getApplicationContext();
        text = getIntent().getStringExtra("question");
        assign = Integer.valueOf(getIntent().getStringExtra("assign"));
        assign_question = Integer.valueOf(getIntent().getStringExtra("assign_question"));

        question = (Button) findViewById(R.id.generate_problem);
        String path = "/data/data/com.c01/files/";


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


                    Toast.makeText(context.getApplicationContext(), "Creating problem",
                            Toast.LENGTH_LONG).show();
                    Toast.makeText(context.getApplicationContext(), "Problem_Set_" + assign + "_Q_" + assign_question + ".txt",
                            Toast.LENGTH_LONG).show();
                    try {
                        name = "Problem_Set_" + assign + "_Q_" + assign_question + ".txt";
                        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("Problem_Set_" + assign + "_Q_" + assign_question + ".txt", Context.MODE_PRIVATE));
                        outputStreamWriter.write(text);
                        outputStreamWriter.close();

                    } catch (IOException e) {
                        Log.e("Exception", "File write failed: " + e.toString());
                    }

                    String ret = "";
                    try {
                        InputStream inputStream = context.openFileInput("Problem_Set_" + assign + "_Q_" + assign_question + ".txt");
                        is = inputStream;
                        if (inputStream != null) {
                            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                            String receiveString = "";
                            StringBuilder stringBuilder = new StringBuilder();

                            while ((receiveString = bufferedReader.readLine()) != null) {
                                stringBuilder.append(receiveString);
                            }

                            inputStream.close();
                            ret = stringBuilder.toString();
                            Toast.makeText(context.getApplicationContext(), ret,
                                    Toast.LENGTH_LONG).show();
                        }
                    } catch (FileNotFoundException e) {
                        Log.e("login activity", "File not found: " + e.toString());
                    } catch (IOException e) {
                        Log.e("login activity", "Can not read file: " + e.toString());
                    }

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

    private List<File> getListFiles(File parentDir) {
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

    @Override
    public void onBackPressed() {
        Intent i = new Intent(Choices.this, CountQuestions.class);
        i.putExtra("assign", String.valueOf(assign));
        i.putExtra("assign_question", String.valueOf(assign_question));
        startActivity(i);
    }
}
