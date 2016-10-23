package com.harkunwar.cameraintent;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOError;
import java.io.IOException;
import java.util.Date;
import java.text.SimpleDateFormat;

public class MainActivity extends AppCompatActivity {

    private static final int CAMERA_APP_ACTIVITY = 0;
    private ImageView photoCapturedView;
    private String ImageFileLocation = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        photoCapturedView = (ImageView) findViewById(R.id.capturedImage);
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                2000);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 2000: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(MainActivity.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }
            case 2001: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent callCameraApplicationIntent = new Intent();
                    callCameraApplicationIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);

                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                    }
                    catch(IOException e) {
                        e.printStackTrace();
                    }
                    Uri photoURI = FileProvider.getUriForFile(MainActivity.this,
                            BuildConfig.APPLICATION_ID + ".provider",
                            photoFile);
                    callCameraApplicationIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(callCameraApplicationIntent,CAMERA_APP_ACTIVITY);
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(MainActivity.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public void takePhoto(View view) {
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                2001);

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == CAMERA_APP_ACTIVITY && resultCode == RESULT_OK) {
        //    Toast.makeText(this, "Picture Taken Succesfully",Toast.LENGTH_LONG).show();
        //    Bundle extras = data.getExtras();
        //    Bitmap capturedPhotoBitmap = (Bitmap) extras.get("data");
        //    photoCapturedView.setImageBitmap(capturedPhotoBitmap);
            Bitmap photoCapturedImage = BitmapFactory.decodeFile(ImageFileLocation);
            photoCapturedView.setImageBitmap(photoCapturedImage);
        }
    }

    File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "IMAGE_" + timeStamp;
        File storageDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName,".jpg",storageDirectory);
        ImageFileLocation = image.getAbsolutePath();
        return image;
    }
}
