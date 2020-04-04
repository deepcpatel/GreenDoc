package com.jayvaghasiya.test;

import android.Manifest;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    private int RESULT_LOAD_IMG;
//    ImageView plantImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        View lay = (View) findViewById(R.id.rellay);
        lay.setBackgroundResource(R.drawable.leaf);
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("MyPref", 0);
        String name = preferences.getString("name", null);
        TextView welcome = findViewById(R.id.welcomeMessage);
        welcome.setText("Welcome, " +name);
//        plantImage = (ImageView) findViewById(R.id.imageViewPlant);

        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {

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

    public void AddPlantImage(View view) {
        getPhotoFromGallery();
    }

    private void getPhotoFromGallery(){
        Intent photoPicketIntent = new Intent(Intent.ACTION_PICK);
        photoPicketIntent.setType("image/*");
        startActivityForResult(photoPicketIntent, RESULT_LOAD_IMG);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            try{
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                File cachePath = new File(getApplicationContext().getCacheDir(), "images");
                cachePath.mkdirs(); // don't forget to make the directory
                FileOutputStream stream = new FileOutputStream(cachePath + "/image.jpg"); // overwrites this image every time
                selectedImage.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                stream.close();
                File imagePath = new File(this.getCacheDir(), "images");
                File newFile = new File(imagePath, "image.jpg");
                Uri contentUri = FileProvider.getUriForFile(this, "com.jayvaghasiya.test.FileProvider", newFile);
                changeScreen(imageUri);
//                plantImage.setImageBitmap(selectedImage);
            }
            catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    private void changeScreen(Uri imageUri) {
        Intent i = new Intent(this, Main2Activity.class);
        i.putExtra("ImageUri", imageUri);
        startActivity(i);
//        finish();
    }



}
