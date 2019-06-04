package com.example.classphotofolder;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

public class horario extends AppCompatActivity  {

    EditText editDisciplina, editHorarioInicio, editHorarioFim;
    Controller_Horario horario;
    Cursor cursor;
    String[] nomeCampos;
    int[] idViews;
    Spinner horarios;
    ImageButton imgBtnInserir, imgBtnAlterar, imgBtnExcluir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horario);

        horario = new Controller_Horario(getBaseContext());
        imgBtnInserir = (ImageButton) findViewById(R.id.imgBtnInserir);
        imgBtnAlterar = (ImageButton) findViewById(R.id.imgBtnAlterar);
        imgBtnExcluir = (ImageButton) findViewById(R.id.imgBtnExcluir);
        editDisciplina = (EditText) findViewById(R.id.editDisciplina);
        editHorarioInicio = (EditText) findViewById(R.id.editHorarioInicio);
        editHorarioFim = (EditText) findViewById(R.id.editHorarioFim);
        horarios = (Spinner) findViewById(R.id.spnHorarios);
        //preencheAdaptador();

        ImageButton imgBtnInserir = (ImageButton) findViewById(R.id.imgBtnInserir);
        imgBtnInserir.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), novoHorario.class);
                startActivity(intent);
            }
        });
    }

}
