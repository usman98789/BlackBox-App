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
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;
import com.nishant.math.MathView;

import org.apache.commons.io.FileUtils;
import org.apache.ivy.util.FileUtil;

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

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
* The activity of editor.
*/
public class Editor extends AppCompatActivity {

    private static MathView mathView;
    private static Button question;
    private static Button problemSet;
    private static TextView text;
    private static int assign = 1;
    private static int assign_question = 1;
    private static Boolean canDo = false;
    private static String name;
    private static File f;
    private static InputStream is;

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
        question = (Button) findViewById(R.id.generate_question);
        problemSet = (Button) findViewById(R.id.generate_problem_set);
        String path = "/data/data/com.c01/files/";

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

        question.setOnClickListener(new View.OnClickListener() {
            
            /**
            * Responds when a click happened.
            * @param view The content to display
            * @exception e IOException, FileNotFoundException
            * @return No return value
            */
            @Override
            public void onClick(View view) {
                Toast.makeText(context.getApplicationContext(), "Creating problem",
                        Toast.LENGTH_LONG).show();
                Toast.makeText(context.getApplicationContext(), "Problem_Set_" + assign + "_Q_" + assign_question + ".txt",
                        Toast.LENGTH_LONG).show();
                try {
                    name = "Problem_Set_" + assign + "_Q_" + assign_question + ".txt";
                    OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("Problem_Set_" + assign + "_Q_" + assign_question + ".txt", Context.MODE_PRIVATE));
                    outputStreamWriter.write(text.getText().toString());
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

                // Uploading txt to server
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

                        Log.d("supertest", "in cache dir");
                        Thread t = new Thread(new Runnable() {
                            
                            /**
                            * Warblegarble request.
                            * @exception e IOException
                            * @return No return value
                            */
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
            }
        });
    }

    /**
    * Get the list of files.
    * @param parentDir The parent directory
    * @return List of files
    */
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

    /**
    * Callback for the result from requesting permissions.
    * @param requestCode The request code passed in
    * @param permissions The requested permissions
    * @param grantResults The corresponding permissions
    * @return No return value
    */
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

    /**
    * Return one of the possible clip MIME types.
    * @param path The path
    * @return One of the possible clip MIME types
    */
    private String getMimeType(String path) {
        String extension = MimeTypeMap.getFileExtensionFromUrl(path);

        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
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
