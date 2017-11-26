package com.c01;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
  The Browsing activity.
*/
public class Browsing extends AppCompatActivity {

    private static final int READ_REQ = 24;
    private static final int WRITE_REQ = 25;
    private static final int EDIT_REQ = 26;
    private static final int DELETE_REQ = 27;
    private static final int CREATE_REQUEST_CODE = 40;
    private static final int OPEN_REQUEST_CODE = 41;
    private static final int SAVE_REQUEST_CODE = 42;

    ViewGroup cont;
    ListView contactLst;

   /**
   * Starts the activity.
   * @param savedInstanceState The data it most recently supplied on
   * @return No return value
   */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browsing);
    }

    /**
    * Reads files.
    * @param view The content to display
    * @return No return value
    */
    public void readFile(View view) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/*");

        startActivityForResult(intent, READ_REQ);
    }

   /**
   * Gets back the result.
   * @param requestCode The request code allowing to identify who this result came from
   * @param resultCode The result code returned by child activity
   * @param resultData The Intent returning result data to caller
   * @return No return value
   */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent resultData) {

        if (resultCode == Activity.RESULT_OK) {

            Uri uri = null;
            if (resultData != null) {
                uri = resultData.getData();

            }

            if (requestCode == READ_REQ) {
                readTextFile(uri);
            }
          
        }
    }

    /**
    * Reads text files.
    * @param uri uri of content
    * @exception e any exception
    * @return No return value
    */
    private void readTextFile(Uri uri) {
        InputStream inputStream = null;
        try {
            inputStream = getContentResolver().openInputStream(uri);
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    inputStream));

            String line;
            Log.i("", "open text file - content" + "\n");
            while ((line = reader.readLine()) != null) {
                Log.i("", line + "\n");
            }
            reader.close();
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
    * Deletes files.
    * @param uri The Uri of the content
    * @exception e Any exception
    * @return No return value
    */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void deleteFile(Uri uri) {
        Cursor cursor = this.getContentResolver()
                .query(uri, null, null, null, null);
        try {
            if (cursor != null && cursor.moveToFirst()) {
                String flags = cursor.getString(cursor.getColumnIndex(DocumentsContract.Document.COLUMN_FLAGS));
                String[] columns = cursor.getColumnNames();
                for (String col : columns) {
                    Log.i("", "Column Flags  " + col);
                }
                Log.i("", "Delete Flags  " + flags);
                if (flags.contains("" + DocumentsContract.Document.FLAG_SUPPORTS_DELETE)) {
                    DocumentsContract.deleteDocument(getContentResolver(), uri);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cursor.close();
        }
    }
}
