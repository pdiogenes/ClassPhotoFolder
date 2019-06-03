package com.example.classphotofolder;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.File;


public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_WRITE_STORAGE = 1;
    private static final int REQUEST_CAMERA = 2;
    private static final int REQUEST_FINE_LOCATION = 3;
    TextView txtDist;
    AppLocationService appLocationService;
    SharedPreferences sp;
    boolean localEstudo = false;
    boolean hasPermission;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 3);

        txtDist = (TextView) findViewById(R.id.textCompDist);

        sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        appLocationService = new AppLocationService(
                MainActivity.this);

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
                checkPermissionStorage();
            }
        });

    }

    public void verificarDistancia(){
        if(sp.contains("LOCATION_LAT") && sp.contains("LOCATION_LON")){
            Location location = appLocationService
                    .getLocation(LocationManager.GPS_PROVIDER);
            double latitudeLocal = location.getLatitude();
            double longitudeLocal = location.getLongitude();

            Double lat = Double.parseDouble(sp.getString("LOCATION_LAT", null));
            Double lon = Double.parseDouble(sp.getString("LOCATION_LON", null));

            if(distance(latitudeLocal, longitudeLocal, lat, lon) < 0.1){
                txtDist.setVisibility(View.VISIBLE);
                txtDist.setText("Você está no local de estudo registrado!");
                localEstudo = true;
            }
            else{
                txtDist.setVisibility(View.VISIBLE);
                txtDist.setText("Você não está no local de estudo registrado.");
                localEstudo = false;
            }
        }
        else{
            txtDist.setVisibility(View.VISIBLE);
            txtDist.setText("Não há local de estudo registrado. Registrar no menu de localizações abaixo.");
            localEstudo = false;
        }
    }

    void checkPermissionStorage(){
        hasPermission = (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
        if (!hasPermission) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_WRITE_STORAGE);
        }
        else {
            prepCreateFolder();
        }
    }

    private double distance(double lat1, double lng1, double lat2, double lng2) {

        double earthRadius = 3958.75; // in miles, change to 6371 for kilometers

        double dLat = Math.toRadians(lat2-lat1);
        double dLng = Math.toRadians(lng2-lng1);

        double sindLat = Math.sin(dLat / 2);
        double sindLng = Math.sin(dLng / 2);

        double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
                * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2));

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

        double dist = earthRadius * c;

        return dist;
    }

    void prepCreateFolder(){
        hasPermission = (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED);
        if (!hasPermission) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    REQUEST_CAMERA);
        }
        else {
            createFolder();
        }
    }

    public void createFolder() {
        final File f = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "ClassPhotoFolder");
        if (!f.exists()) {
            f.mkdir();
        }
        else{
            Intent intent = new Intent(getApplicationContext(), camera.class);
            startActivity(intent);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CAMERA: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    createFolder();
                } else {
                }
                return;
            }

            case REQUEST_WRITE_STORAGE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    prepCreateFolder();
                } else {
                }
                return;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        verificarDistancia();
    }
}
