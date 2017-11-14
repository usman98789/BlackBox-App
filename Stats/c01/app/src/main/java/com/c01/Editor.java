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

import maximsblog.blogspot.com.jlatexmath.*;

import maximsblog.blogspot.com.jlatexmath.core.Insets;
import maximsblog.blogspot.com.jlatexmath.core.TeXConstants;
import maximsblog.blogspot.com.jlatexmath.core.TeXFormula;
import maximsblog.blogspot.com.jlatexmath.core.TeXIcon;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Editor extends AppCompatActivity {

    private static MathView mathView;
    private static Button question;
    private static Button problemSet;
    private static TextView text;
    private static int assign = 1;
    private static int assign_question = 1;
    Boolean canDo = false;
    String name;
    File f;
    InputStream is;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        Context context = this.getApplicationContext();



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
                Toast.makeText(context.getApplicationContext(), "Problem_Set_" + assign + "_Q_" + assign_question + ".txt",
                        Toast.LENGTH_LONG).show();
                try {
                    name = "Problem_Set_" + assign + "_Q_" + assign_question;
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
//                    new MaterialFilePicker().withActivity(Editor.this).withRequestCode(10).start();
                    try {
                        f = File.createTempFile(name, "");
                        is = context.openFileInput(name);
                        FileUtils.copyInputStreamToFile(is, f);

                        Log.d("supertest", "in cache dir");
//                        for (File j : dir.listFiles()) {
//                            //perform here your operation
//                            f = j;
//                            if (j.getName().equals(name)) {
//                                break;
//                            }
//                        }

                        progress = new ProgressDialog(Editor.this);
                        progress.setTitle("Uploading");
                        progress.setMessage("Please wait...");
                        progress.show();

                        Thread t = new Thread(new Runnable() {
                            @Override
                            public void run() {
//                        f = new File(data.getStringExtra((FilePickerActivity.RESULT_FILE_PATH)));
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
                                    progress.dismiss();
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
            @Override
            public void onClick(View view) {
                assign_question = 1;
                Toast.makeText(context.getApplicationContext(), "Problem Set " + assign +" has been uploaded",
                        Toast.LENGTH_LONG).show();
                assign++;
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

    ProgressDialog progress;

    private String getMimeType(String path) {
        String extension = MimeTypeMap.getFileExtensionFromUrl(path);

        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
    }
    //---------------------------------------------
}
