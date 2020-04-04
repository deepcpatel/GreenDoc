package com.jayvaghasiya.test;

import android.Manifest;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.json.JSONArray;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class Main2Activity extends AppCompatActivity {

    ImageView plantImageView;
    Button cancelButton;
    Button confirmButton;
    Uri imageUri;
    File newFile;
    String selectedImagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        plantImageView = findViewById(R.id.plantImageView);
        setImage();
//        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
//        actionBar.setHomeButtonEnabled(true);
//        actionBar.setDisplayHomeAsUpEnabled(true);
        cancelButton = findViewById(R.id.cancelButton);
        confirmButton = findViewById(R.id.confirmButton);
        ActivityCompat.requestPermissions(Main2Activity.this, new String[]{Manifest.permission.INTERNET}, 2);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            }
        });
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i = new Intent(getApplicationContext(), ResultActivity.class);
//                startActivity(i);


                connectServer(v);
            }
        });
    }


    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    Toast.makeText(getApplicationContext(), "Access to Storage Permission Granted. Thanks.", Toast.LENGTH_SHORT).show();
                } else {
//                    Toast.makeText(getApplicationContext(), "Access to Storage Permission Denied.", Toast.LENGTH_SHORT).show();
                }
                return;
            }
            case 2: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    Toast.makeText(getApplicationContext(), "Access to Internet Permission Granted. Thanks.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Access to Internet Permission Denied.", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    void postRequest(String postUrl, RequestBody postBody) {

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(postUrl)
                .post(postBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // Cancel the post on failure.
                call.cancel();
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Failed to Connect to Server", Toast.LENGTH_SHORT).show();
                    }
                });



            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                // In order to access the TextView inside the UI thread, the code is executed inside runOnUiThread()

                Intent j = new Intent(getBaseContext(), ResultActivity.class);
                j.putExtra("result", response.body().string());
                startActivity(j);

//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            Toast.makeText(getApplicationContext(), response.body().string(), Toast.LENGTH_SHORT).show();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });

            }
        });
    }


    void connectServer(View v){

        String postUrl= "http://18.207.225.12:8000/";


//        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inPreferredConfig = Bitmap.Config.RGB_565;
        // Read BitMap by file path
//        String path = imageUri.toString();
//        selectedImagePath = getPath(getApplicationContext(), imageUri);

//        File file = new File(imageUri.getPath());

//        try {
//            final InputStream imageStream = getContentResolver().openInputStream(imageUri);
//            final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
////            Bitmap bitmap = BitmapFactory.decodeFile(selectedImagePath, options);
//            selectedImage.compress(Bitmap.CompressFormat.JPEG, 100, stream);
//            byte[] byteArray = stream.toByteArray();
//
//            RequestBody postBodyImage = new MultipartBody.Builder()
//                    .setType(MultipartBody.FORM)
//                    .addFormDataPart("image", "androidFlask.jpg", RequestBody.create(MediaType.parse("image/*jpg"), byteArray))
//                    .build();
//
////            Toast.makeText(this, "Please wait ...", Toast.LENGTH_SHORT).show();
//
//            postRequest(postUrl, postBodyImage);
//        } catch (Exception e){
//            e.printStackTrace();
//        }


        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;

//        selectedImagePath = getPath(getApplicationContext(), imageUri);
        // Read BitMap by file path
        try {

            selectedImagePath = getPath(getApplicationContext(), imageUri);
            Bitmap bitmap = BitmapFactory.decodeFile(selectedImagePath, options);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            RequestBody postBodyImage = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("image", "androidFlask.jpg", RequestBody.create(MediaType.parse("image/*jpg"), byteArray))
                    .build();

            Toast.makeText(this, "Please wait ...", Toast.LENGTH_SHORT).show();

            postRequest(postUrl, postBodyImage);
        }  catch (Exception e) {
            e.printStackTrace();
        }



    }


//    public class MyRunnable implements Runnable {
//
//        private int var;
//
//        public MyRunnable(int var) {
//            this.var = var;
//        }
//
//        public void run() {
//            // code in the other thread, can reference "var" variable
//            String Url = "https://webhook.site/3c0cde70-d489-4fd9-b110-b70bd5ec522e";
////        String Url = "https://www.codepunker.com";
//
//            try {
//                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
//                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
//                File cachePath = new File(getApplicationContext().getCacheDir(), "images");
//                cachePath.mkdirs(); // don't forget to make the directory
//                FileOutputStream stream = new FileOutputStream(cachePath + "/image.jpg"); // overwrites this image every time
//                selectedImage.compress(Bitmap.CompressFormat.JPEG, 100, stream);
//                stream.close();
//                File imagePath = new File(getApplicationContext().getCacheDir(), "images");
//                newFile = new File(imagePath, "image.jpg");
//
//                AsyncHttpClient client = new AsyncHttpClient();
//                RequestParams params = new RequestParams();
//                params.put("pic", newFile);
//                client.post(Url, params, new JsonHttpResponseHandler(){
//
//                    @Override
//                    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
//                        System.out.println(response);
//                    }
//
//                    @Override
//                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//                        System.out.println(throwable);
//                    }
//                });
//            }
//            catch (FileNotFoundException e){
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }


    private  void  setImage(){
        Bundle ex = getIntent().getExtras();
        imageUri = ex.getParcelable("ImageUri");
        plantImageView.setImageURI(imageUri);
    }

    public static String getPath(final Context context, final Uri uri) {

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
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
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
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }
}
