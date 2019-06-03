package com.example.classphotofolder;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class horario extends AppCompatActivity {

    TextView txtvHorario = (TextView) findViewById(R.id.txtvHorario);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horario);
    }

    CalendarService.readCalendar(class.this);
}
