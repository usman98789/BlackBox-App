package com.c01;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
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

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
  The activity for editting file contect.
*/
public class EditFileContent extends AppCompatActivity {

    private static TextView content;
    private static Button changeFile;
    private static Context context;
    private static int assign;
    private static int assign_question;
    private static String name;
    private static InputStream is;
    private static Boolean canDo = false;
    private static File f;
    private static String path;
    private static Intent i;

    /**
    * Starts the activity.
    * @param savedInstanceState The data it most recently supplied on
    * @return No return value
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_file_content);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context = getApplicationContext();

        i = getIntent();
        EditText editText = (EditText) findViewById(R.id.input_view_edit);

        content = (TextView) findViewById(R.id.input_view_edit);
        //changeFile = (Button) findViewById(R.id.edit_question);
        MathView mathView = (MathView) findViewById(R.id.math_view_edit);
        assign = i.getIntExtra("assign", 0);
        assign_question = i.getIntExtra("assign_question", 0);
        System.out.println("Problem set -> " + assign + " Question Number -> " + assign_question);
        String text = i.getStringExtra("text");

        editText.setText(text);
        mathView.setText(content.getText().toString());

        content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after){
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mathView.setText(content.getText().toString());
                System.out.println("X : " + mathView.getX() + " Y: " + mathView.getY() + " Z: " + mathView.getZ());
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Problem set -> " + assign + " Question Number -> " + assign_question);
                Snackbar.make(view, "Editted Finalized Edit", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                Toast.makeText(context.getApplicationContext(), "Editing problem",
                        Toast.LENGTH_LONG).show();
                Toast.makeText(context.getApplicationContext(), "Problem_Set_" + assign + "_Q_" + assign_question + ".txt",
                        Toast.LENGTH_LONG).show();
                try {
                    name = "Problem_Set_" + assign + "_Q_" + assign_question + ".txt";
                    OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("Problem_Set_" + assign + "_Q_" + assign_question + ".txt", Context.MODE_PRIVATE));
                    outputStreamWriter.write(content.getText().toString());
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
                    if (ActivityCompat.checkSelfPermission(EditFileContent.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
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

    @Override
    public void onBackPressed() {
        Intent i = new Intent(EditFileContent.this, CreateProblemSet.class);
        startActivity(i);
    }
}
