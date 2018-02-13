package com.c01;

import android.Manifest;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.MimeTypeMap;
import android.widget.TextView;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
* The activity for instructor menu.
*/
public class InstructorMenu extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Boolean canDo = false;

    /**
    * Starts the activity.
    * @param savedInstanceState The data it most recently supplied on
    * @return No return value
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {

            /**
            * Responds when a click happened.
            * @param view The content to display
            * @return No return value
            */
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Feature in development..", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    /**
    * Responds when user presses the back key.
    * @return No return value
    */
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //super.onBackPressed();
        }
    }

    /**
    * Initialize the contents of the Activity's standard options menu.
    * @param menu The options menu in which items are placed.
    * @return boolean True for the menu to be displayed
    */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.instructor_menu, menu);
        return true;
    }

    /**
    * Called when an item in options menu is selected.
    * @param item The menu item being selected.
    * @return boolean True to consume it here, or false to allow normal menu processing to proceed
    */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handles action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        // Noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
    * Called when an item in the navigation menu is selected.
    * @param item The selected item.
    * @return boolean True to display the item as the selected item
    */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handles navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_view_lecture_notes) {
            Intent i = new Intent(InstructorMenu.this, Browsing.class);
            startActivity(i);
        } else if (id == R.id.nav_add_assignments) {
            Intent i = new Intent(InstructorMenu.this, CreateProblemSet.class);
            startActivity(i);
        }  else if (id == R.id.nav_manage) {
            Intent i = new Intent(InstructorMenu.this, PostAnnouncement.class);
            startActivity(i);
        } else if (id == R.id.nav_logout) {
            Intent i = new Intent(InstructorMenu.this, MainActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_add_student) {
            Intent i = new Intent(InstructorMenu.this, addStudent.class);
            startActivity(i);
        } else if (id == R.id.nav_add_lecture_notes) {
            //Check the phone to see if it has permission to access files
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
                } else {
                    canDo = true;
                }
            }

            // If permission is granted open file explorer to choose file to upload
            if(canDo) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                startActivityForResult(intent, 10);
            }
        }  else if (id == R.id.nav_edit_grades) {
            Intent i = new Intent(InstructorMenu.this, EditGrades.class);
            startActivity(i);
        } else if (id == R.id.nav_add_Professor){
            Intent i = new Intent(InstructorMenu.this, addProfessor.class);
            startActivity(i);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
        if(requestCode == 100 && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
            canDo = true;
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
            }
        }
    }

    /**
    * Gets back the result.
    * @param requestCode The request code allowing to identify who this result came from
    * @param resultCode The result code returned by child activity
    * @param resultData The Intent returning result data to caller
    * @return No return value
    */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        if(requestCode == 10 && resultCode == RESULT_OK) {
            Context context = this;

            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    String path = "";

                    // Gets the extension of the file to upload so we can generate it's mime type
                    // and get it's url to create a file object
                    Uri uri = data.getData();
                    path = data.getData().getPath().toString();
                    int index = path.lastIndexOf(".");


                    if (index != -1) {
                        path = path.substring(path.lastIndexOf(":") + 1);
                    } else {
                        path = getPath(context, uri);
                        if (path == null) {
                            path = FilenameUtils.getName(uri.toString());
                        }
                    }
                    String temp = path.substring(path.lastIndexOf("."));
                    String content_type = getMimeType("temp" + temp);
                    File f = new File(path);
                    Log.d("supertest", path);

                    // Starts building an http post request to send the file to the server
                    OkHttpClient client = new OkHttpClient();
                    if (content_type == null) {
                        Log.d("supertest", "null");
                    }
                    RequestBody file_body = RequestBody.create(MediaType.parse(content_type), f);
                    String file_path = f.getAbsolutePath();

                    RequestBody request_body = new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("type", content_type)
                            .addFormDataPart("uploaded_file", file_path.substring(file_path.lastIndexOf("/") + 1), file_body)
                            .build();

                    Request request = new Request.Builder()
                            .url("https://shev:Biscut123@megumin.ga/stats/save_file.php")
                            .post(request_body)
                            .build();

                    // Sends the file to the server
                    try {
                        Log.d("warblegarble", "running request");
                        Response response = client.newCall(request).execute();

                        if (!response.isSuccessful()) {
                            throw new IOException("Error : " +response);
                        }
                        Log.d("warblegarble", "request passed");
                    } catch (IOException e) {
                        Log.d("warblegarble", "request failed");
                        e.printStackTrace();
                    }


                }
            });

            t.start();
        }
    }

    /**
    * Return one of the possible clip MIME types.
    * @param path The path
    * @return String One of the possible clip MIME types
    */
    public String getMimeType(String path) {
        String extension = MimeTypeMap.getFileExtensionFromUrl(path);

        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
    }

    /**
    * Gets the path.
    * @param context The context
    * @param uri The uri
    * @return String The path
    */
    public static String getPath(Context context, Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else
            if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {split[1]};
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    /**
    * Gets the data column.
    * @param context The context
    * @param uri The uri
    * @param selection Things being selected
    * @return String The index
    */
    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = { column };
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
    * Check if it is external storage document.
    * @param uri The uri
    * @return boolean True if it is external storage document, or false otherwise
    */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

}
