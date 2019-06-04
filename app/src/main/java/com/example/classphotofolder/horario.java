package com.example.classphotofolder;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class horario extends AppCompatActivity implements View.OnClickListener {

    TextView txtvHorario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horario);
        txtvHorario = findViewById(R.id.txtvHorario);
    }

}
