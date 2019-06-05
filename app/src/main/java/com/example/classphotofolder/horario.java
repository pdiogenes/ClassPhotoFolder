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

    Controller_Horario horario;
    Cursor cursor;
    String[] nomeCampos;
    int[] idViews;
    Spinner horarios;
    ImageButton imgBtnInserir, imgBtnExcluir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horario);

        cursor = horario.preencheSpinner(); 
        nomeCampos = new String[]{DBHelper.COLUNA_NOME_DISCIPLINA, DBHelper.COLUNA_DIA_SEMANA, DBHelper.COLUNA_HORA_INICIO, DBHelper.COLUNA_HORA_FIM};
        idViews = new int[]{R.id.txtNomeDisciplina, R.id.txtDiaSemana, R.id.txtHoraInicio, R.id.txtHoraFim};
        SimpleCursorAdapter adaptador = new SimpleCursorAdapter(getBaseContext(), R.layout.spinner_horarios, cursor, nomeCampos, idViews, 0);
        produtos.setAdapter(adaptador);


        horario = new Controller_Horario(getBaseContext());
        imgBtnInserir = (ImageButton) findViewById(R.id.imgBtnInserir);
        imgBtnExcluir = (ImageButton) findViewById(R.id.imgBtnExcluir);
        horarios = (Spinner) findViewById(R.id.spnHorarios);
        horario.preencheSpinner();


        ImageButton imgBtnInserir = (ImageButton) findViewById(R.id.imgBtnInserir);
        imgBtnInserir.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), novoHorario.class);
                startActivity(intent);
            }
        });
    }

}
