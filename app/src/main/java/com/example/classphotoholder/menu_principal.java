package com.example.classphotoholder;

import android.content.Intent;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class menu_principal extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);

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
                dispatchTakePictureIntent();
            }
        });
    } // fim do onCreate

    static final int REQUEST_IMAGE_CAPTURE = 1;

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

}
