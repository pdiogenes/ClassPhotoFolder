package com.example.classphotoholder;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.Date;

public class menu_principal extends AppCompatActivity {

    private static final String TAG = "address" ;
    private static final int REQUEST_WRITE_STORAGE = 1 ;
    public boolean hasCamera;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);

        boolean hasPermission = (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);

        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA}, 2);

        if (!hasPermission) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_WRITE_STORAGE);
        }

        else {
           createFolder();
        }


        /*if(checkCameraHardware(this)){
            hasCamera = true;
        }*/



        final Button btnLocalizacao = findViewById(R.id.btnLocalizacao);
        btnLocalizacao.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), localizacao.class);
                startActivity(intent);
            }
        });

        final Button btnHorario = findViewById(R.id.btnHorario);
        btnHorario.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), horario.class);
                startActivity(intent);
            }
        });


        ImageButton imgBtnCamera = (ImageButton) findViewById(R.id.imgBtnCamera);
        imgBtnCamera.setOnClickListener(new View.OnClickListener() {
             public void onClick(View v) {
                //dispatchTakePictureIntent();
                Intent intent = new Intent(getApplicationContext(), camera.class);
                startActivity(intent);
            }
        });
    } // fim do onCreate
    //-----------------------------------------------------------------
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode)
        {
            case REQUEST_WRITE_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    //reload my activity with permission granted or use the features what required the permission
                    createFolder();

                } else
                {
                    Toast.makeText(this, "The app was not allowed to write to your storage. Hence, it cannot function properly. Please consider granting it this permission", Toast.LENGTH_LONG).show();
                }
            }
        }

    }

    public void createFolder() {

        final File f = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "ClassPhotoFolder");

        if (!f.exists()) {
            Toast.makeText(this, "Folder doesn't exist, creating it...", Toast.LENGTH_SHORT).show();
            boolean rv = f.mkdir();
            Toast.makeText(this, "Folder creation " + ( rv ? "success" : "failed"), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Folder already exists.", Toast.LENGTH_SHORT).show();
        }

    }










    //------------------------------------------------------------

    static final int REQUEST_TAKE_PHOTO = 1;

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    String currentPhotoPath;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";

        System.out.println(Environment.DIRECTORY_PICTURES);
        final File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "ClassPhotoFolder");
        //File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        storageDir.mkdirs();
        String imgName = imageFileName+".jpg";
        final File image = new File(storageDir, imgName);

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    /*private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }*/


}
