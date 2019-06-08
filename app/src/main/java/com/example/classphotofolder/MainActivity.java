package com.example.classphotofolder;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.Calendar;


public class MainActivity extends AppCompatActivity implements LocationListener {

    private static final int REQUEST_WRITE_STORAGE = 1;
    private static final int REQUEST_CAMERA = 2;
    private static final int REQUEST_FINE_LOCATION = 3;
    TextView txtDist;
    TextView txtAula;
    AppLocationService appLocationService;
    LocationManager locationManager;
    SharedPreferences sp;
    boolean localEstudo = false;
    boolean hasPermissionCamera;
    boolean hasPermissionGPS;
    boolean hasPermissionStorage;
    Criteria criteria;
    Location currentLocation;
    String bestProvider;
    AulaHelper aulaHelper;

    //BD
    Controller_Horario horario;
    Cursor cur;
    boolean possuiAula;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        horario = new Controller_Horario(getApplicationContext());

        txtDist = (TextView) findViewById(R.id.textCompDist);
        txtDist.setText("");
        txtAula = (TextView) findViewById(R.id.txtAulaAtual);
        txtAula.setText("");

        getGPSPermission();

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
                openHorarios();
            }
        });

        aulaHelper = new AulaHelper(this);
        setTextAula();

        ImageButton imgBtnCamera = (ImageButton) findViewById(R.id.imgBtnCamera);
        imgBtnCamera.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), camera.class);
                startActivity(intent);
            }
        });

    }



    void getGPSPermission(){
        hasPermissionGPS = (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED);
        if (!hasPermissionGPS) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_FINE_LOCATION);
        }
        else {
            verificarDistancia();
        }
    }

    void setTextAula(){
        String aula = aulaHelper.getAulaAtual();
        if(aula.equals("")){
            txtAula.setText("Você não tem nenhuma aula registrada agora.");
            possuiAula = false;
        }
        else {
            txtAula.setText("Aula atual: " + aula);
            possuiAula = true;
        }
    }

    public static boolean isLocationEnabled(Context context)
    {
        int locationMode = 0;
        String locationProviders;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
        {
            try
            {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);
            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
            }
            return locationMode != Settings.Secure.LOCATION_MODE_OFF;
        }
        else
        {
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }
    }

    @SuppressLint("MissingPermission")
    protected void getLocation() {
        if (isLocationEnabled(MainActivity.this)) {
            locationManager = (LocationManager)  this.getSystemService(Context.LOCATION_SERVICE);
            criteria = new Criteria();
            bestProvider = String.valueOf(locationManager.getBestProvider(criteria, true)).toString();

            //You can still do this if you like, you might get lucky:
            currentLocation = locationManager.getLastKnownLocation(bestProvider);
            if (currentLocation != null) {
                Log.e("TAG", "GPS is on");
                double latitude = currentLocation.getLatitude();
                double longitude = currentLocation.getLongitude();
                Toast.makeText(MainActivity.this, "latitude:" + latitude + " longitude:" + longitude, Toast.LENGTH_SHORT).show();
            }
            else{
                //This is what you need:
                locationManager.requestLocationUpdates(bestProvider, 1000, 0, this);
            }
        }
        else
        {
            getGPSPermission();
        }
    }

    public void verificarDistancia(){
        if(sp.contains("LOCATION_LAT") && sp.contains("LOCATION_LON")){
            getLocation();
            double latitudeLocal = currentLocation.getLatitude();
            double longitudeLocal = currentLocation.getLongitude();

            Double lat = Double.parseDouble(sp.getString("LOCATION_LAT", null));
            Double lon = Double.parseDouble(sp.getString("LOCATION_LON", null));

            if(distance(latitudeLocal, longitudeLocal, lat, lon) < 0.6){
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
        hasPermissionStorage = (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
        if (!hasPermissionStorage) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_WRITE_STORAGE);
        }
        else {
            prepCreateFolder();
        }
    }

    private double distance(double lat1, double lng1, double lat2, double lng2) {

        double earthRadius =  6371; // in miles, change to 3958.75 for miles

        double dLat = Math.toRadians(lat2-lat1);
        double dLng = Math.toRadians(lng2-lng1);

        double sindLat = Math.sin(dLat / 2);
        double sindLng = Math.sin(dLng / 2);

        double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
                * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2));

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

        double dist = earthRadius * c;

        System.out.println(dist);

        return dist;
    }

    void prepCreateFolder(){
        hasPermissionCamera = (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED);
        if (!hasPermissionCamera) {
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

    }

    void openHorarios(){
        Intent intent = new Intent(getApplicationContext(), horario.class);
        startActivity(intent);
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

            case REQUEST_FINE_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    verificarDistancia();
                    checkPermissionStorage();
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
        setTextAula();
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
