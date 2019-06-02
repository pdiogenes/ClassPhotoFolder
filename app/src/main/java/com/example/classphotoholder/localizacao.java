package com.example.classphotoholder;

import android.app.Activity;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class localizacao extends AppCompatActivity implements OnMapReadyCallback {

    private SupportMapFragment fragment;
    private GoogleMap map;
    AppLocationService appLocationService;
    EditText editEndereco;
    Button btnProcurar;
    Button btnAdicionar;
    double latitudeLocal;
    double longitudeLocal;
    LatLng manter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_localizacao);
        editEndereco = (EditText) findViewById(R.id.txtEndereco);
        editEndereco.setHint("Digite o endereço.");

        btnProcurar = (Button) findViewById(R.id.btnProcurarEndereco);
        btnProcurar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                procurarEndereco();
            }
        });

        btnAdicionar = (Button) findViewById(R.id.btnAdicionarEndereco);
        btnAdicionar.setVisibility(View.INVISIBLE);
        btnAdicionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adicionarEndereço();
            }
        });

        getEnderecoAtual();
        FragmentManager fm = getSupportFragmentManager();
        fragment = (SupportMapFragment) fm.findFragmentById(R.id.map);
        if (fragment == null) {
            fragment = SupportMapFragment.newInstance();
            fm.beginTransaction().replace(R.id.map, fragment).commit();
        }

        fragment.getMapAsync(this);
    }

    public void procurarEndereco(){
        String endereco;
        endereco = editEndereco.getText().toString();
        LatLng local = getLocationFromAddress(endereco);
        if(local != null){
            addMarker(local);
            btnAdicionar.setVisibility(View.VISIBLE);
            manter = local;
        }
    }

    public void getEnderecoAtual(){
        appLocationService = new AppLocationService(
                localizacao.this);

        Location location = appLocationService
                .getLocation(LocationManager.GPS_PROVIDER);

        latitudeLocal = location.getLatitude();
        longitudeLocal = location.getLongitude();
    }

    public void adicionarEndereço(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        sharedPreferences.edit().putString("LOCATION_LAT", String.valueOf(manter.latitude)).apply();
        sharedPreferences.edit().putString("LOCATION_LON", String.valueOf(manter.longitude)).apply();
        Toast toast = Toast.makeText(getApplicationContext(), "Local de estudos adicionado!", Toast.LENGTH_SHORT);
        toast.show();
    }

    public LatLng getLocationFromAddress(String endereco){

        Geocoder coder = new Geocoder(this);
        List<Address> address;
        LatLng p1 = null;

        try {
            address = coder.getFromLocationName(endereco,5);
            if (address==null) {
                return null;
            }
            Address location=address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new LatLng((location.getLatitude()),
                    (location.getLongitude()));

            return p1;
        }
        catch(IOException e){
            return null;
        }
    }

    public void addMarker(LatLng local){
        map.clear();
        map.addMarker(new MarkerOptions().position(local)
                .title(editEndereco.getText().toString()));
        map.moveCamera(CameraUpdateFactory.newLatLng(local));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(local,15));
        // Zoom in, animating the camera.
        map.animateCamera(CameraUpdateFactory.zoomIn());
        // Zoom out to zoom level 10, animating with a duration of 2 seconds.
        map.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
    }

    public boolean containsAddress(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        return (sharedPreferences.contains("LOCATION_LAT") && sharedPreferences.contains("LOCATION_LON"));
    }

    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        LatLng location;
        if(containsAddress()){
            String lat = sharedPreferences.getString("LOCATION_LAT", null);
            String lon = sharedPreferences.getString("LOCATION_LON", null);
            location = new LatLng(Double.parseDouble(lat),Double.parseDouble(lon));
        }
        else{
            location = new LatLng(latitudeLocal, longitudeLocal);
        }
        map.addMarker(new MarkerOptions().position(location)
                .title("Localização Atual"));
        map.moveCamera(CameraUpdateFactory.newLatLng(location));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(location,15));
        // Zoom in, animating the camera.
        map.animateCamera(CameraUpdateFactory.zoomIn());
        // Zoom out to zoom level 10, animating with a duration of 2 seconds.
        map.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
    }
}
