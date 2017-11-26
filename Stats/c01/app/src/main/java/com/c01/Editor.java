package com.c01;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
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
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Editor extends AppCompatActivity {

    private static MathView mathView;
    private static Button question;
    private static Button generateProblemSet;
    private static TextView text;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        Context context = this.getApplicationContext();

        mathView = (MathView) findViewById(R.id.math_view);
        text = (TextView) findViewById(R.id.input_view);
        mathView.setText("");
        question = (Button) findViewById(R.id.generate_question);
        generateProblemSet = (Button) findViewById(R.id.generate_problem_set);
        String path = "/data/data/com.c01/files/";

        releaseDate = getIntent().getStringExtra("releaseDate");
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
            }
        });

        question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                printEditorBanner(context);
                writeQuestionToFile(context);
                readQuestionFile(context);
                assign_question++;

                //Uploading txt to server
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ActivityCompat.checkSelfPermission(Editor.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
                    } else {
                        canDo = true;
                    }
                }

                if (canDo) {
                    Log.d("supertest", "running");
                    try {
                        f = new File(path + name);
                        System.out.println(f.getAbsoluteFile());

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
        });

        generateProblemSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                assign_question = 1;
                printToast(context, "Problem Set " + assign +" has been finalized.");
                assign++;
                serializeProblemSet(context);
            }
        });
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
            outputStreamWriter.write(text.getText().toString());
            outputStreamWriter.close();

        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
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

    @Override
    public void onBackPressed() {
        Intent i = new Intent(Editor.this, CreateProblemSet.class);
        startActivity(i);
    }
}
