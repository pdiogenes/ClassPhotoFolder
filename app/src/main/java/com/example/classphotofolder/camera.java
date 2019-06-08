package com.example.classphotofolder;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

public class camera extends AppCompatActivity {
    private Camera mCamera;
    private CameraPreview mPreview;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    AulaHelper aulaHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        getSupportActionBar().hide(); //hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //show the activity in full screen
        setContentView(R.layout.activity_camera);

        // Create an instance of Camera
        mCamera = getCameraInstance();
        aulaHelper = new AulaHelper(this);


        // Create our Preview view and set it as the content of our activity.
        mPreview = new CameraPreview(this, mCamera);
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(mPreview);

                // Add a listener to the Capture button
        ImageButton captureButton = (ImageButton) findViewById(R.id.button_capture);
        captureButton.setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // get an image from the camera
                    String aula = aulaHelper.getAulaAtual();
                    if(aula.equals("")){
                        sendToast();
                    }
                    else {
                      mCamera.takePicture(null, null, mPicture);
                      photoTaken();
                    }
                }
            }
        );

        ImageButton folderButton = (ImageButton) findViewById(R.id.button_folder);
        folderButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // open dir
                        String aula = aulaHelper.getAulaAtual();
                        if(aula.equals("")){
                            sendToast();
                        }
                        else {
                          Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                          Uri uri = Uri.parse(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                                  + "/pictures/ClassPhotoFolder/");
                          intent.setDataAndType(uri, "text/csv");
                          startActivity(Intent.createChooser(intent, "Open folder"));
                        }                        
                    }
                }
        );
    } // fim do onCreate


    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            return true;
        } else {
            return false;
        }
    }

    void sendToast(){
        Toast.makeText(this, "Você não tem nenhuma aula registrada agora!", Toast.LENGTH_SHORT).show();
    }

    void photoTaken(){
        Toast.makeText(this, "Foto tirada. Voltando para o menu.", Toast.LENGTH_SHORT).show();
        this.finish();
    }

    public static Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }

    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            String disciplina = aulaHelper.getAulaAtual();
            File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE,disciplina);
            if (pictureFile == null){
                Toast.makeText(getApplicationContext(), "Error creating media file, check storage permissions", Toast.LENGTH_LONG).show();
                return;
            }

            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data);
                fos.close();
            } catch (FileNotFoundException e) {
                Toast toast = Toast.makeText(getApplicationContext(), "File not found", Toast.LENGTH_SHORT);
            } catch (IOException e) {
                Toast toast = Toast.makeText(getApplicationContext(), "Erro acessando arquivo", Toast.LENGTH_SHORT);
            }
        }
    };


/** Create a file Uri for saving an image or video */
    private static Uri getOutputMediaFileUri(int type , String disciplina){
          return Uri.fromFile(getOutputMediaFile(type , disciplina));
    }

    /** Create a File for saving an image or video */
    private static File getOutputMediaFile(int type, String disciplina){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "ClassPhotoFolder/"+disciplina);
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                //Toast toast = Toast.makeText(getApplicationContext(), "failed to create directory", Toast.LENGTH_SHORT);
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
            "IMG_"+ timeStamp + ".jpg");
        } else if(type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
            "VID_"+ timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }

}
